import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Dude_Not_Full extends Dude  {

    public Dude_Not_Full(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        super(id, position, actionPeriod, animationPeriod,resourceLimit, images);
        this.kind = EntityKind.DUDE_NOT_FULL;


    }
    @Override
    public boolean moveTo(WorldModel world, Entity selectedEntity, EventScheduler scheduler) {
        if (this.position.adjacent(selectedEntity.position)) {
            int current_resourceCount = this.getResourceCount();
            current_resourceCount++;
            setResourceCont(current_resourceCount);

            ((Health) selectedEntity).setHealth(((Health) selectedEntity).getHealth() -1) ;
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
    public boolean transform (WorldModel world, EventScheduler scheduler, ImageStore imageStore){
        if (this.getResourceCount() >= this.getResourceLimit()) {
            Dude_Full dude = Dude_Full.createDudeFull(this.id, this.position, this.getActionPeriod(), this.getAnimationPeriod(), this.getResourceLimit(), this.images);

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            dude.scheduleAction(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    @Override
    public void ExecuteActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target = world.findNearest(this.position, new ArrayList<>(Arrays.asList(EntityKind.TREE, EntityKind.SAPLING)));

        if (target.isEmpty() || !this.moveTo(world, target.get(), scheduler) || !this.transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, Activity.createActivity(this, world, imageStore), this.getActionPeriod());
        }
    }

    public static Dude_Not_Full createDudeNotFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new Dude_Not_Full(id, position, actionPeriod, animationPeriod,resourceLimit, images);


    }
}

