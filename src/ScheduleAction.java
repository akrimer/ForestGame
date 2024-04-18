import processing.core.PImage;

import java.util.List;

public abstract class ScheduleAction extends AnimationPeriod {
    public ScheduleAction(String id, Point position, List<PImage> images, double animationPeriod) {
        super(id, position, images, animationPeriod);
    }
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore){

    }
}
