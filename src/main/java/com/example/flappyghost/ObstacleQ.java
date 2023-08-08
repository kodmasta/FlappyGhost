package com.example.flappyghost;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;

public class ObstacleQ extends Entity {
    double teleportTicks;
    double timeAccumulator;
    public ObstacleQ(double x, double y, double r, Color c, double bgSpeed, Image skin) {
        super(x, y, r, c, skin);
        this.vx = bgSpeed;
        this.teleportTicks = 0.2;
        this.timeAccumulator = 0;
    }
    public void move(double dt) {
        //track every 0.2 seconds
        timeAccumulator += dt;
        this.x += (dt* vx);

        if (timeAccumulator-teleportTicks >0){
            Random rand = new Random();
            int newPos = rand.nextInt(31);
            boolean coeffNegatif = rand.nextBoolean();
            if (coeffNegatif){
                newPos = newPos * -1;
            }
            this.x += newPos;
            timeAccumulator = 0;
        }
    }
}
