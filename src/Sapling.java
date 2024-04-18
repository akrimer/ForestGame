import processing.core.PImage;

import java.util.List;

public class Sapling extends Health implements transform {


    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_HEALTH = 0;
    public static final int SAPLING_NUM_PROPERTIES = 1;
    public static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000;
    private final int health_limit;
    private static final int SAPLING_HEALTH_LIMIT = 5;

    private double actionPeriod;

    public Sapling(String id, Point position, List<PImage> images, int health) {
        super(id, position, images, SAPLING_ACTION_ANIMATION_PERIOD, health);
    this.actionPeriod =SAPLING_ACTION_ANIMATION_PERIOD;
    this.health_limit = SAPLING_HEALTH_LIMIT;
    }

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Activity.createActivity(this, world, imageStore), this.actionPeriod);
        scheduler.scheduleEvent(this, Animation.createAnimation(this, 0), this.getAnimationPeriod());
    }

@Override
public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
    if (this.getHealth() <= 0) {
       Stump stump = Stump.createStump(Stump.STUMP_KEY + "_" + this.id, this.position, imageStore.getImageList(Stump.STUMP_KEY));

        world.removeEntity(scheduler, this);

        world.addEntity(stump);

        return true;
    } else if (this.getHealth() >= this.health_limit) {
        Tree tree = Tree.createTree(Tree.TREE_KEY + "_" + this.id, this.position, Functions.getNumFromRange(Tree.TREE_ACTION_MAX, Tree.TREE_ACTION_MIN), Functions.getNumFromRange(Tree.TREE_ANIMATION_MAX, Tree.TREE_ANIMATION_MIN), Functions.getIntFromRange(Tree.TREE_HEALTH_MAX, Tree.TREE_HEALTH_MIN), imageStore.getImageList(Tree.TREE_KEY));

        world.removeEntity(scheduler, this);

        world.addEntity(tree);
        tree.scheduleAction(scheduler, world, imageStore);

        return true;
    }

    return false;
}
    @Override
    public void ExecuteActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        int currentHealth = getHealth();
        currentHealth++;
        setHealth(currentHealth);

        if (!this.transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, Activity.createActivity(this, world, imageStore), this.actionPeriod);

        }

    }
    public static Sapling createSapling(String id, Point position, List<PImage> images, int health) {
        return new Sapling(id, position, images, health);

    }
}
