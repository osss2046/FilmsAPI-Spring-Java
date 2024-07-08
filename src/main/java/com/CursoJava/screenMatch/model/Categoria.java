package com.CursoJava.screenMatch.model;

public enum Categoria {

    ACCION("Action","Acción"),
    ROMANCE("Romance", "Romance"),
    CRIMEN("Crime","Crimen"),
    DRAMA("Drama","Drama"),
    COMEDIA("Comedy","Comedia");

    private String categoriaOMDB;
    private String categoriaEspanol;

    Categoria(String categoriaOMDB, String categoriaEspanol) {
        this.categoriaOMDB = categoriaOMDB;
        this.categoriaEspanol = categoriaEspanol;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOMDB.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nunguna categoria encontrada: " + text);
    }

    public static Categoria fromEspanol(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaEspanol.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nunguna categoria encontrada: " + text);
    }
}
