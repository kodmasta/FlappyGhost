package com.example.flappyghost;

import java.util.*;

public class ControleurGUI {
    // La vue est le point d'entrée de l'Application JavaFX
    private VueGUI vue;
    // Le modèle de l'application
     private Ghost ghost;
     private LinkedList<Obstacle> obstacles = new LinkedList<Obstacle>();

    ControleurGUI(VueGUI vue, Ghost ghost, LinkedList<Obstacle> obstacles) {
        this.vue = vue;
        this.ghost = ghost;
        this.obstacles = obstacles;
    }
}
