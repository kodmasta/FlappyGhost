package com.example.flappyghost;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Orientation;
import javafx.scene.canvas.*;
import javafx.util.Duration;
import java.util.LinkedList;
import java.util.Random;
import static javafx.application.Platform.*;

public class VueGUI extends Application {
    public static final double WIDTH = 640, HEIGHT = 440, flapStrength = -300;
    public double CANVASHEIGHT;

    // Éléments graphiques
    private Button pauseButton;
    private CheckBox debugCheckBox;
    private Text scoreCounter;
    private Separator sep1;
    private Separator sep2;
    private Canvas canvas = new Canvas();
    private Image bgImg = new Image("file:fichiersFH/bg.png");
    private ImageView background;
    private ImageView background2;
    private Entity entityGhost;
    private LinkedList<Entity> entities = new LinkedList<Entity>();
    boolean debugMode = false;
    double bgSpeed = 120;
    //bgSpeed c'est la vitesse que le bg scroll
    //12*10px/sec = 120px/sec. pour augmenter de 15px, on fait bgSpeed+=1.5

    public void restartGame(ParallelTransition parTrans){
        System.out.println("Le jeu a été redémarré");
        setBgSpeed(120);
        parTrans.setRate(getBgSpeed());
        entities.clear();
        scoreCounter.setText("Score: 0");
        entityGhost = new Ghost(WIDTH/2,HEIGHT/2, new Image("file:fichiersFH/ghost.png"));
        entities.add(entityGhost); //ghost est tjrs le premier entity dans le linkedList entities
    }
    public void accelerateGame(ParallelTransition parTrans, Ghost ghost){
        System.out.println("Le jeu a été accéléré");
        setBgSpeed(getBgSpeed() + 15);
        parTrans.setRate(getBgSpeed());
        ghost.setAy(15);

    }
    public double getBgSpeed(){
        return this.bgSpeed;
    }
    public void setBgSpeed(double newBgSpeed){
        this.bgSpeed = newBgSpeed;
    }
    @Override
    public void start(Stage stage) throws Exception {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        //Control buttons section
        HBox controlBar = new HBox();
        pauseButton = new Button("Pause");
        debugCheckBox = new CheckBox("Debug");
        scoreCounter = new Text("Score: 0");
        sep1 = new Separator();
        sep2 = new Separator();
        controlBar.getChildren().addAll(pauseButton,sep1,debugCheckBox,sep2,scoreCounter);
        controlBar.setAlignment(Pos.CENTER);
        controlBar.setSpacing(10);
        sep1.setOrientation(Orientation.VERTICAL);
        sep2.setOrientation(Orientation.VERTICAL);
        root.setBottom(controlBar);

        //Canvas section
        Pane canvasWrapper = new Pane();
        canvas = new Canvas(WIDTH,HEIGHT-25);
        background = new ImageView(bgImg);
        background2 = new ImageView(bgImg);
        canvasWrapper.getChildren().addAll(background, background2);
        background.fitHeightProperty().bind(canvasWrapper.heightProperty());
        background.fitWidthProperty().bind(canvasWrapper.widthProperty());
        background2.fitHeightProperty().bind(canvasWrapper.heightProperty());
        background2.fitWidthProperty().bind(canvasWrapper.widthProperty());
        // Animation to scroll background
        TranslateTransition trans1 = new TranslateTransition(Duration.seconds(640), background);
        trans1.setFromX(0);
        trans1.setToX(-WIDTH);
        trans1.setInterpolator(Interpolator.LINEAR);
        trans1.setCycleCount(Animation.INDEFINITE);
        TranslateTransition trans2 = new TranslateTransition(Duration.seconds(640), background2);
        trans2.setFromX(WIDTH);
        trans2.setToX(0);
        trans2.setCycleCount(Animation.INDEFINITE);
        trans2.setInterpolator(Interpolator.LINEAR);
        ParallelTransition parTrans = new ParallelTransition(trans1, trans2);
        //ajout de vitesse apres chaque obstacle +15
        parTrans.setRate(getBgSpeed());
        parTrans.play();
        canvasWrapper.getChildren().add(canvas);
        root.setCenter(canvasWrapper);
        GraphicsContext context = canvas.getGraphicsContext2D();

        //Fixes the mouse focus on pause button at the beginning issue
        runLater(() -> {
            canvas.requestFocus();
        });
        /* Lorsqu’on clique ailleurs sur la scène,
           le focus retourne sur le canvas */
        scene.setOnMouseClicked((event) -> {
            canvas.requestFocus();
        });

        entityGhost = new Ghost(WIDTH/2,HEIGHT/2, new Image("file:fichiersFH/ghost.png"));
        entities.add(entityGhost); //ghost est tjrs le premier entity dans le linkedList entities

        //space bar jump feature and escape button to close window feature
        scene.setOnKeyPressed((value) -> {
            if (value.getCode() == KeyCode.SPACE) {
                entityGhost.flap(flapStrength);
            }else if (value.getCode() == KeyCode.ESCAPE){
                exit();
            }
        });


        //draws the entities(ghost+obstacles)
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;
            double timeToSpawn = 0;
            boolean accelerationBool = false;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                double deltaTime = (now - lastTime) * 1e-9;
                context.clearRect(0, 0, WIDTH, HEIGHT);

                Ghost ghost = (Ghost) entityGhost;
                for (int i = 0; i < entities.size(); i++) {
                    Entity e = entities.get(i);
                    //fix button pause
                    e.move(deltaTime);
                    for (int j = i + 1; j < entities.size(); j++) {
                        if (ghost.intersects(entities.get(j))){
                            entities.get(j).setColor(Color.RED);
                            if (!debugMode){
                                restartGame(parTrans);
                            }
                        } else if (ghost.passObstacle(entities.get(j))) {
                            entities.get(j).scored = true;
                            ghost.setScore(5);
                            scoreCounter.setText("Score: "+ghost.score);
                            if(ghost.getScore() % 10 == 0){
                                accelerationBool = true;
                            }
                        }
                    }
                    e.draw(context, debugMode);
                }
                if (ghost.getScore() % 10 == 0 & ghost.getScore() != 0 & accelerationBool){
                    accelerateGame(parTrans, ghost);

                    for (int s = 1; s < entities.size(); s++) {
                        entities.get(s).setXSpeed(-getBgSpeed());
                    }
                    accelerationBool = false;
                }
                timeToSpawn = timeToSpawn + deltaTime;
                //every 3 seconds will spawn an obstacle
                if (timeToSpawn - 3 > 0){
                    Random randNumber = new Random();
                    int obstacleMoveType = randNumber.nextInt(3);
                    double obstacleHeight = randNumber.nextDouble() * 340;
                    int obstacleStyle = randNumber.nextInt(27);
                    int obstacleRayon = randNumber.nextInt(36) + 10;
                    switch (obstacleMoveType) {
                        case 0 :
                            Obstacle obstacle = new Obstacle(680,obstacleHeight + 40, 10, Color.YELLOW, -getBgSpeed(), new Image( String.format("file:fichiersFH/obstacles/%s.png", obstacleStyle)));
                            entities.add(obstacle);
                            break;
                        case 1 :
                            ObstacleSin obstacleSin = new ObstacleSin(680,obstacleHeight + obstacleRayon, 30, Color.YELLOW, -getBgSpeed(), new Image( String.format("file:fichiersFH/obstacles/%s.png", obstacleStyle)));
                            entities.add(obstacleSin);
                            break;
                        case 2 :
                            ObstacleQ obstacleQ = new ObstacleQ(680,obstacleHeight + 40, obstacleRayon, Color.YELLOW, -getBgSpeed(), new Image( String.format("file:fichiersFH/obstacles/%s.png", obstacleStyle)));
                            entities.add(obstacleQ);
                            break;
                    }
                    timeToSpawn = 0;
                }
                //to make sure we don't overkill the memory
                if (entities.size() > 5) {
                        entities.remove(1);
                }
                lastTime = now;
            }
        };
        timer.start();

        pauseButton.setOnAction((event) -> {
            if (pauseButton.getText().equals("Pause")){
                pauseButton.setText("Resume");
                parTrans.pause();
                timer.stop();
                Platform.runLater(()->{
                    canvas.requestFocus();
                });
            }
            else {
                pauseButton.setText("Pause");
                parTrans.play();
                timer.start();
                Platform.runLater(()->{
                    canvas.requestFocus();
                });
            }
        });
        debugCheckBox.setIndeterminate(false);
        debugCheckBox.setOnAction((event) -> {
            if(debugCheckBox.isSelected()) {
                this.debugMode = true;
                Platform.runLater(()->{
                    canvas.requestFocus();
                });
            }
            else {
                this.debugMode = false;
                Platform.runLater(()->{
                    canvas.requestFocus();
                });
            }

        });


        stage.setTitle("Flappy Ghost");
        stage.getIcons().add(new Image("file:fichiersFH/ghost.png"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}