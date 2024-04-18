public class Activity extends Action{

    private WorldModel world;
    private ImageStore imageStore;

    public Activity(Entity entity, WorldModel world, ImageStore imageStore) {
        super(entity);
        this.kind = ActionKind.ACTIVITY;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeActivityAction(EventScheduler scheduler) {
            if (entity instanceof ExcuteAction) {
                ((ExcuteAction) entity).ExecuteActivity(this.world, this.imageStore, scheduler);

}
    }

    public static Activity createActivity(Entity entity, WorldModel world, ImageStore imageStore) {
        return new Activity(entity, world, imageStore);
    }

    public void executeAction(EventScheduler scheduler){
        this.executeActivityAction(scheduler);
    }

}




