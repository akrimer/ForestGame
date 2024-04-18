import processing.core.PImage;

//import javax.swing.Action;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Fairy extends ExcuteAction implements ActionPeriod, MoveEntity {

    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_ANIMATION_PERIOD = 0;
    public static final int FAIRY_ACTION_PERIOD = 1;
    public static final int FAIRY_NUM_PROPERTIES = 2;

    private double actionPeriod;
    public Fairy(String id, Point position, double actionPeriod,  double animationPeriod, List<PImage> images) {
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
/*
        this.kind = EntityKind.FAIRY;
*/
    }

    public double getActionPeriod(){
        return actionPeriod;
    }

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Activity.createActivity(this, world, imageStore), this.actionPeriod);
        scheduler.scheduleEvent(this, Animation.createAnimation(this, 0), getAnimationPeriod());
    }
    /*public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.position;
            }
        }

        return newPos;
    }*/

    public Point nextPosition(WorldModel world, Point destPos) {
        List<Point> newPos = new AStarPathingStrategy().computePath(this.position, destPos,
                (Point pos) -> !world.isOccupied(pos) && world.withinBounds(pos),
                (Point p1, Point p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (newPos.isEmpty()) {
            return this.position;
        }
        if (newPos.size() > 1) {
            // Start from the second element in the path
            Point nextStep = newPos.get(1);
            return nextStep;
        }
        this.position = newPos.get(0);
        return this.position;
    }
    @Override
    public boolean moveTo(WorldModel world, Entity selectedEntity, EventScheduler scheduler) {
        if (this.position.adjacent(selectedEntity.position)) {
            world.removeEntity(scheduler, selectedEntity);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, selectedEntity.position);

            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
    @Override
    public void ExecuteActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(this.position, new ArrayList<>(List.of(EntityKind.STUMP)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().position;

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {

                Sapling sapling = Sapling.createSapling(Sapling.SAPLING_KEY + "_" + fairyTarget.get().id, tgtPos, imageStore.getImageList(Sapling.SAPLING_KEY), 0);

                world.addEntity(sapling);
                sapling.scheduleAction(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, Activity.createActivity(this, world, imageStore), this.actionPeriod);
    }
    public static Fairy createFairy(String id, Point position, double animationPeriod,double actionPeriod, List<PImage> images) {
        return new Fairy(id, position, animationPeriod, actionPeriod, images);
    }

}
