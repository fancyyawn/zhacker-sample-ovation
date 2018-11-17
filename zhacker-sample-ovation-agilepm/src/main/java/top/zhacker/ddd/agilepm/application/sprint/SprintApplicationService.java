package top.zhacker.ddd.agilepm.application.sprint;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zhacker.ddd.agilepm.application.sprint.command.CommitBacklogItemToSprintCommand;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItem;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItemId;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItemRepository;
import top.zhacker.ddd.agilepm.domain.product.sprint.Sprint;
import top.zhacker.ddd.agilepm.domain.product.sprint.SprintId;
import top.zhacker.ddd.agilepm.domain.product.sprint.SprintRepository;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;
import top.zhacker.boot.leveldb.LevelDbTransactional;


@Service
public class SprintApplicationService {

    private BacklogItemRepository backlogItemRepository;
    private SprintRepository sprintRepository;

    @Autowired
    public SprintApplicationService(
            SprintRepository aSprintRepository,
            BacklogItemRepository aBacklogItemRepository) {

        super();

        this.backlogItemRepository = aBacklogItemRepository;
        this.sprintRepository = aSprintRepository;
    }

    @LevelDbTransactional
    public void commitBacklogItemToSprint(
            CommitBacklogItemToSprintCommand aCommand) {

        TenantId tenantId = new TenantId(aCommand.getTenantId());

        Sprint sprint =
                this.sprintRepository()
                    .sprintOfId(
                            tenantId,
                            new SprintId(aCommand.getSprintId()));

        BacklogItem backlogItem =
                this.backlogItemRepository()
                    .backlogItemOfId(
                            tenantId,
                            new BacklogItemId(aCommand.getBacklogItemId()));

        sprint.commit(backlogItem);

        this.sprintRepository().save(sprint);
    }

    private BacklogItemRepository backlogItemRepository() {
        return this.backlogItemRepository;
    }

    private SprintRepository sprintRepository() {
        return this.sprintRepository;
    }
}
