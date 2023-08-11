package com.example.flappyghost;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.Random;
/**
 * @author Casta Ung, Heng Wei
 */
public class ObstacleQ extends Entity {
    /**
     * Variables diverses
     */
    double teleportTicks;
    double timeAccumulator;
    /**
     * Constructeur de la classe ObstacleQ
     * @param x position dans l'axe des x dans le canvas
     * @param y position dans l'axe des y dans le canvas
     * @param r rayon du cercle pour collision
     * @param c couleur de l'entite en mode debugging
     * @param bgSpeed vitesse du background
     * @param skin l`image de l`obstacle
     */
    public ObstacleQ(double x, double y, double r, Color c, double bgSpeed, Image skin) {
        super(x, y, r, c, skin);
        this.vx = bgSpeed;
        this.teleportTicks = 0.2;
        this.timeAccumulator = 0;
    }
    /**
     * Fait bouger l'obstacleQ
     * @param dt le delta time calculé par le controleur
     */
    public void move(double dt) {
        //track every 0.2 seconds
        timeAccumulator += dt;
        this.x += (dt* vx);
        if (timeAccumulator-teleportTicks >0){
            Random rand = new Random();
            int newPos = rand.nextInt(31);
            boolean coeffNegatif = rand.nextBoolean();
            if (coeffNegatif){
                newPos = newPos * -1;
            }
            this.x += newPos;
            timeAccumulator = 0;
        }
    }
}
