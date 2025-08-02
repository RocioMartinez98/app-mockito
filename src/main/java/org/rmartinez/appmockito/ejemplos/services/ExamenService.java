package org.rmartinez.appmockito.ejemplos.services;

import org.rmartinez.appmockito.ejemplos.models.Examen;

public interface ExamenService {
    Examen findExamenPorNombre(String nombre);

}
