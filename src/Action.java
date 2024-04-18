/**
 * An action that can be taken by an entity
 */

public abstract class Action {
    public ActionKind kind;
    public Entity entity;

    public Action(Entity entity){
        this.entity = entity;
    }

    public abstract void executeAction(EventScheduler scheduler);


}
