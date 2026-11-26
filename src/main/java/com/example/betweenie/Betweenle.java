package com.example.betweenie;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.text.Collator;

public class Betweenle {

    private HashSet<String> palabrasPosibles;
    private HashSet<Character> letrasSecreta;

    private String palabraSecreta;
    private int intentos;
    private final int MAX_INTENTOS = 14;

    private boolean terminado;
    private boolean ganado;

    private Diccionario diccionario;
    private Collator comparador;

    public Betweenle(Diccionario diccionario, int dificultad,
                     String idioma) {

        this.diccionario = diccionario;

        palabrasPosibles = diccionario.obtenerPalabras(dificultad);
        letrasSecreta = new HashSet<>();

        intentos = 0;
        terminado = false;
        ganado = false;

        comparador = Collator.getInstance(new Locale(idioma));
        comparador.setStrength(Collator.TERTIARY);

        escogerPalabra();
    }

    private void escogerPalabra() {

        if (palabrasPosibles.isEmpty()) {
            terminado = true;
            return;
        }

        Random random = new Random();
        int posicion = random.nextInt(palabrasPosibles.size());

        Iterator<String> iterador = palabrasPosibles.iterator();

        for (int i = 0; i < posicion; i++) {
            iterador.next();
        }

        palabraSecreta = iterador.next();

        // Guardar las letras únicas de la palabra secreta
        for (int i = 0; i < palabraSecreta.length(); i++) {
            letrasSecreta.add(palabraSecreta.charAt(i));
        }
    }

    public String comprobarIntento(String intento, String definicion) {

        intento = intento.trim().toLowerCase();

        if (terminado) {
            return "El juego ha terminado.";
        }

        if (intento.length() != palabraSecreta.length()) {
            return "La palabra debe tener " +
                    palabraSecreta.length() + " letras.";
        }

        if (!diccionario.existePalabra(intento)) {

            diccionario.agregarPalabra(intento, definicion);
        }

        intentos++;

        int comparacion = comparador.compare(intento, palabraSecreta);

        if (comparacion == 0) {

            ganado = true;
            terminado = true;

            return "¡Correcto! Has encontrado la palabra.";
        }

        Iterator<String> iterador = palabrasPosibles.iterator();

        if (comparacion < 0) {

            // La palabra secreta está después
            while (iterador.hasNext()) {

                String palabra = iterador.next();

                if (comparador.compare(palabra, intento) <= 0) {
                    iterador.remove();
                }
            }

            if (intentos >= MAX_INTENTOS) {
                terminado = true;
                return "Se terminaron los intentos. La palabra era: "
                        + palabraSecreta;
            }

            return "La palabra secreta está más adelante.";

        } else {

            // La palabra secreta está antes
            while (iterador.hasNext()) {

                String palabra = iterador.next();

                if (comparador.compare(palabra, intento) >= 0) {
                    iterador.remove();
                }
            }

            if (intentos >= MAX_INTENTOS) {
                terminado = true;
                return "Se terminaron los intentos. La palabra era: "
                        + palabraSecreta;
            }

            return "La palabra secreta está más atrás.";
        }
    }

    // PISTA 1: Recorrer 1% desde el inicio alfabético
    public String pistaArriba() {

        return recorrerLimite(true);
    }

    // PISTA 2: Recorrer 1% desde el final alfabético
    public String pistaAbajo() {

        return recorrerLimite(false);
    }

    private String recorrerLimite(boolean arriba) {

        if (terminado) {
            return "El juego ha terminado.";
        }

        ArrayList<String> lista =
                new ArrayList<>(palabrasPosibles);

        Collections.sort(lista, comparador);

        int posicionSecreta = lista.indexOf(palabraSecreta);

        if (posicionSecreta == -1) {
            return "No se puede utilizar esta pista.";
        }

        int cantidad = Math.max(1,
                (int) Math.ceil(lista.size() * 0.01));

        int eliminadas = 0;

        if (arriba) {

            // Eliminar palabras desde el inicio
            eliminadas = Math.min(cantidad, posicionSecreta);

            for (int i = 0; i < eliminadas; i++) {
                palabrasPosibles.remove(lista.get(i));
            }

        } else {

            // Eliminar palabras desde el final
            int disponibles = lista.size() - posicionSecreta - 1;

            eliminadas = Math.min(cantidad, disponibles);

            for (int i = 0; i < eliminadas; i++) {
                palabrasPosibles.remove(
                        lista.get(lista.size() - 1 - i)
                );
            }
        }

        return "Pista utilizada. Se eliminaron " +
                eliminadas + " palabras posibles.";
    }

    // PISTA 3: Primera letra de la palabra secreta
    public char pistaPrimeraLetra() {

        return palabraSecreta.charAt(0);
    }
    public int compararPalabra(String palabra) {
        palabra = palabra.trim().toLowerCase();

        return comparador.compare(palabra, palabraSecreta);
    }

    public boolean contieneLetra(char letra) {

        return letrasSecreta.contains(Character.toLowerCase(letra));
    }

    public int getIntentos() {
        return intentos;
    }

    public int getIntentosRestantes() {
        return MAX_INTENTOS - intentos;
    }

    public int getPalabrasRestantes() {
        return palabrasPosibles.size();
    }

    public boolean juegoTerminado() {
        return terminado;
    }

    public boolean jugadorGano() {
        return ganado;
    }
}