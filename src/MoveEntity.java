public interface MoveEntity {
    Point nextPosition(WorldModel world, Point destPos);
    boolean moveTo(WorldModel world, Entity selectedEntity, EventScheduler scheduler);
}
