package com.example.flappyghost;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
/**
 * @author Casta Ung, Heng Wei
 */
public class Obstacle extends Entity {
    /**
     * Constructeur de la classe Obstacle
     * @param x position dans l'axe des x dans le canvas
     * @param y position dans l'axe des y dans le canvas
     * @param r rayon du cercle pour collision
     * @param c couleur de l'entite en mode debugging
     * @param bgSpeed vitesse du background
     * @param skin l`image de l`obstacle
     */
    public Obstacle(double x, double y, double r, Color c, double bgSpeed, Image skin) {
        super(x, y, r, c, skin);
        this.vx = bgSpeed;
    }
    /**
     * Fait bouger l'obstacle
     * @param dt le delta time calculé par le controleur
     */
    public void move(double dt) {
        this.x += dt * vx;
    }
}
