package org.rmartinez.appmockito.ejemplos.repositories;

import org.rmartinez.appmockito.ejemplos.models.Examen;

import java.util.List;

public interface ExamenRepository {
    Examen guardar(Examen examen);
    List<Examen> findAll();


}
