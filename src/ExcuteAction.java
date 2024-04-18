import processing.core.PImage;

import java.util.List;

public abstract class ExcuteAction extends ScheduleAction {

    public ExcuteAction(String id, Point position, List<PImage> images,double animationPeriod) {
        super(id, position, images, animationPeriod);
    }

    public abstract void ExecuteActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);


}
