package com.example.betweenie;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BetweenleGUI extends Application {

    private Stage ventana;

    private Diccionario diccionario;
    private Betweenle juego;

    private String rutaArchivo;
    private String idiomaSeleccionado;
    private int dificultadSeleccionada;

    // Elementos de la pantalla de juego
    private Label mensaje;
    private Label contadorIntentos;
    private Label palabrasRestantes;

    private TextField campoPalabra;

    private Button comprobar;
    private ShapeButton pistaArriba;
    private ShapeButton pistaAbajo;
    private ImageButton pistaLetra;
    private VBox palabrasArriba;
    private VBox palabrasAbajo;

    @Override
    public void start(Stage stage) {

        ventana = stage;
        ventana.setTitle("Betweenle");

        mostrarMenu();

        ventana.show();
    }

    // -------------------------------
    // PANTALLA PRINCIPAL
    // -------------------------------

    private void mostrarMenu() {

        Label titulo = new Label("BETWEENLE");
        titulo.setStyle(
                "-fx-font-size: 38px;" +
                        "-fx-font-weight: bold;"
        );

        Label subtitulo = new Label(
                "Adivina la palabra secreta"
        );

        subtitulo.setStyle("-fx-font-size: 16px;");

        Label dificultad = new Label("Selecciona la dificultad:");

        ComboBox<String> opcionesDificultad = new ComboBox<>();

        opcionesDificultad.getItems().addAll(
                "Fácil (5 letras)",
                "Intermedio (6 letras)",
                "Difícil (7 letras)"
        );

        opcionesDificultad.setValue("Fácil (5 letras)");

        Label idioma = new Label("Selecciona el idioma:");

        ComboBox<String> opcionesIdioma = new ComboBox<>();

        opcionesIdioma.getItems().addAll(
                "Español",
                "English"
        );

        opcionesIdioma.setValue("Español");

        ShapeButton iniciar = new ShapeButton("INICIAR JUEGO");

        iniciar.ponerFormaRectangulo(190, 50);
        iniciar.setPrefSize(190, 50);

        iniciar.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;"
        );

        iniciar.setOnAction(e -> {

            dificultadSeleccionada =
                    opcionesDificultad.getSelectionModel()
                            .getSelectedIndex() + 5;

            idiomaSeleccionado = opcionesIdioma.getValue();

            if (idiomaSeleccionado.equals("Español")) {
                rutaArchivo = "Palabras_Espanol.txt";
            } else {
                rutaArchivo = "Palabras_Ingles.txt";
            }

            iniciarJuego();
        });

        VBox contenido = new VBox(
                18,
                titulo,
                subtitulo,
                dificultad,
                opcionesDificultad,
                idioma,
                opcionesIdioma,
                iniciar
        );

        contenido.setAlignment(Pos.CENTER);
        contenido.setPadding(new Insets(30));

        contenido.setStyle("-fx-background-color: #eeeeee;");

        Scene escena = new Scene(contenido, 600, 550);

        ventana.setScene(escena);
    }

    // -------------------------------
    // INICIAR PARTIDA
    // -------------------------------

    private void iniciarJuego() {

        diccionario = new Diccionario();

        diccionario.cargarArchivo(rutaArchivo);

        if (diccionario.cantidadPalabras() == 0) {

            mostrarAlerta(
                    "Error",
                    "No se encontraron palabras en el archivo."
            );

            return;
        }

        juego = new Betweenle(
                diccionario,
                dificultadSeleccionada,
                idiomaSeleccionado.equals("Español") ? "es" : "en"
        );

        mostrarPantallaJuego();
    }

    // -------------------------------
    // PANTALLA DEL JUEGO
    // -------------------------------

    private void mostrarPantallaJuego() {

        Label titulo = new Label("BETWEENLE");

        titulo.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;"
        );

        Label informacion = new Label(
                idiomaSeleccionado + " | " +
                        dificultadSeleccionada + " letras"
        );

        contadorIntentos = new Label("Intentos: 0 / 14");

        palabrasRestantes = new Label(
                "Palabras posibles: " + juego.getPalabrasRestantes()
        );

        VBox encabezado = new VBox(
                8,
                titulo,
                informacion,
                contadorIntentos,
                palabrasRestantes
        );

        encabezado.setAlignment(Pos.CENTER);

        Label tituloArriba = new Label("↑ ARRIBA");
        tituloArriba.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        palabrasArriba = new VBox(8);
        palabrasArriba.setAlignment(Pos.CENTER);

        ScrollPane scrollArriba = new ScrollPane(palabrasArriba);
        scrollArriba.setFitToWidth(true);
        scrollArriba.setPrefHeight(130);
        scrollArriba.setMaxWidth(220);

// Palabras que están abajo
        Label tituloAbajo = new Label("↓ ABAJO");
        tituloAbajo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        palabrasAbajo = new VBox(8);
        palabrasAbajo.setAlignment(Pos.CENTER);

        ScrollPane scrollAbajo = new ScrollPane(palabrasAbajo);
        scrollAbajo.setFitToWidth(true);
        scrollAbajo.setPrefHeight(130);
        scrollAbajo.setMaxWidth(220);

        // Campo para escribir palabras

        campoPalabra = new TextField();

        campoPalabra.setPromptText("Escribe tu palabra...");

        campoPalabra.setMaxWidth(300);

        campoPalabra.setAlignment(Pos.CENTER);

        comprobar = new Button("COMPROBAR");

        comprobar.setPrefWidth(150);

        comprobar.setOnAction(e -> comprobarPalabra());

        campoPalabra.setOnAction(e -> comprobarPalabra());

        VBox entrada = new VBox(
                12,
                new Label("¿Cuál es la palabra secreta?"),
                campoPalabra,
                comprobar
        );

        entrada.setAlignment(Pos.CENTER);

        // Mensajes

        mensaje = new Label("¡Comienza a jugar!");

        mensaje.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );

        mensaje.setWrapText(true);

        // Pistas

        Label tituloPistas = new Label("PISTAS");

        tituloPistas.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );

        pistaArriba = new ShapeButton("1% Arriba");

        pistaArriba.ponerFormaRectangulo(130, 45);
        pistaArriba.setPrefSize(130, 45);

        pistaAbajo = new ShapeButton("1% Abajo");

        pistaAbajo.ponerFormaRectangulo(130, 45);
        pistaAbajo.setPrefSize(130, 45);

        pistaLetra = new ImageButton();

        pistaLetra.ponerImagen("/com/example/betweenie/imagenes/pista.png", 25, 25);

        pistaLetra.setText("Primera letra");

        pistaArriba.setOnAction(e -> {

            mensaje.setText(juego.pistaArriba());

            actualizarEstado();
        });

        pistaAbajo.setOnAction(e -> {

            mensaje.setText(juego.pistaAbajo());

            actualizarEstado();
        });

        pistaLetra.setOnAction(e -> {

            char letra = juego.pistaPrimeraLetra();

            mensaje.setText("La primera letra es: " + letra);

        });

        HBox botonesPistas = new HBox(
                12,
                pistaArriba,
                pistaAbajo,
                pistaLetra
        );

        botonesPistas.setAlignment(Pos.CENTER);

        // Botón de sonido

        SoundButton sonido = new SoundButton("♪ Sonido");

        sonido.ponerArchivo("/sonidos/click.mp3");

        // Botón para regresar

        Button regresar = new Button("Volver al menú");

        regresar.setOnAction(e -> mostrarMenu());
        VBox columnaArriba = new VBox(
                10,
                tituloArriba,
                scrollArriba
        );

        columnaArriba.setAlignment(Pos.CENTER);

        VBox columnaAbajo = new VBox(
                10,
                tituloAbajo,
                scrollAbajo
        );

        columnaAbajo.setAlignment(Pos.CENTER);

        HBox historial = new HBox(
                30,
                columnaArriba,
                columnaAbajo
        );

        historial.setAlignment(Pos.CENTER);

        VBox contenido = new VBox(
                22,
                encabezado,
                entrada,
                mensaje,
                historial,
                tituloPistas,
                botonesPistas,
                sonido,
                regresar
        );
        contenido.setAlignment(Pos.CENTER);
        contenido.setPadding(new Insets(25));

        contenido.setStyle("-fx-background-color: #eeeeee;");

        Scene escena = new Scene(contenido, 700, 650);

        ventana.setScene(escena);
    }

    // -------------------------------
    // COMPROBAR PALABRA
    // -------------------------------

    private void comprobarPalabra() {

        String intento = campoPalabra.getText()
                .trim().toLowerCase();

        if (intento.isEmpty()) {
            mensaje.setText("Escribe una palabra.");
            return;
        }

        String definicion = "";

        // Si no existe, solicitar definición

        if (!diccionario.existePalabra(intento)) {

            TextInputDialog dialogo = new TextInputDialog();

            dialogo.setTitle("Nueva palabra");

            dialogo.setHeaderText(
                    "La palabra no está en el diccionario."
            );

            dialogo.setContentText(
                    "Escribe su definición:"
            );

            var resultado = dialogo.showAndWait();

            if (resultado.isEmpty() ||
                    resultado.get().trim().isEmpty()) {

                mensaje.setText(
                        "Debes proporcionar una definición."
                );

                return;
            }

            definicion = resultado.get().trim();

            // Guardar palabra y definición
            diccionario.guardarPalabra(
                    rutaArchivo,
                    intento,
                    definicion
            );
        }

        String resultado = juego.comprobarIntento(
                intento,
                definicion
        );

        mensaje.setText(resultado);
        if (!resultado.equals("El juego ha terminado.")
                && !resultado.startsWith("La palabra debe tener")) {

            agregarPalabra(intento);
        }

        campoPalabra.clear();

        actualizarEstado();
    }

    // -------------------------------
    // ACTUALIZAR INFORMACIÓN
    // -------------------------------

    private void actualizarEstado() {

        contadorIntentos.setText(
                "Intentos: " + juego.getIntentos() + " / 14"
        );

        palabrasRestantes.setText(
                "Palabras posibles: " +
                        juego.getPalabrasRestantes()
        );

        if (juego.juegoTerminado()) {

            campoPalabra.setDisable(true);
            comprobar.setDisable(true);

            pistaArriba.setDisable(true);
            pistaAbajo.setDisable(true);
            pistaLetra.setDisable(true);

            if (juego.jugadorGano()) {

                mensaje.setText(
                        "¡Felicidades! Encontraste la palabra."
                );

            } else {

                mensaje.setText(
                        "Fin del juego. ¡Inténtalo nuevamente!"
                );
            }
        }
    }

    // -------------------------------
    // ALERTAS
    // -------------------------------

    private void mostrarAlerta(String titulo, String texto) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(texto);

        alerta.showAndWait();
    }
    private void agregarPalabra(String palabra) {
        for (var elemento : palabrasArriba.getChildren()) {
            if (((Label) elemento).getText().equals(palabra)) {
                return;
            }
        }

        for (var elemento : palabrasAbajo.getChildren()) {
            if (((Label) elemento).getText().equals(palabra)) {
                return;
            }
        }
        Label nuevaPalabra = new Label(palabra);

        nuevaPalabra.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-padding: 5px;"
        );

        int comparacion = juego.compararPalabra(palabra);

        if (comparacion > 0) {

            palabrasArriba.getChildren().add(nuevaPalabra);

        } else if (comparacion < 0) {

            palabrasAbajo.getChildren().add(nuevaPalabra);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
