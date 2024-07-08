package com.CursoJava.screenMatch.controller;

import com.CursoJava.screenMatch.dto.SerieDTO;
import com.CursoJava.screenMatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {

    @Autowired
    private SerieRepository repository;

    @GetMapping("/series")
    public List<SerieDTO> obtenerTodasLasSeries() {
        return repository.findAll().stream()
                .map(s -> new SerieDTO(s.getTitulo(), s.getTotalTemporadas(), s.getEvaluacion(),
                        s.getPoster(), s.getGenero(), s.getActores(), s.getSinopsis()))
                .collect(Collectors.toList());
    }

}
