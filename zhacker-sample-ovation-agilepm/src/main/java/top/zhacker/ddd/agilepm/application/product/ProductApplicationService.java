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

package top.zhacker.ddd.agilepm.application.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zhacker.boot.event.process.ProcessId;
import top.zhacker.boot.event.process.TimeConstrainedProcessTracker;
import top.zhacker.boot.event.process.TimeConstrainedProcessTrackerRepository;
import top.zhacker.ddd.agilepm.application.product.command.*;
import top.zhacker.ddd.agilepm.domain.discussion.DiscussionAvailability;
import top.zhacker.ddd.agilepm.domain.discussion.DiscussionDescriptor;
import top.zhacker.ddd.agilepm.domain.product.Product;
import top.zhacker.ddd.agilepm.domain.product.ProductId;
import top.zhacker.ddd.agilepm.domain.product.ProductRepository;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItem;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItemId;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItemRepository;
import top.zhacker.ddd.agilepm.domain.product.discussion.ProductDiscussionRequestTimedOut;
import top.zhacker.ddd.agilepm.domain.product.release.Release;
import top.zhacker.ddd.agilepm.domain.product.release.ReleaseRepository;
import top.zhacker.ddd.agilepm.domain.product.sprint.Sprint;
import top.zhacker.ddd.agilepm.domain.product.sprint.SprintRepository;
import top.zhacker.ddd.agilepm.domain.team.member.ProductOwner;
import top.zhacker.ddd.agilepm.domain.team.member.ProductOwnerRepository;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;
import top.zhacker.boot.leveldb.LevelDbTransactional;

import java.util.Date;


@Service
public class ProductApplicationService {

    private TimeConstrainedProcessTrackerRepository processTrackerRepository;
    private ProductOwnerRepository productOwnerRepository;
    private ProductRepository productRepository;
    private SprintRepository sprintRepository;
    private ReleaseRepository releaseRepository;
    private BacklogItemRepository backlogItemRepository;

    @Autowired
    public ProductApplicationService(ProductRepository aProductRepository, ProductOwnerRepository aProductOwnerRepository, TimeConstrainedProcessTrackerRepository aProcessTrackerRepository, SprintRepository sprintRepository, ReleaseRepository releaseRepository, BacklogItemRepository backlogItemRepository) {

        super();

        this.processTrackerRepository = aProcessTrackerRepository;
        this.productOwnerRepository = aProductOwnerRepository;
        this.productRepository = aProductRepository;
        this.sprintRepository = sprintRepository;
        this.releaseRepository = releaseRepository;
        this.backlogItemRepository = backlogItemRepository;
    }

    // TODO: additional APIs / student assignment

    @LevelDbTransactional
    public void initiateDiscussion(InitiateDiscussionCommand aCommand) {

        Product product =
                this.productRepository()
                    .productOfId(
                            new TenantId(aCommand.getTenantId()),
                            new ProductId(aCommand.getProductId()));

        if (product == null) {
            throw new IllegalStateException(
                    "Unknown product of tenant id: "
                    + aCommand.getTenantId()
                    + " and product id: "
                    + aCommand.getProductId());
        }

        product.initiateDiscussion(new DiscussionDescriptor(aCommand.getDiscussionId()));

        this.productRepository().save(product);

        ProcessId processId = ProcessId.existingProcessId(product.discussionInitiationId());

        TimeConstrainedProcessTracker tracker =
                this.processTrackerRepository()
                    .trackerOfProcessId(aCommand.getTenantId(), processId);

        tracker.completed();

        this.processTrackerRepository().save(tracker);
        
    }

    @LevelDbTransactional
    public String newProduct(NewProductCommand aCommand) {

        return this.newProductWith(
                aCommand.getTenantId(),
                aCommand.getProductOwnerId(),
                aCommand.getName(),
                aCommand.getDescription(),
                DiscussionAvailability.NOT_REQUESTED);
    }

    @LevelDbTransactional
    public String newProductWithDiscussion(NewProductCommand aCommand) {

        return this.newProductWith(
                aCommand.getTenantId(),
                aCommand.getProductOwnerId(),
                aCommand.getName(),
                aCommand.getDescription(),
                this.requestDiscussionIfAvailable());
    }

    @LevelDbTransactional
    public void requestProductDiscussion(RequestProductDiscussionCommand aCommand) {
        Product product =
                this.productRepository()
                    .productOfId(
                            new TenantId(aCommand.getTenantId()),
                            new ProductId(aCommand.getProductId()));

        if (product == null) {
            throw new IllegalStateException(
                    "Unknown product of tenant id: "
                    + aCommand.getTenantId()
                    + " and product id: "
                    + aCommand.getProductId());
        }

        this.requestProductDiscussionFor(product);
    }

    @LevelDbTransactional
    public void retryProductDiscussionRequest(RetryProductDiscussionRequestCommand aCommand) {

        ProcessId processId = ProcessId.existingProcessId(aCommand.getProcessId());

        TenantId tenantId = new TenantId(aCommand.getTenantId());

        Product product =
                this.productRepository()
                    .productOfDiscussionInitiationId(
                            tenantId,
                            processId.id());

        if (product == null) {
            throw new IllegalStateException(
                    "Unknown product of tenant id: "
                    + aCommand.getTenantId()
                    + " and discussion initiation id: "
                    + processId.id());
        }

        this.requestProductDiscussionFor(product);
    }

    @LevelDbTransactional
    public void startDiscussionInitiation(StartDiscussionInitiationCommand aCommand) {
        
            Product product =
                    this.productRepository()
                        .productOfId(
                                new TenantId(aCommand.getTenantId()),
                                new ProductId(aCommand.getProductId()));

            if (product == null) {
                throw new IllegalStateException(
                        "Unknown product of tenant id: "
                        + aCommand.getTenantId()
                        + " and product id: "
                        + aCommand.getProductId());
            }

            String timedOutEventName =
                    ProductDiscussionRequestTimedOut.class.getName();

            TimeConstrainedProcessTracker tracker =
                    new TimeConstrainedProcessTracker(
                            product.tenantId().id(),
                            ProcessId.newProcessId(),
                            "Create discussion for product: "
                                + product.name(),
                            new Date(),
                            5L * 60L * 1000L, // retries every 5 minutes
                            3, // 3 total retries
                            timedOutEventName);

            this.processTrackerRepository().save(tracker);

            product.startDiscussionInitiation(tracker.processId().id());

            this.productRepository().save(product);
    }

    @LevelDbTransactional
    public void timeOutProductDiscussionRequest(TimeOutProductDiscussionRequestCommand aCommand) {
        
            ProcessId processId = ProcessId.existingProcessId(aCommand.getProcessId());

            TenantId tenantId = new TenantId(aCommand.getTenantId());

            Product product =
                    this.productRepository()
                        .productOfDiscussionInitiationId(
                                tenantId,
                                processId.id());

            this.sendEmailForTimedOutProcess(product);

            product.failDiscussionInitiation();

            this.productRepository().save(product);
            
    }
    
    @LevelDbTransactional
    public String scheduleSprint(ScheduleSprintCommand command){
        
        TenantId tenantId = new TenantId(command.getTenantId());
        
        Product product = this.productRepository.productOfId(
            tenantId, new ProductId(command.getProductId())
        );
        
        Sprint sprint = product.scheduleSprint(
            sprintRepository.nextIdentity(),
            command.getName(),
            command.getGoals(),
            command.getBegins(),
            command.getEnds()
        );
        
        sprintRepository.save(sprint);
        
        return sprint.sprintId().id();
    }
    
    @LevelDbTransactional
    public String scheduleRelease(ScheduleReleaseCommand command){
        
        TenantId tenantId = new TenantId(command.getTenantId());
        
        Product product = this.productRepository.productOfId(
            tenantId, new ProductId(command.getProductId())
        );
        
        Release release = product.scheduleRelease(
            releaseRepository.nextIdentity(),
            command.getName(),
            command.getDescription(),
            command.getBegins(),
            command.getEnds()
        );
        
        releaseRepository.save(release);
        
        return release.releaseId().id();
    }
    
    
    @LevelDbTransactional
    public String planBacklogItem(PlanBacklogItemCommand command){
        TenantId tenantId = new TenantId(command.getTenantId());
    
        Product product = this.productRepository.productOfId(
            tenantId, new ProductId(command.getProductId())
        );
        
        BacklogItem backlogItem = product.planBacklogItem(
            backlogItemRepository.nextIdentity(),
            command.getSummary(),
            command.getCategory(),
            command.getType(),
            command.getStoryPoints()
        );
        
        backlogItemRepository.save(backlogItem);
        
        return backlogItem.backlogItemId().id();
    }
    
    @LevelDbTransactional
    public void plannedProductBacklogItem(
        String tenantId,
        String productId,
        String backlogItemId
    ) {
        Product product = this.productRepository.productOfId(
            new TenantId(tenantId), new ProductId(productId)
        );
        
        BacklogItem backlogItem = backlogItemRepository.backlogItemOfId(new TenantId(tenantId), new BacklogItemId(backlogItemId));
        
        product.plannedProductBacklogItem(backlogItem);
        
        productRepository.save(product);
    }
    
    
    private void sendEmailForTimedOutProcess(Product aProduct) {

        // TODO: Implement

    }

    private String newProductWith(
            String aTenantId,
            String aProductOwnerId,
            String aName,
            String aDescription,
            DiscussionAvailability aDiscussionAvailability) {

        TenantId tenantId = new TenantId(aTenantId);
        ProductId productId = null;
        
            productId = this.productRepository().nextIdentity();

            ProductOwner productOwner =
                    this.productOwnerRepository()
                        .productOwnerOfIdentity(
                                tenantId,
                                aProductOwnerId);

            Product product =
                    new Product(
                            tenantId,
                            productId,
                            productOwner.getProductOwnerId(),
                            aName,
                            aDescription,
                            aDiscussionAvailability);

            this.productRepository().save(product);
            
        return productId.getId();
    }

    private DiscussionAvailability requestDiscussionIfAvailable() {
        DiscussionAvailability availability = DiscussionAvailability.ADD_ON_NOT_ENABLED;

        boolean enabled = true; // TODO: determine add-on enabled

        if (enabled) {
            availability = DiscussionAvailability.REQUESTED;
        }

        return availability;
    }

    private TimeConstrainedProcessTrackerRepository processTrackerRepository() {
        return this.processTrackerRepository;
    }

    private ProductOwnerRepository productOwnerRepository() {
        return this.productOwnerRepository;
    }

    private ProductRepository productRepository() {
        return this.productRepository;
    }
    
    private void requestProductDiscussionFor(Product aProduct) {

            aProduct.requestDiscussion(this.requestDiscussionIfAvailable());

            this.productRepository().save(aProduct);
            
    }
}
