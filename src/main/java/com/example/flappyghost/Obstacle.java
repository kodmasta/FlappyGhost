package com.example.flappyghost;
import javafx.scene.paint.Color;

public class Obstacle extends Entity {
    public Obstacle(double x, double y, double r, Color c, double bgSpeed) {
        super(x, y, r, c);
        vx = bgSpeed;
    }
    public void move(double dt) {
        x += dt * vx;

    }
}
