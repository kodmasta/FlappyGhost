package com.example.flappyghost;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
/**
 * @author Casta Ung, Heng Wei
 */
public class Ghost extends Entity {
    protected double ay;
    protected int score;
    protected double height;
    /**
     * Constructeur de la classe Ghost
     * @param x position dans l'axe des x dans le canvas
     * @param y position dans l'axe des y dans le canvas
     * @param skin l'image du ghost
     * @param height la grandeur du canvas
     */
    public Ghost(double x, double y, Image skin, double height) {
        super(x, y, 30 , Color.BLACK, skin);
        this.ay = 500;
        this.score = 0;
        this.height = height;
    }
    /**
     * Fait bouger le ghost
     * @param dt le delta time calculer par le controleur
     */
    public void move(double dt) {
        vy += dt * ay;
        y += dt * vy;
        if (y + getH() / 2 > this.height-25 || y - getH() / 2 < 0) {
            vy *= -0.9;
        }
        y = Math.min(y, this.height-25 - getH() / 2);
        y = Math.max(y, getH() / 2);
    }
    /**
     * Setter de l'acceleration
     * @param newAy le nouvel acceleration
     */
    public void setAy(double newAy){
        this.ay += newAy;
    }
    /**
     * Getter du score actuel du ghost
     * @return la variable du score
     */
    public int getScore(){
        return this.score;
    }
    /**
     * S'occupe d'ajouter le score
     * @param score l'incrémentation du score
     */
    public void addScore(int score){
        this.score = this.score + score;
    }
}
