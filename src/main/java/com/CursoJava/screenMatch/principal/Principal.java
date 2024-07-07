package com.CursoJava.screenMatch.principal;

import com.CursoJava.screenMatch.model.*;
import com.CursoJava.screenMatch.service.ConsumoAPI;
import com.CursoJava.screenMatch.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

//        temporadas.forEach(t -> {
//            System.out.println("Temporada: "+t.numero()+", Episodios: "+t.episodios().size());
//            t.episodios().forEach(e -> {
//                System.out.println("(" + e.numeroEpisodio() + "). " + e.titulo());
//            });
//        });

        // convertir todas las informaciones a una lista del tipo datosEpisodio

        List<DatosEpisodioConTemporada> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(e -> new DatosEpisodioConTemporada(e, t.numero())))
                .collect(Collectors.toList());



        // Top 5 Episodios
        System.out.println("Top 5 episodios");
        datosEpisodios.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DatosEpisodioConTemporada::evaluacion).reversed())
                .limit(5)
                .forEach(System.out::println);

        // Convirtiendo los datos a una lista del tipo Episodio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

        // Busqueda de episodios a partir de X años

//        System.out.println("Por favor indica el año a partir del cual deseas ver los episodios");
//        var fecha = teclado.nextInt();
//        teclado.nextLine();
//
//        LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada()
//                                + ". Episodio: " + e.getTitulo()
//                                + ". Fecha de lanzamiento: " + e.getFechaDeLanzamiento().format(dtf)
//
//                ));


//
//        // Busca episodios por pedazo del titulo
//        System.out.println("Por favor escriba el titulo del episodio");
//        var pedazoTitulo = teclado.nextLine();
//
//        Optional<Episodio> episodioEncontrado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
//                .findFirst();
//        if(episodioEncontrado.isPresent()){
//            System.out.println("Episodio encontrado c: ");
//            System.out.println(episodioEncontrado);
//        }else{
//            System.out.println("Episodio No encontrado :c ");
//        }


        Map<Integer, Double> evaluacionesPorTemporada = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));

        System.out.println(evaluacionesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println(
                "Promedio de las evaluaciones: " + est.getAverage()
                        + "\nEpisodio Mejor evaluado: " + est.getMax()
                        + "\nEpisodio Peor evaluado: " + est.getMin()
        );




    }
}
