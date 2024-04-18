import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DudeDogFull extends Dude {



    public DudeDogFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        super(id, position,actionPeriod, animationPeriod,resourceLimit ,images);
        this.kind = EntityKind.DudeDogFull;

    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.position)) {
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
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        DudeDogNotFull dude = DudeDogNotFull.createDudeDogNotFull(this.id, this.position, this.getActionPeriod(), this.getAnimationPeriod(), this.getResourceLimit(), this.images);

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

    public static DudeDogFull createDudeDogFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new DudeDogFull(id, position,actionPeriod,animationPeriod, resourceLimit,images);

    }


}
