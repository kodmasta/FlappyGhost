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
import static javafx.application.Platform.*;
/**
 * Voici notre version du jeu Flappy Ghost pour le travail pratique #2
 * Le projet a été décomposé en utilisant la méthode de décomposition MVC
 * Nous avons les modèles (classes Entity, Ghost, Obstacle, ObstacleQ, ObstacleSin)
 * Nous avons la vue (classe VueGUI)
 * Nous avons le contrôleur (ControleurGUI)
 *
 * @author Casta Ung, Heng Wei
 */
public class VueGUI extends Application {
    /**
     * Variables diverses
     */
    private static final double WIDTH = 640;
    private static final double HEIGHT = 440;
    /**
     * Éléments graphiques
     */
    private Button pauseButton;
    private CheckBox debugCheckBox;
    private Text scoreCounter;
    private Separator sep1;
    private Separator sep2;
    private Canvas canvas;
    private Image bgImg;
    private ImageView background;
    private ImageView background2;
    /**
     * Éléments d'animation
     */
    private ParallelTransition parTrans;
    private GraphicsContext context;
    /**
     * Création du contrôleur
     */
    private ControleurGUI controleur = new ControleurGUI(this);
    /**
     * Fonction main pour démarrer le jeu
     */
    public static void main(String[] args) {
        launch();
    }
    /**
     * Getter de scoreCounter
     * @return la variable contenant le texte du compteur de score
     */
    public Text getScoreCounter(){return scoreCounter;}
    /**
     * Getter de parTrans
     * @return la variable contenant le défilement du background
     */
    public ParallelTransition getParTrans(){return parTrans;}
    /**
     * Getter de context
     * @return la variable contenant le contexte
     */
    public GraphicsContext getContext(){return context;}
    /**
     * Getter de pauseButton
     * @return la variable contenant le boutton pause
     */
    public Button getPauseButton(){return pauseButton;}
    /**
     * Getter de debugCheckBox
     * @return la variable contenant le checkbox de debug
     */
    public CheckBox getDebugCheckBox(){return debugCheckBox;}
    /**
     * Getter de canvas
     * @return la variable contenant le canvas
     */
    public Canvas getCanvas(){return canvas;}
    /**
     * Getter du diamètre du canvas
     * @return le diamètre du canvas
     */
    public double getWidth(){
        return this.WIDTH;
    }
    /**
     * Getter de la hauteur du canvas
     * @return la hauteur du canvas
     */
    public double getHeight(){
        return this.HEIGHT;
    }
    /**
     * Le point d'entrée de l'application
     * @param stage le contenant au haut niveau de l'application
     * @throws Exception s`il y a un exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        /**
         * Control buttons section
         */
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
        /**
         * Canvas section
         */
        Pane canvasWrapper = new Pane();
        canvas = new Canvas(WIDTH,HEIGHT-25);
        bgImg = new Image("file:fichiersFH/bg.png");
        background = new ImageView(bgImg);
        background2 = new ImageView(bgImg);
        canvasWrapper.getChildren().addAll(background, background2);
        background.fitHeightProperty().bind(canvasWrapper.heightProperty());
        background.fitWidthProperty().bind(canvasWrapper.widthProperty());
        background2.fitHeightProperty().bind(canvasWrapper.heightProperty());
        background2.fitWidthProperty().bind(canvasWrapper.widthProperty());

        /**
         * Animation pour le défilement du background
         */
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
        parTrans = new ParallelTransition(trans1, trans2);
        parTrans.setRate(controleur.getBgSpeed());
        parTrans.play();
        canvasWrapper.getChildren().add(canvas);
        root.setCenter(canvasWrapper);
        context = canvas.getGraphicsContext2D();
        /**
         * Fixe le problème du focus de la souris sur le boutton pause au début
         */
        runLater(() -> {
            canvas.requestFocus();
        });
        /**
         * Lorsqu’on clique ailleurs sur la scène, le focus retourne sur le canvas
         */
        scene.setOnMouseClicked((event) -> {
            canvas.requestFocus();
        });
        /**
         * Space bar pour sauter et escape button pour fermer la fenêtre
         */
        scene.setOnKeyPressed((value) -> {
            if (value.getCode() == KeyCode.SPACE) {
                controleur.flap();
            }else if (value.getCode() == KeyCode.ESCAPE){
                exit();
            }
        });
        /**
         * Début de l'animation
         */
        controleur.timer.start();
        /**
         * Pause button event
         */
        pauseButton.setOnAction((event) -> {
            controleur.pauseButton();
        });
        /**
         * Debug checkbox event
         */
        debugCheckBox.setIndeterminate(false);
        debugCheckBox.setOnAction((event) -> {
            controleur.debugCheckBox();
        });

        stage.setTitle("Flappy Ghost");
        stage.getIcons().add(new Image("file:fichiersFH/ghost.png"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}