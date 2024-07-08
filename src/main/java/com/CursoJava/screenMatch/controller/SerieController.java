package com.CursoJava.screenMatch.controller;

import com.CursoJava.screenMatch.dto.SerieDTO;
import com.CursoJava.screenMatch.repository.SerieRepository;
import com.CursoJava.screenMatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {

    @Autowired
    private SerieService servicio;

    @GetMapping("/series")
    public List<SerieDTO> obtenerTodasLasSeries() {
        return servicio.obtenerTodasLasSeries();

    }

    @GetMapping("/series/top5")
    public List<SerieDTO> obtenerTop5() {
        return servicio.obtenerTop5();
    }
}
