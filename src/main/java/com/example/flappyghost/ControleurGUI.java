package com.example.flappyghost;
import javafx.animation.AnimationTimer;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.*;
/**
 * @author Casta Ung, Heng Wei
 */
public class ControleurGUI {
    /**
     * Variables diverses
     */
    private static double flapStrength;
    private long lastTime;
    private double timeToSpawn;
    private boolean accelerationBool;
    private double deltaTime;
    private boolean debugMode;
    private long pauseStart;
    private long pauseEnd;
    private boolean unpause;
    /**
     * Vitesse du défilement du background
     */
    public static double bgSpeed = 120;
    /**
     * La vue est le point d'entrée de l'Application JavaFX
     */
    private VueGUI vue;
    /**
     * Le modèle de l'application
     */
    private Ghost ghost;
    private LinkedList<Entity> entities = new LinkedList<Entity>();
    /**
     * Constructeur de la classe ControleurGUI
     * @param vue instanciation de la classe s'occupant de la vue de l'application
     */
    ControleurGUI(VueGUI vue) {
        this.vue = vue;
        this.ghost = new Ghost(vue.getWidth()/2, vue.getHeight()/2, new Image("file:fichiersFH/ghost.png"), vue.getHeight());
        /**
         * Ghost est toujours la première entité dans le linkedList entities
         */
        this.entities.add(ghost);
        this.flapStrength = -300;
        this.lastTime = 0;
        this.timeToSpawn = 0;
        this.accelerationBool = false;
        this.deltaTime = 0;
        this.pauseStart = 0;
        this.pauseEnd = 0;
        this.unpause = false;
        this.debugMode = false;
    }
    /**
     * Getter du ghost
     * @return variable contenant le ghost
     */
    public Ghost getGhost(){
        return ghost;
    }
    /**
     * Getter d'un nouveau ghost
     * @return variable contenant un nouveau ghost
     */
    public Ghost getNewGhost(){return new Ghost(vue.getWidth()/2, vue.getHeight()/2, new Image("file:fichiersFH/ghost.png"),vue.getHeight());}
    /**
     * Getter du LinkedList contenant les entities
     * @return variable contenant les entitiesZ
     */
    public LinkedList<Entity> getEntities(){
        return entities;
    }
    /**
     * Saut du ghost
     */
    public void flap(){ghost.flap(flapStrength);}
    /**
     * Getter de pauseStart
     * @return variable contenant le pauseStart
     */
    public double getPauseStart(){return this.pauseStart;}
    /**
     * Setter de pauseStart
     * @param time le temps actuel en nanosecondes
     */
    public void setPauseStart(long time){this.pauseStart = time;}
    /**
     * Getter de pauseEnd
     * @return variable contenant le pauseEnd
     */
    public double getPauseEnd(){return this.pauseEnd;}
    /**
     * Setter de pauseEnd
     * @param time le temps actuel en nanosecondes
     */
    public void setPauseEnd(long time){this.pauseEnd = time;}
    /**
     * Getter de unPause
     * @return variable contenant le unPause
     */
    public boolean getUnpause(){return this.unpause;}

    /**
     * Setter de unPause
     * @param pause boolean si on est en pause ou non
     */
    public void setUnpause(boolean pause){this.unpause = pause;}
    /**
     * Getter de bgSpeed
     * @return variable contenant le bgSpeed
     */
    public double getBgSpeed(){return this.bgSpeed;}

    /**
     * Setter de bgSpeed
     * @param newBgSpeed nouveau background speed
     */
    public void setBgSpeed(double newBgSpeed){ this.bgSpeed = newBgSpeed;}
    /**
     * Getter de lastTime
     * @return variable contenant le lastTime
     */
    public double getLastTime(){return this.lastTime;}
    /**
     * Calcule le dernier temps de la pause
     */
    public void calculeLastTime(){this.lastTime += (this.pauseEnd-this.pauseStart);}
    /**
     * Setter de lastTime
     * @param now le temps actuel en nanosecondes
     */
    public void setLastTime(long now){this.lastTime = now;}
    /**
     * Getter de timeToSpawn
     * @return variable contenant le timeToSpawn
     */
    public double getTimeToSpawn(){return this.timeToSpawn;}
    /**
     * Setter de timeToSpawn
     * @param num le temps actuel en nanosecondes
     */
    public void setTimeToSpawn(long num){this.timeToSpawn = num;}
    /**
     * Incrementation du temps pour le spawn
     */
    public void incrementTimeToSpawn(){this.timeToSpawn = this.timeToSpawn + this.deltaTime;}
    /**
     * Getter de accelerationBool
     * @return variable contenant le accelerationBool
     */
    public boolean getAccelerationBool(){return this.accelerationBool;}
    /**
     * Setter de accelerationBool
     * @param acceleration la nouvelle acceleration
     */
    public void setAccelerationBool(boolean acceleration){this.accelerationBool = acceleration;}
    /**
     * Getter de deltaTime
     * @return variable contenant le deltaTime
     */
    public double getDeltaTime(){return this.deltaTime;}
    /**
     * Calcule le nouveau delta time
     * @param now le temps actuel en nanosecondes
     */
    public void calculeDeltaTime(long now){this.deltaTime = (now - this.lastTime) * 1e-9;}
    /**
     * Getter de debugMode
     * @return variable contenant le debugMode
     */
    public boolean getdebugMode(){return debugMode;}
    /**
     * Setter de debugMode
     * @param mode le bool de debug
     */
    public void setdebugMode(boolean mode){this.debugMode = mode;}
    /**
     * Bouger les entities
     * @param e ghost ou obstacles
     */
    public void moveEntities(Entity e){e.move(this.deltaTime);}
    /**
     * Recommence une partie
     * @param parTrans variable contenant le défilement du background
     */
    public void restartGame(ParallelTransition parTrans){
        System.out.println("Le jeu a été redémarré");
        vue.getScoreCounter().setText("Score: 0");
        setBgSpeed(120);
        parTrans.setRate(getBgSpeed());
        getEntities().clear();
        ghost = getNewGhost();
        entities.add(ghost); //ghost est tjrs le premier entity dans le linkedList entities
    }
    /**
     * Accélère le jeu
     * @param parTrans variable contenant le défilement du background
     */
    public void accelerateGame(ParallelTransition parTrans){
        System.out.println("Le jeu a été accéléré");
        setBgSpeed(getBgSpeed() + 15);
        parTrans.setRate(getBgSpeed());
        getGhost().setAy(15);
    }
    /**
     * Pause le jeu
     * @param start boolean contenant lorsque la pause a commencé
     */
    public void pausedTime(boolean start){
        if(start)
            setPauseStart(System.nanoTime());
        else {
            setPauseEnd(System.nanoTime());
            setUnpause(true);
        }
    }
    /**
     * S'occupe de l`événement du boutton pause
     */
    public void pauseButton(){
        if (vue.getPauseButton().getText().equals("Pause")){
            vue.getPauseButton().setText("Resume");
            vue.getParTrans().pause();
            timer.stop();
            pausedTime(true);
            Platform.runLater(()->{
                vue.getCanvas().requestFocus();
            });
        }
        else {
            vue.getPauseButton().setText("Pause");
            vue.getParTrans().play();
            pausedTime(false);
            timer.start();
            Platform.runLater(()->{
                vue.getCanvas().requestFocus();
            });
        }
    }
    /**
     * S'occupe de l`événement du checkbox debug
     */
    public void debugCheckBox() {
        if (vue.getDebugCheckBox().isSelected()) {
            setdebugMode(true);
            Platform.runLater(() -> {
                vue.getCanvas().requestFocus();
            });
        } else {
            setdebugMode(false);
            Platform.runLater(() -> {
                vue.getCanvas().requestFocus();
            });
        }
    }
    /**
     * S'occupe s'il y a une collision
     * @param obstacle l'obstacle en collisioh avec le ghost
     */
    public void handleIntersects(int obstacle){
        if (getGhost().intersects(getEntities().get(obstacle))){
            getEntities().get(obstacle).setColor(Color.RED);
            if (!debugMode){
                restartGame(vue.getParTrans());
            }
        }
    }
    /**
     * S'occupe du score du joueur
     * @param obstacle l'obstacle dépassé par le ghost
     */
    public void handleScoring(int obstacle){
        getEntities().get(obstacle).scored = true;
        getGhost().addScore(5);
        vue.getScoreCounter().setText("Score: "+getGhost().getScore());
        if(getGhost().getScore() % 10 == 0){
            setAccelerationBool(true);
        }
    }
    /**
     * S'occupe du spawn des obstacles de maniere aleatoire
     */
    public void handleSpawn(){
        Random randNumber = new Random();
        int obstacleMoveType = randNumber.nextInt(3);
        double obstacleHeight = randNumber.nextDouble() * 340;
        int obstacleStyle = randNumber.nextInt(27);
        int obstacleRayon = randNumber.nextInt(36) + 10;
        switch (obstacleMoveType) {
            case 0 :
                Obstacle obstacle = new Obstacle(680,obstacleHeight + 40, 10, Color.YELLOW, -getBgSpeed(), new Image( String.format("file:fichiersFH/obstacles/%s.png", obstacleStyle)));
                getEntities().add(obstacle);
                break;
            case 1 :
                ObstacleSin obstacleSin = new ObstacleSin(680,obstacleHeight + obstacleRayon, 30, Color.YELLOW, -getBgSpeed(), new Image( String.format("file:fichiersFH/obstacles/%s.png", obstacleStyle)));
                getEntities().add(obstacleSin);
                break;
            case 2 :
                ObstacleQ obstacleQ = new ObstacleQ(680,obstacleHeight + 40, obstacleRayon, Color.YELLOW, -getBgSpeed(), new Image( String.format("file:fichiersFH/obstacles/%s.png", obstacleStyle)));
                getEntities().add(obstacleQ);
                break;
        }
        setTimeToSpawn(0);
    }
    /**
     * Commence l'animation
     */
    AnimationTimer timer = new AnimationTimer() {
        /**
         * Override de la méthode handle
         */
        @Override
        public void handle(long now) {
            if (getLastTime() == 0) {
                setLastTime(now);
                return;
            }
            if(getUnpause()){
                setUnpause(false);
                calculeLastTime();
            }

            calculeDeltaTime(now);
            vue.getContext().clearRect(0, 0, vue.getWidth(), vue.getHeight());

            for (int i = 0; i < getEntities().size(); i++) {
                Entity e = getEntities().get(i);
                moveEntities(e);

                for (int j = i + 1; j < getEntities().size(); j++) {
                    if (getGhost().intersects(getEntities().get(j))){
                        handleIntersects(j);
                    }
                    else if (getGhost().passObstacle(getEntities().get(j))) {
                        handleScoring(j);
                    }
                    if (debugMode){
                        if (!getGhost().intersects(getEntities().get(j))){
                            getEntities().get(j).setColor(Color.YELLOW);
                        }
                    }

                }
                e.draw(vue.getContext(), debugMode);
            }
            if (getGhost().getScore() % 10 == 0 & getGhost().getScore() != 0 & getAccelerationBool()){
                accelerateGame(vue.getParTrans());

                for (int s = 1; s < getEntities().size(); s++) {
                    getEntities().get(s).setXSpeed(-getBgSpeed());
                }
                setAccelerationBool(false);
            }
            incrementTimeToSpawn();

            /**
             * Spawn un obstacle chaque 3 secondes
             */
            if (getTimeToSpawn() - 3 > 0){
                handleSpawn();
            }
            /**
             * Pour être sûr de pas trop utiliser de la mémoire vive
             */
            if (getEntities().size() > 5) {
                getEntities().remove(1);
            }
            setLastTime(now);
        }
    };
};

