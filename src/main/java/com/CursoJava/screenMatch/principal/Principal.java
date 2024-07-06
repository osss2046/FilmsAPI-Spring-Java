package com.CursoJava.screenMatch.principal;

import com.CursoJava.screenMatch.model.DatosEpisodio;
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
        //temporadas.forEach(System.out::println);


        // Mostrar solo el titulo de los episodios para cada temporada
//        for (int i = 0; i < datos.totalDeTemporadas(); i++) {
//            int numeroTemporada = temporadas.get(i).numero();
//            int numeroDeEpisodios = temporadas.get(i).episodios().size();
//            System.out.println("Temporada: "+ numeroTemporada + ", Cantidad Episodios: "+ numeroDeEpisodios);
//            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                int nro = j+1;
//                System.out.println("(" + nro + ") " + episodiosTemporada.get(j).titulo());
//            }
//        }

        // mejorar lo anterior usando funciones lambda

        temporadas.forEach(t -> {
            System.out.println("Temporada: "+t.numero()+", Episodios: "+t.episodios().size());
            t.episodios().forEach(e -> {
                System.out.println("(" + e.numeroEpisodio() + "). " + e.titulo());
            });
        });

    }
}
