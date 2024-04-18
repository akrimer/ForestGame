import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeDogNotFull extends Dude  {


    public DudeDogNotFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        super(id, position, actionPeriod, animationPeriod,resourceLimit, images);
        this.kind = EntityKind.DudeDogNotFull;
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.position)) {
            int current_resourceCount = this.getResourceCount();
            current_resourceCount++;
            setResourceCont(current_resourceCount);
            ((Health) target).setHealth(((Health)target).getHealth() -1) ;
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
    public boolean transform (WorldModel world, EventScheduler scheduler, ImageStore imageStore){
        if (this.getResourceCount() >= this.getResourceLimit()) {
            DudeDogFull dude = DudeDogFull.createDudeDogFull(this.id, this.position, this.getActionPeriod(), this.getAnimationPeriod(), this.getResourceLimit(), this.images);

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

    public static DudeDogNotFull createDudeDogNotFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new DudeDogNotFull(id, position, actionPeriod, animationPeriod,resourceLimit, images);


    }
}
