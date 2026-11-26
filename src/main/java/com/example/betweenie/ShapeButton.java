package com.example.betweenie;

import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class ShapeButton extends Button {
    public ShapeButton(String texto){
            super(texto);
    }
    public void ponerFormaCirculo(double radio){
        this.setShape(new Circle(radio));
    }
    public void ponerFormaRectangulo(double largo, double ancho){
        this.setShape(new Rectangle(largo,ancho));
    }
    public void ponerFormaTriangulo(double tamano){
        Polygon triangulo = new Polygon();
        triangulo.getPoints().addAll(0.0,0.0,tamano, 0.0, tamano/2,tamano);
        this.setShape(triangulo);
    }
}

