package com.example.flappyghost;
import javafx.animation.*;
import javafx.application.Application;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Orientation;
import javafx.scene.canvas.*;
import javafx.util.Duration;
import javafx.application.Platform;

import java.util.LinkedList;

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
    private ImageView bg1;
    private ImageView bg2;
    private Image bgImg = new Image("file:fichiersFH/bg.png");
    //c'est pas le filepath, mais le classpath. fichiersFH est dans "target" et non "src"
    private ImageView background;
    private ImageView background2;
    private Entity ghost;
    private LinkedList<Entity> entities = new LinkedList<Entity>();
    boolean debugMode = false;

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(getClass().getName().toString());
        int score = 0;
        double bgSpeed = 12;
        //bgSpeed c'est la vitesse que le bg scroll
        //12*10px/sec = 120px/sec. pour augmenter de 15px, on fait bgSpeed+=1.5

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        //Control buttons section
        HBox controlBar = new HBox();
        pauseButton = new Button("Pause");
        debugCheckBox = new CheckBox("Debug");
        scoreCounter = new Text("Score: "+score);
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
        TranslateTransition trans1 = new TranslateTransition(Duration.seconds(64), background);
        trans1.setFromX(0);
        trans1.setToX(-WIDTH);
        trans1.setInterpolator(Interpolator.LINEAR);
        trans1.setCycleCount(Animation.INDEFINITE);
        TranslateTransition trans2 = new TranslateTransition(Duration.seconds(64), background2);
        trans2.setFromX(WIDTH);
        trans2.setToX(0);
        trans2.setCycleCount(Animation.INDEFINITE);
        trans2.setInterpolator(Interpolator.LINEAR);
        ParallelTransition parTrans = new ParallelTransition(trans1, trans2);
        parTrans.setRate(bgSpeed);
        parTrans.play();
        canvasWrapper.getChildren().add(canvas);
        root.setCenter(canvasWrapper);
        GraphicsContext context = canvas.getGraphicsContext2D();

        //Fixes the mouse focus on pause button at the beginning issue
        Platform.runLater(() -> {
            canvas.requestFocus();
        });
        /* Lorsqu’on clique ailleurs sur la scène,
           le focus retourne sur le canvas */
        scene.setOnMouseClicked((event) -> {
            canvas.requestFocus();
        });

        ghost = new Ghost(WIDTH/2,HEIGHT/2, new Image("file:fichiersFH/ghost.png"));
        entities.add(ghost); //ghost est tjrs le premier entity dans le linkedList entities6

        //space bar jump feature
        scene.setOnKeyPressed((value) -> {
            if (value.getCode() == KeyCode.SPACE) {
                ghost.flap(flapStrength);
            }
        });

        //draws the entities(ghost+obstacles)
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                double deltaTime = (now - lastTime) * 1e-9;
                context.clearRect(0, 0, WIDTH, HEIGHT);

                for (int i = 0; i < entities.size(); i++) {
                    Entity e = entities.get(i);
                    e.move(deltaTime);
                    for (int j = i + 1; j < entities.size(); j++) {
                        e.obstacleCollided(entities.get(j));
                    }
                    e.draw(context, debugMode);
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
            }
            else {
                pauseButton.setText("Pause");
                parTrans.play();
                timer.start();
            }
        });
        debugCheckBox.setIndeterminate(false);
        debugCheckBox.setOnAction((event) -> {
            if(debugCheckBox.isSelected()) {
                this.debugMode = true;
            }
            else {
                this.debugMode = false;
            }

        });


        stage.setTitle("Flappy Ghost!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}