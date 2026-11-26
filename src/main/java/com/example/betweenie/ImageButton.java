package com.example.betweenie;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageButton extends Button{
    public ImageButton(){
        super();
    }
    public void ponerImagen(String path, double alto, double ancho){
        Image img=new Image(getClass().getResource(path).toExternalForm());
        ImageView imgView=new ImageView(img);

        imgView.setFitWidth(ancho);
        imgView.setFitHeight(alto);
        this.setGraphic(imgView);
    }
}
