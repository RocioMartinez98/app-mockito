package org.rmartinez.appmockito.ejemplos.services;

import org.rmartinez.appmockito.ejemplos.models.Examen;

import java.util.Optional;

public interface ExamenService {
    Optional<Examen> findExamenPorNombre(String nombre);
    Examen findExamenPorNombreConPrguntas(String nombre);

    Examen guardar(Examen examen);
}
