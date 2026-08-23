package org.example;

// Eigene main-Klasse ohne Application-Vererbung, damit das gebaute Jar auch
// ohne "--module-path" starten kann (sonst meckert Java wegen "JavaFX runtime components are missing").
public class Launcher {
    public static void main(String[] args) {
        App.main(args);
    }
}