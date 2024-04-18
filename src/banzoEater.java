import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class banzoEater extends ExcuteAction implements ActionPeriod, Move{
    private double actionPeriod;



    public banzoEater(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
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
                (Point pos) -> (!world.isOccupied(pos) || world.getOccupancyCell(pos).getClass() == Banzo.class) && world.withinBounds(pos),
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

    public static banzoEater createBanzoEater(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new banzoEater(id, position,actionPeriod,animationPeriod,images);

    }

    public void ExecuteActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> entityToRemove = world.findNearest(this.position, new ArrayList<>(List.of(EntityKind.Banzo)));

        entityToRemove.ifPresent(banzo -> {
            moveTo(world, banzo, scheduler);

            if (this.position.adjacent(banzo.position)) {

                world.removeEntity(scheduler, this);
                Point banzoPosition = banzo.position;
                world.removeEntity(scheduler, banzo);


                DudeDogFull dude = DudeDogFull.createDudeDogFull("dudeDog", banzoPosition, .1, .1, 0, imageStore.getImageList("dudeDog"));
                world.addEntity(dude);
                dude.scheduleAction(scheduler, world, imageStore);

            }
        });

        scheduler.scheduleEvent(this, Activity.createActivity(this, world, imageStore), this.getActionPeriod());
    }
}
