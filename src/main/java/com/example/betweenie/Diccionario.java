package com.example.betweenie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.HashSet;

public class Diccionario {
    private HashMap<String, Integer> diccionario;
    private HashMap<String, String> definiciones;

    public Diccionario() {
        diccionario = new HashMap<>();
        definiciones = new HashMap<>();
    }

    public void cargarArchivo(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;

            while ((linea = br.readLine()) != null) {

                linea = linea.trim().toLowerCase();

                if (linea.isEmpty()) {
                    continue;
                }

                String[] partes = linea.split(":", 2);

                String palabra = partes[0].trim();

                if (palabra.matches("[a-záéíóúüñ]+")) {

                    diccionario.put(palabra, palabra.length());

                    if (partes.length > 1) {
                        definiciones.put(palabra, partes[1].trim());
                    }
                }
            }
            System.out.println("Palabras cargadas: " + diccionario.size());

        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
        }
    }
    private String quitarAcentos(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return texto.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    public boolean existePalabra(String palabra) {
        palabra = palabra.toLowerCase().trim();
        return diccionario.containsKey(palabra);
    }
    public HashSet<String> obtenerPalabras(int longitud) {
        HashSet<String> palabras = new HashSet<>();

        for (String palabra : diccionario.keySet()) {
            if (diccionario.get(palabra) == longitud) {
                palabras.add(palabra);
            }
        }

        return palabras;
    }

    public int obtenerLongitud(String palabra) {
        palabra = palabra.toLowerCase().trim();
        return diccionario.getOrDefault(palabra, 0);
    }
    public void guardarPalabra(String ruta, String palabra,
                               String definicion) {

        if (diccionario.containsKey(palabra)) {
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(ruta, true))) {

            bw.write(palabra + ":" + definicion);
            bw.newLine();

            diccionario.put(palabra, palabra.length());
            definiciones.put(palabra, definicion);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void agregarPalabra(String palabra, String definicion) {
        diccionario.put(palabra, palabra.length());
    }

    public int cantidadPalabras() {
        return diccionario.size();
    }
}
