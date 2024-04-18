import processing.core.PImage;
import java.util.List;

public class Tree extends Health implements ActionPeriod, transform{


    public static final String TREE_KEY = "tree";
    public static final int TREE_ANIMATION_PERIOD = 0;
    public static final int TREE_ACTION_PERIOD = 1;
    public static final int TREE_HEALTH = 2;
    public static final int TREE_NUM_PROPERTIES = 3;
    public static final double TREE_ANIMATION_MAX = 0.600;
    public static final double TREE_ANIMATION_MIN = 0.050;
    public static final double TREE_ACTION_MAX = 1.400;
    public static final double TREE_ACTION_MIN = 1.000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;
    private double actionPeriod;



    public Tree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        super(id, position, images,animationPeriod, health);
       this.actionPeriod = actionPeriod;
       this.kind = EntityKind.TREE;
    }
    public static Tree createTree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        return new Tree(id, position, actionPeriod, animationPeriod, health,images);

    }

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Activity.createActivity(this, world, imageStore), this.actionPeriod);
        scheduler.scheduleEvent(this, Animation.createAnimation(this, 0), getAnimationPeriod());
    }

    public double getActionPeriod() {
        return actionPeriod;
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getHealth() <= 0) {
            Stump stump = Stump.createStump(Stump.STUMP_KEY + "_" + this.id, this.position, imageStore.getImageList(Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }
    @Override
    public void ExecuteActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        if (!this.transform(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this, Activity.createActivity(this, world, imageStore), this.actionPeriod);
        }
    }


}
