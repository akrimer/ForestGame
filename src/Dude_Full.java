import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Dude_Full extends Dude{

    public Dude_Full(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        super(id, position,actionPeriod, animationPeriod,resourceLimit ,images);
        this.kind = EntityKind.DUDE_FULL;

    }


    @Override
    public boolean moveTo(WorldModel world, Entity selectedEntity, EventScheduler scheduler) {
        if (this.position.adjacent(selectedEntity.position)) {
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
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Dude_Not_Full dude = Dude_Not_Full.createDudeNotFull(this.id, this.position, this.getActionPeriod(), this.getAnimationPeriod(), this.getResourceLimit(), this.images);

        world.removeEntity(scheduler, this);

        world.addEntity(dude);
        dude.scheduleAction(scheduler, world, imageStore);
        return true;
    }

        @Override
        public void ExecuteActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
            Optional<Entity> fullTarget = world.findNearest(this.position, new ArrayList<>(List.of(EntityKind.HOUSE)));

            if (fullTarget.isPresent() && this.moveTo(world, fullTarget.get(), scheduler)) {
                this.transform(world, scheduler, imageStore);
            } else {
                scheduler.scheduleEvent(this, Activity.createActivity(this, world, imageStore), this.getActionPeriod());
            }
        }

    public static Dude_Full createDudeFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new Dude_Full(id, position,actionPeriod,animationPeriod, resourceLimit,images);

    }


}


