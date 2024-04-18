import processing.core.PImage;
import java.util.List;

public abstract class Dude extends ExcuteAction implements ActionPeriod, MoveEntity,resourceLimit, transform{

    private double actionPeriod;
    private int resourceCount = 0;
    private int resourceLimit;
    public static final String DUDE_KEY = "dude";
    public static final int DUDE_ACTION_PERIOD = 0;
    public static final int DUDE_ANIMATION_PERIOD = 1;
    public static final int DUDE_LIMIT = 2;
    public static final int DUDE_NUM_PROPERTIES = 3;

    public Dude(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
            super(id, position, images, animationPeriod);
            this.actionPeriod  = actionPeriod;
            this.resourceLimit = resourceLimit;
        }

    public double getActionPeriod() {
        return actionPeriod;
    }
    @Override
    public int getResourceLimit() {
        return resourceLimit;
    }

    public void setActionPeriod(double actionPeriod){
        this.actionPeriod = actionPeriod;
    }

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Activity.createActivity(this, world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, Animation.createAnimation(this, 0), this.getAnimationPeriod());
    }
    @Override
   /* public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).kind != EntityKind.STUMP) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).kind != EntityKind.STUMP) {
                newPos = this.position;
            }
        }

        return newPos;
    }*/
    public Point nextPosition(WorldModel world, Point destPos) {
        List<Point> newPos = new AStarPathingStrategy().computePath(
                this.position,
                destPos,
                (Point pos) -> (!world.isOccupied(pos) || world.getOccupancyCell(pos).getClass() == Stump.class) && world.withinBounds(pos),
                (Point p1, Point p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS
        );
        System.out.println("Computed Path: " + newPos);
        if (newPos.isEmpty()) {
            return this.position;
        }
        if (newPos.size() > 1) {
            // Start from the second element in the path
            Point nextStep = newPos.get(1);
            return nextStep;
        }

        // Update the current position based on the computed path
        this.position = newPos.get(0);
        return this.position;
    }
    public int getResourceCount(){
            return resourceCount;
    }
    public void setResourceCont(int resourceCount){
            this.resourceCount = resourceCount;
    }

}

