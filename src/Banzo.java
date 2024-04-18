import processing.core.PImage;

import java.util.List;

public class Banzo extends Entity {
    public Banzo(String id, Point position, List<PImage> images) {
        super(id, position, images);
        this.kind = EntityKind.Banzo;
    }

    public static Banzo createBanzo(String id, Point position, List<PImage> images) {
        return new Banzo(id, position,images);


    }
}