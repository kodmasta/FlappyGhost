package com.example.flappyghost;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
/**
 * @author Casta Ung, Heng Wei
 */
public abstract class Entity {
    protected double x, y;
    protected double rayon;
    protected double vx, vy;
    protected Image skin;
    protected Color color;
    protected boolean scored;
    /**
     * Constructeur de la classe Entity
     * @param x position dans l'axe des x dans le canvas
     * @param y position dans l'axe des y dans le canvas
     * @param r rayon du cercle pour collision
     * @param c couleur de l'entite en mode debugging
     * @param skin l'image de l'entity
     */
    public Entity(double x, double y, double r, Color c, Image skin) {
        this.x = x;
        this.y = y;
        this.rayon = r;
        this.color = c;
        this.skin = skin;
        this.scored = false;
    }
    /**
     * Getter de position x
     * @return position en x
     */
    public double getX() {
        return x;
    }
    /**
     * Getter de position y
     * @return position en y
     */
    public double getY() {
        return y;
    }
    /**
     * Getter du diametre du cercle
     * @return diametre du cercle
     */
    public double getH() {
        return 2 * rayon;
    }
    /**
     * Verifie si il y a intersection
     * @param other l'obstacle en question
     * @return boolean si il y a eu une collision
     */
    public boolean intersects(Entity other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double d2 = dx * dx + dy * dy;

        return d2 < (this.rayon + other.rayon) * (this.rayon + other.rayon);
    }
    /**
     * Verifie si le ghost a passé un obstacle
     * @param other l'obstacle en question
     * @return boolean si il y a eu un dépassement
     */
    public boolean passObstacle(Entity other) {
        if (this.x - other.x > this.rayon & other.scored == false){
            return true;
        }else{
            return false;
        }
    }
    /**
     * Dessine les entities si mode debug oui pas
     * @param context le contexte
     * @param debugMode boolean si en mode debugging ou non
     */
    public void draw(GraphicsContext context,boolean debugMode) {
        context.setFill(this.color);
        if(debugMode){
            context.fillOval(this.x - this.rayon, this.y - this.rayon, 2 * this.rayon, 2 * this.rayon);
        }
        else {
            context.drawImage(skin, x - skin.getWidth() / 2, y - skin.getWidth() / 2);
        }
    }
    /**
     * Abstract method pour bouger les entity
     * @param dt
     */
    public abstract void move(double dt);
    /**
     * Fait sauter le ghost
     * @param v la vitesse du flap
     */
    void flap(double v) {
        this.vy = v;
    }
    /**
     * Fait bouger la vitesse du entity en axe x
     * @param newVx nouveau vx en x
     */
    public void setXSpeed(double newVx) {
        this.vx = newVx;
    }
    /**
     * Change la couleur en mode debugging
     * @param color la nouvelle couleur
     */
    public void setColor(Color color) {
        this.color = color;
    }
}