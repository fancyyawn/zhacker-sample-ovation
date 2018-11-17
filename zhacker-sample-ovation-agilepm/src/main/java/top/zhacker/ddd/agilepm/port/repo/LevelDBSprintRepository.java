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
import top.zhacker.ddd.agilepm.domain.product.ProductId;
import top.zhacker.ddd.agilepm.domain.product.sprint.Sprint;
import top.zhacker.ddd.agilepm.domain.product.sprint.SprintId;
import top.zhacker.ddd.agilepm.domain.product.sprint.SprintRepository;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;
import top.zhacker.boot.leveldb.AbstractLevelDBRepository;
import top.zhacker.boot.leveldb.LevelDBKey;
import top.zhacker.boot.leveldb.LevelDBUnitOfWork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Repository
public class LevelDBSprintRepository
        extends AbstractLevelDBRepository
        implements SprintRepository {

    private static final String PRIMARY = "SPRINT#PK";
    private static final String PRODUCT_RELEASES = "SPRINT#PR";
    
    @Override
    public Collection<Sprint> allProductSprints(TenantId aTenantId, ProductId aProductId) {
        List<Sprint> sprints = new ArrayList<Sprint>();

        LevelDBKey productSprints = new LevelDBKey(PRODUCT_RELEASES, aTenantId.id(), aProductId.id());

        LevelDBUnitOfWork uow = LevelDBUnitOfWork.readOnly(this.database());

        List<Object> keys = uow.readKeys(productSprints);

        for (Object sprintId : keys) {
            Sprint sprint = uow.readObject(sprintId.toString().getBytes(), Sprint.class);

            if (sprint != null) {
                sprints.add(sprint);
            }
        }

        return sprints;
    }

    @Override
    public SprintId nextIdentity() {
        return new SprintId(UUID.randomUUID().toString().toUpperCase());
    }

    @Override
    public Sprint sprintOfId(TenantId aTenantId, SprintId aSprintId) {
        LevelDBKey primaryKey = new LevelDBKey(PRIMARY, aTenantId.id(), aSprintId.id());

        Sprint sprint =
                LevelDBUnitOfWork.readOnly(this.database())
                    .readObject(primaryKey.key().getBytes(), Sprint.class);

        return sprint;
    }

    @Override
    public void remove(Sprint aSprint) {
        LevelDBKey lockKey = new LevelDBKey(PRIMARY, aSprint.tenantId().id());

        LevelDBUnitOfWork uow = LevelDBUnitOfWork.current();

        uow.lock(lockKey.key());

        this.remove(aSprint, uow);
    }

    @Override
    public void removeAll(Collection<Sprint> aSprintCollection) {
        boolean locked = false;

        LevelDBUnitOfWork uow = LevelDBUnitOfWork.current();

        for (Sprint sprint : aSprintCollection) {
            if (!locked) {
                LevelDBKey lockKey = new LevelDBKey(PRIMARY, sprint.tenantId().id());

                uow.lock(lockKey.key());

                locked = true;
            }

            this.remove(sprint, uow);
        }
    }

    @Override
    public void save(Sprint aSprint) {
        LevelDBKey lockKey = new LevelDBKey(PRIMARY, aSprint.tenantId().id());

        LevelDBUnitOfWork uow = LevelDBUnitOfWork.current();

        uow.lock(lockKey.key());

        this.save(aSprint, uow);
    }

    @Override
    public void saveAll(Collection<Sprint> aSprintCollection) {
        boolean locked = false;

        LevelDBUnitOfWork uow = LevelDBUnitOfWork.current();

        for (Sprint sprint : aSprintCollection) {
            if (!locked) {
                LevelDBKey lockKey = new LevelDBKey(PRIMARY, sprint.tenantId().id());

                uow.lock(lockKey.key());

                locked = true;
            }

            this.save(sprint, uow);
        }
    }

    private void remove(Sprint aSprint, LevelDBUnitOfWork aUoW) {
        LevelDBKey primaryKey = new LevelDBKey(PRIMARY, aSprint.tenantId().id(), aSprint.sprintId().id());
        aUoW.remove(primaryKey);

        LevelDBKey productSprints = new LevelDBKey(primaryKey, PRODUCT_RELEASES, aSprint.tenantId().id(), aSprint.productId().id());
        aUoW.removeKeyReference(productSprints);
    }

    private void save(Sprint aSprint, LevelDBUnitOfWork aUoW) {
        LevelDBKey primaryKey = new LevelDBKey(PRIMARY, aSprint.tenantId().id(), aSprint.sprintId().id());
        aUoW.write(primaryKey, aSprint);

        LevelDBKey productSprints = new LevelDBKey(primaryKey, PRODUCT_RELEASES, aSprint.tenantId().id(), aSprint.productId().id());
        aUoW.updateKeyReference(productSprints);
    }
}
