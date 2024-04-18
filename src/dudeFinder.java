import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class dudeFinder extends ExcuteAction implements ActionPeriod, Move{
    private double actionPeriod;



    public dudeFinder(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        super(id, position, images, animationPeriod);
        this.actionPeriod  = actionPeriod;
    }

    public double getActionPeriod() {
        return actionPeriod;
    }


    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Activity.createActivity(this, world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, Animation.createAnimation(this, 0), this.getAnimationPeriod());
    }
    AStarPathingStrategy pathingStrategy = new AStarPathingStrategy();

    public Point nextPosition(WorldModel world, Point destPos) {
        List<Point> newPos = pathingStrategy.computePath(
                this.position,
                destPos,
                (Point pos) -> (!world.isOccupied(pos) || world.getOccupancyCell(pos).getClass() == Dude.class) && world.withinBounds(pos),
                (Point p1, Point p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS
        );
        if (newPos.isEmpty()) {
            return this.position;
        }
        if (newPos.size() > 1) {
            Point nextStep = newPos.get(1);
            return nextStep;
        }

        this.position = newPos.get(0);
        return this.position;
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.position)) {
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.position);

            if (!this.position.equals(nextPos)) {
                long moveDelay = 1000;
                scheduler.scheduleEvent(this, Activity.createActivity(this, world, null), moveDelay);
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    public static dudeFinder createDudeFinder(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new dudeFinder(id, position,actionPeriod,animationPeriod,images);

    }

    @Override
    public void ExecuteActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> entityToTransform = world.findNearest(this.position, new ArrayList<>(List.of(EntityKind.DUDE_FULL,EntityKind.DUDE_NOT_FULL)));

        entityToTransform.ifPresent(dude -> {
            moveTo(world, dude, scheduler);

            if (this.position.adjacent(dude.position)) {
                Point dudePos = dude.position;
                world.removeEntity(scheduler, dude);

                banzoEater banzoeater = banzoEater.createBanzoEater("banzoEater", dudePos, .5, .1, imageStore.getImageList("banzoEater"));
                world.addEntity(banzoeater);
                banzoeater.scheduleAction(scheduler, world, imageStore);
            }
        });

        scheduler.scheduleEvent(this, Activity.createActivity(this, world, imageStore), this.getActionPeriod());
    }
}