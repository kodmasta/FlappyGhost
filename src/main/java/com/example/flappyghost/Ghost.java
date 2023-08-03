package com.example.flappyghost;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ghost extends Entity {
    protected double ay;
    public Ghost(double x, double y) {
        super(x, y, 30 , Color.BLACK);
        ay = 500;
    }
    public void move(double dt) {
        vy += dt * ay;
        y += dt * vy;
        if (y + getH() / 2 > VueGUI.HEIGHT-25 || y - getH() / 2 < 0) {
            vy *= -0.9;
        }
        y = Math.min(y, VueGUI.HEIGHT-25 - getH() / 2);
        y = Math.max(y, getH() / 2);
    }
}
