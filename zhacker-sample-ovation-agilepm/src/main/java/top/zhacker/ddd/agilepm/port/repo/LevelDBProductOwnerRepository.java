//   Copyright 2012,2013 Vaughn Vernon
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

package top.zhacker.ddd.agilepm.port.repo;

import org.springframework.stereotype.Repository;
import top.zhacker.ddd.agilepm.domain.team.member.ProductOwner;
import top.zhacker.ddd.agilepm.domain.team.member.ProductOwnerRepository;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;
import top.zhacker.boot.leveldb.AbstractLevelDBRepository;
import top.zhacker.boot.leveldb.LevelDBKey;
import top.zhacker.boot.leveldb.LevelDBUnitOfWork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Repository
public class LevelDBProductOwnerRepository
        extends AbstractLevelDBRepository
        implements ProductOwnerRepository {

    private static final String PRIMARY = "PRODUCTOWNER#PK";
    private static final String PRODUCT_OWNER_OF_TENANT = "PRODUCTOWNER#T";
    
    @Override
    public Collection<ProductOwner> allProductOwnersOfTenant(TenantId aTenantId) {
        List<ProductOwner> productOwners = new ArrayList<ProductOwner>();

        LevelDBKey productOwnersOfTenant = new LevelDBKey(PRODUCT_OWNER_OF_TENANT, aTenantId.id());

        LevelDBUnitOfWork uow = LevelDBUnitOfWork.readOnly(this.database());

        List<Object> keys = uow.readKeys(productOwnersOfTenant);

        for (Object productOwnerId : keys) {
            ProductOwner productOwner = uow.readObject(productOwnerId.toString().getBytes(), ProductOwner.class);

            if (productOwner != null) {
                productOwners.add(productOwner);
            }
        }

        return productOwners;
    }

    @Override
    public ProductOwner productOwnerOfIdentity(TenantId aTenantId, String aUsername) {
        LevelDBKey primaryKey = new LevelDBKey(PRIMARY, aTenantId.id(), aUsername);

        ProductOwner productOwner =
                LevelDBUnitOfWork.readOnly(this.database())
                    .readObject(primaryKey.key().getBytes(), ProductOwner.class);

        return productOwner;
    }

    @Override
    public void remove(ProductOwner aProductOwner) {
        LevelDBKey lockKey = new LevelDBKey(PRIMARY, aProductOwner.getTenantId().id());

        LevelDBUnitOfWork uow = LevelDBUnitOfWork.current();

        uow.lock(lockKey.key());

        this.remove(aProductOwner, uow);
    }

    @Override
    public void removeAll(Collection<ProductOwner> aProductOwnerCollection) {
        boolean locked = false;

        LevelDBUnitOfWork uow = LevelDBUnitOfWork.current();

        for (ProductOwner productOwner : aProductOwnerCollection) {
            if (!locked) {
                LevelDBKey lockKey = new LevelDBKey(PRIMARY, productOwner.getTenantId().id());

                uow.lock(lockKey.key());

                locked = true;
            }

            this.remove(productOwner, uow);
        }
    }

    @Override
    public void save(ProductOwner aProductOwner) {
        LevelDBKey lockKey = new LevelDBKey(PRIMARY, aProductOwner.getTenantId().id());

        LevelDBUnitOfWork uow = LevelDBUnitOfWork.current();

        uow.lock(lockKey.key());

        this.save(aProductOwner, uow);
    }

    @Override
    public void saveAll(Collection<ProductOwner> aProductOwnerCollection) {
        boolean locked = false;

        LevelDBUnitOfWork uow = LevelDBUnitOfWork.current();

        for (ProductOwner productOwner : aProductOwnerCollection) {
            if (!locked) {
                LevelDBKey lockKey = new LevelDBKey(PRIMARY, productOwner.getTenantId().id());

                uow.lock(lockKey.key());

                locked = true;
            }

            this.save(productOwner, uow);
        }
    }

    private void remove(ProductOwner aProductOwner, LevelDBUnitOfWork aUoW) {
        LevelDBKey primaryKey = new LevelDBKey(PRIMARY, aProductOwner.getTenantId().id(), aProductOwner.getUsername());
        aUoW.remove(primaryKey);

        LevelDBKey teamMemberOfTenant = new LevelDBKey(primaryKey, PRODUCT_OWNER_OF_TENANT, aProductOwner.getTenantId().id());
        aUoW.removeKeyReference(teamMemberOfTenant);
    }

    private void save(ProductOwner aProductOwner, LevelDBUnitOfWork aUoW) {
        LevelDBKey primaryKey = new LevelDBKey(PRIMARY, aProductOwner.getTenantId().id(), aProductOwner.getUsername());
        aUoW.write(primaryKey, aProductOwner);

        LevelDBKey productOwnersOfTenant = new LevelDBKey(primaryKey, PRODUCT_OWNER_OF_TENANT, aProductOwner.getTenantId().id());
        aUoW.updateKeyReference(productOwnersOfTenant);
    }
}
