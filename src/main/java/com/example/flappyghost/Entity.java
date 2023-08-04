package com.example.flappyghost;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public abstract class Entity {
    protected double x, y;
    protected double rayon;
    protected double vx, vy;
    protected Image skin;
    protected Color color;
    public Entity(double x, double y, double r, Color c, Image skin) {
        this.x = x;
        this.y = y;
        this.rayon = r;
        this.color = c;
        this.skin = skin;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getW() {
        return 2 * rayon;
    }

    public double getH() {
        return 2 * rayon;
    }
    public boolean intersects(Entity other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double d2 = dx * dx + dy * dy;

        return d2 < (this.rayon + other.rayon) * (this.rayon + other.rayon);
    }
    public void obstacleCollided(Entity other) {
        if (this.intersects(other))
            restartGame();
    }
    public void draw(GraphicsContext context,boolean debugMode) {
        context.setFill(this.color);

        if(debugMode)
            context.fillOval(this.x - this.rayon, this.y - this.rayon, 2 * this.rayon, 2 * this.rayon);
        else
            context.drawImage(skin, x-skin.getWidth()/2, y-skin.getWidth()/2);

    }
    public abstract void move(double dt);
    void flap(double v) {
        this.vy = v;
    }
    public void restartGame(){
        //to be coded
    };
}