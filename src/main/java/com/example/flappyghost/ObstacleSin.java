package com.example.flappyghost;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
/**
 * @author Casta Ung, Heng Wei
 */
public class ObstacleSin extends Entity {
    /**
     * Variables diverses
     */
    double timeSinceStart;
    double posDepart;
    /**
     * Constructeur de la classe ObstacleSin
     * @param x position dans l'axe des x dans le canvas
     * @param y position dans l'axe des y dans le canvas
     * @param r rayon du cercle pour collision
     * @param c couleur de l'entite en mode debugging
     * @param bgSpeed vitesse du background
     * @param skin l`image de l`obstacle
     */
    public ObstacleSin(double x, double y, double r, Color c, double bgSpeed, Image skin) {
        super(x, y, r, c, skin);
        this.vx = bgSpeed;
        this.timeSinceStart = 0;
        this.posDepart = y;
    }
    /**
     * Fait bouger l'obstacle
     * @param dt le delta time calculé par le controleur
     */
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
