package com.CursoJava.screenMatch.principal;

import com.CursoJava.screenMatch.model.*;
import com.CursoJava.screenMatch.repository.SerieRepository;
import com.CursoJava.screenMatch.service.ConsumoAPI;
import com.CursoJava.screenMatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=375c629f";
    private ConvierteDatos conversor = new ConvierteDatos();

    private List<DatosSerie> datosSeries = new ArrayList<>();
    private SerieRepository repository;

    private List<Serie> series;

    private Optional<Serie> serieBuscada;
    public Principal(SerieRepository repository) {
        this.repository = repository;
    }


    public void muestraMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar Serie por Titulo
                    5 - Top 5 Mejores Series
                    6 - Buscar Series por Categoria
                    7 - filtrar series por temporadas y evaluación
                    8 - Buscar episodio por Titulo 
                    9 - Top 5 Episodios por Serie
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCategorias();
                    break;
                case 7:
                    filtrarSeriesPorTemporadaYEvaluacion();
                    break;
                case 8:
                    buscarEpisodiosPorTitulo();
                    break;
                case 9:
                    buscarTop5Episodios();
                    break;

                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }



    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }
    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie de la cual quieres ver los episodios");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();
        if(serie.isPresent()){
            var serieEncontrada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo()
                        .replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);

        }else{
            System.out.println("Serie no encontrada :c");
        }

    }
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        //datosSeries.add(datos);
        Serie serie = new Serie(datos);
        repository.save(serie);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {
        series = repository.findAll();

        series.stream().sorted(Comparator.comparing((Serie::getGenero)))
                .forEach(System.out::println);


    }

    private void buscarSeriesPorTitulo() {
        System.out.println("Ingrese el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        serieBuscada = repository.findByTituloContainsIgnoreCase(nombreSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("La serie buscada es: " + serieBuscada.get());

        } else {
            System.out.println("Serie no encontrada");

        }
    }

    private void buscarTop5Series() {
        List<Serie> topSeries = repository.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s ->
                System.out.println("Titulo: " + s.getTitulo() + " - Evaluación: " + s.getEvaluacion()));
    }

    private void buscarSeriesPorCategorias() {
        System.out.println("Escriba el genero/categoria por la cual desea buscar");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);
        System.out.println("Las series de la categoría " + genero);
        seriesPorCategoria.forEach(System.out::println);

    }

    public void filtrarSeriesPorTemporadaYEvaluacion(){
        System.out.println("¿Filtrar series con cuántas temporadas? ");
        var totalTemporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("¿Com evaluación apartir de cuál valor? ");
        var evaluacion = teclado.nextDouble();
        teclado.nextLine();
        List<Serie> filtroSeries = repository.seriesPorTemparadaYEvaluacion(totalTemporadas,evaluacion);
        System.out.println("*** Series filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - evaluacion: " + s.getEvaluacion()));
    }


    private void buscarEpisodiosPorTitulo() {
        System.out.println("Ingrese el nombre del Episodio que desea buscar");
        var nombreEpisodio = teclado.nextLine();
        List<Episodio> episodiosEncontrados = repository.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("\nSerie: %s - Episodio %s - Temporada %s - Evaluación %s \n",
                        e.getSerie().getTitulo(), e.getNumeroEpisodio(), e.getTemporada(), e.getEvaluacion()));
    }

    private void buscarTop5Episodios() {
        buscarSeriesPorTitulo();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repository.top5Episodios(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("\nSerie: %s - Episodio %s - Temporada %s - Evaluación %s \n",
                            e.getSerie().getTitulo(), e.getNumeroEpisodio(), e.getTemporada(), e.getEvaluacion()));
        }
    }

}