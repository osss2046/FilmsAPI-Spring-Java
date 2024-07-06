package com.CursoJava.screenMatch;

import com.CursoJava.screenMatch.model.DatosEpisodio;
import com.CursoJava.screenMatch.model.DatosSerie;
import com.CursoJava.screenMatch.model.DatosTemporadas;
import com.CursoJava.screenMatch.principal.EjemploStreams;
import com.CursoJava.screenMatch.principal.Principal;
import com.CursoJava.screenMatch.service.ConsumoAPI;
import com.CursoJava.screenMatch.service.ConvierteDatos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.MuestraMenu();
//		EjemploStreams ejemploStreams = new EjemploStreams();
//		ejemploStreams.muestraEjemplo();
	}
}

