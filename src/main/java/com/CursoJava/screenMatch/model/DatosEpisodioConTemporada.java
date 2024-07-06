package com.CursoJava.screenMatch.model;

public record DatosEpisodioConTemporada(
        String titulo,
        Integer numeroEpisodio,
        String evaluacion,
        String fechaLanzamiento,
        Integer numeroTemporada
) {
    public DatosEpisodioConTemporada(DatosEpisodio episodio, Integer numeroTemporada) {
        this(episodio.titulo(), episodio.numeroEpisodio(), episodio.evaluacion(), episodio.fechaLanzamiento(), numeroTemporada);
    }
}
