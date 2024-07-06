package com.CursoJava.screenMatch.principal;

import com.CursoJava.screenMatch.model.DatosSerie;
import com.CursoJava.screenMatch.model.DatosTemporadas;
import com.CursoJava.screenMatch.service.ConsumoAPI;
import com.CursoJava.screenMatch.service.ConvierteDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private String URI = "https://www.omdbapi.com/?t=";
    private String APIKey = "&apikey=375c629f";
    private ConvierteDatos conversor = new ConvierteDatos();

    public void MuestraMenu() {

        // Busca datos generales de todas las series

        System.out.println("Por favor escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URI + nombreSerie.replace(" ", "+") + APIKey);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        // BUsca los datos de toda las temporadas
        List<DatosTemporadas> temporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totalDeTemporadas() ; i++) {

            json = consumoAPI.obtenerDatos(URI + nombreSerie.replace(" ", "+") + "&Season=" + i + APIKey);
            var datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporadas);

        }
        temporadas.forEach(System.out::println);
    }
}
