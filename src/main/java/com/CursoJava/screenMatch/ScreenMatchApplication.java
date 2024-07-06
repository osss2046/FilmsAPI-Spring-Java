package com.CursoJava.screenMatch;

import com.CursoJava.screenMatch.model.DatosSerie;
import com.CursoJava.screenMatch.service.ConsumoAPI;
import com.CursoJava.screenMatch.service.ConvierteDatos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoAPI();
		var json = consumoApi.obtenerDatos("https://www.omdbapi.com/?t=game+of+thrones&apikey=375c629f");
		///var json = consumoApi.obtenerDatos("https://coffee.alexflipnote.dev/random.json");
		System.out.println(json);

		ConvierteDatos conversor = new ConvierteDatos();
		var datos = conversor.obtenerDatos(json, DatosSerie.class);
		System.out.println(datos);

	}
}

