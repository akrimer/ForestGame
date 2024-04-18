import processing.core.PImage;
import java.util.List;

public class Obstacle extends ScheduleAction {
    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_ANIMATION_PERIOD = 0;
    public static final int OBSTACLE_NUM_PROPERTIES = 1;

    private double animationPeriod;

    public Obstacle(String id, Point position, double animationPeriod, List<PImage> images) {
        super(id, position, images, animationPeriod);
        this.animationPeriod = animationPeriod;
        this.kind = EntityKind.OBSTACLE;

    }
    public static Obstacle createObstacle(String id, Point position, double animationPeriod, List<PImage> images) {
        return new Obstacle(id, position, animationPeriod, images);
    }


    @Override
    public double getAnimationPeriod() {
        return animationPeriod;
    }



    public void setAnimationPeriod(double animationPeriod) {
        this.animationPeriod = animationPeriod;
    }


    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Animation.createAnimation(this, 0), getAnimationPeriod());
    }


}
