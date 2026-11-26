package com.example.betweenie;

import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;
import java.net.URL;

public class SoundButton extends Button {
    private AudioClip audio;

    public SoundButton(String texto) {
        super(texto);
    }

    public void ponerArchivo(String archivo) {
        URL url = SoundButton.class.getResource(archivo);

        if (url == null) {
            System.out.println("❌ No se encontró el audio: " + archivo);
            return;
        }

        audio = new AudioClip(url.toExternalForm());

        this.setOnMouseClicked(e -> {
            if (audio != null) {
                audio.play();
            }
        });
    }
}