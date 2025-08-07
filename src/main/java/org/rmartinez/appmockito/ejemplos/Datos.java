package org.rmartinez.appmockito.ejemplos;

import org.rmartinez.appmockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {
    public final static List<Examen> EXAMENES = Arrays.asList(new Examen(5L, "Matematicas")
            , new Examen(6L,"Lengua")
            , new Examen(7L,"Ingles"));
    public final static List<String> PREGUNTAS = Arrays.asList("aritmética", "integrales","derivadas","trigonometría","geometría");

    public final static Examen EXAMEN = new Examen(null, "Física");

    public final static List<Examen> EXAMENES_ID_NULL = Arrays.asList(new Examen(null, "Matematicas")
            , new Examen(null,"Lengua")
            , new Examen(null,"Ingles"));

    public final static List<Examen> EXAMENES_NEGATIVO = Arrays.asList(new Examen(-5L, "Matematicas")
            , new Examen(-6L,"Lengua")
            , new Examen(null,"Ingles"));
}
