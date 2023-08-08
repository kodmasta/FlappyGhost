package com.example.flappyghost;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Ghost extends Entity {
    protected double ay;
    int score;
    public Ghost(double x, double y, Image skin) {
        super(x, y, 30 , Color.BLACK, skin);
        ay = 500;
        this.score = 0;
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
    public void setAy(double newAy){
        this.ay += newAy;
    }
    public int getScore(){
        return this.score;
    }
    public void setScore(int score){
        this.score = this.score + score;
    }
}
