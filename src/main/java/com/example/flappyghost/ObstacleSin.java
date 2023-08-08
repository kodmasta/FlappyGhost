package com.example.flappyghost;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class ObstacleSin extends Entity {
    double timeSinceStart;
    double posDepart;
    public ObstacleSin(double x, double y, double r, Color c, double bgSpeed, Image skin) {
        super(x, y, r, c, skin);
        this.vx = bgSpeed;
        this.timeSinceStart = 0;
        this.posDepart = y;
    }
    public void move(double dt) {
        this.x += dt * vx;
        //get total duration for oscillation
        timeSinceStart += dt;
        //to control oscillation speed : touch argument in Math.sin
        double oscillations = Math.sin(10*timeSinceStart);
        //25 px amplitude
        this.y = posDepart + (oscillations * 25);
    }
}
