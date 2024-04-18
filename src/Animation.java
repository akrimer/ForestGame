public class Animation extends Action{

    private int repeatCount;
    public Animation(Entity entity, int repeatCount) {
        super(entity);
        this.kind = ActionKind.ANIMATION;
        this.repeatCount = repeatCount;
    }

    public static Animation createAnimation(Entity entity, int repeatCount) {
        return new Animation(entity,repeatCount);
    }

    public void executeAnimation(EventScheduler scheduler) {
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            if (entity instanceof AnimationPeriod) {
                scheduler.scheduleEvent(this.entity, createAnimation(this.entity, Math.max(this.repeatCount - 1, 0)), ((AnimationPeriod) this.entity).getAnimationPeriod());
            }
        }
    }

    public void executeAction(EventScheduler scheduler){
        this.executeAnimation(scheduler);
    }
}
/*

public class Animation extends Action {
    private int repeatCount;

    public Animation(Entity entity, int repeatCount) {
        super(entity);
        this.repeatCount = repeatCount;
    }

    public static Animation createAnimation(Entity entity, int repeatCount) {
        return new Animation(entity, repeatCount);
    }

    public void executeAnimation(EventScheduler scheduler) {
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            if (entity instanceof AnimationPeriod) {
                scheduler.scheduleEvent(this.entity, createAnimation(this.entity, Math.max(this.repeatCount - 1, 0)), ((AnimationPeriod) this.entity).getAnimationPeriod());
            }
        }
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        this.executeAnimation(scheduler);
    }
}

*/
