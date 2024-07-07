package com.CursoJava.screenMatch.model;

public enum Categoria {

    ACCION("Action"),
    ROMANCE("Romance"),
    CRIMEN("Crime"),
    DRAMA("Drama"),
    COMEDIA("Comedy");

    private String categoriaOMDB;

    Categoria(String categoriaOMDB) {
        this.categoriaOMDB = categoriaOMDB;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOMDB.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nunguna categoria encontrada: " + text);
    }
}
