import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Weenie extends ExcuteAction implements ActionPeriod, Move {

    private double actionPeriod;
    public Weenie(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
        this.kind = EntityKind.FAIRY;
    }

    public double getActionPeriod(){
        return actionPeriod;
    }

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Activity.createActivity(this, world, imageStore), this.actionPeriod);
        scheduler.scheduleEvent(this, Animation.createAnimation(this, 0), getAnimationPeriod());

    }


    public Point nextPosition(WorldModel world, Point destPos) {
        List<Point> newPos = new AStarPathingStrategy().computePath(this.position, destPos,
                (Point pos) -> !world.isOccupied(pos) && world.withinBounds(pos),
                (Point p1, Point p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS);
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
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.position);

            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
    @Override
    public void ExecuteActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(this.position, new ArrayList<>(List.of(EntityKind.Banzo)));

        if (fairyTarget.isPresent()) {

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {
                world.removeEntity(scheduler, fairyTarget.get());
            }
        }

        scheduler.scheduleEvent(this, Activity.createActivity(this, world, imageStore), this.actionPeriod);
    }

    public static Weenie createWeenie(String id, Point position, double animationPeriod, double actionPeriod, List<PImage> images) {
        return new Weenie(id, position, animationPeriod, actionPeriod, images);
    }

}
