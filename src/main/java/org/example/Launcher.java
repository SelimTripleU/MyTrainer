package org.example;

// Separate main class without extending Application, so the built jar can also
// start without "--module-path" (otherwise Java complains about "JavaFX runtime components are missing").
public class Launcher {
    public static void main(String[] args) {
        App.main(args);
    }
}