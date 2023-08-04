package com.example.flappyghost;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
public class Obstacle extends Entity {
    public Obstacle(double x, double y, double r, Color c, double bgSpeed, Image skin) {
        super(x, y, r, c, skin);
        vx = bgSpeed;
    }
    public void move(double dt) {
        x += dt * vx;

    }
}
