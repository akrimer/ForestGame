import processing.core.PImage;

import java.util.List;


public class AnimationPeriod extends Entity {

    private double animationPeriod;
    public AnimationPeriod(String id, Point position, List<PImage> images, double animationPeriod) {
        super(id, position, images);
        this.animationPeriod = animationPeriod;
    }
    public double getAnimationPeriod() {
        return animationPeriod;
    }
    public void setAnimationPeriod(double animationPeriod) {
       this.animationPeriod = animationPeriod;
   }
}
