package org.rmartinez.appmockito.ejemplos.repositories;

import org.rmartinez.appmockito.ejemplos.Datos;

import java.util.List;

public class PreguntaRepositoryImpl implements PreguntaRepository{
    @Override
    public void guardarVarias(List<String> preguntas) {

    }

    @Override
    public List<String> findPreguntasPorExamen(Long id) {
        System.out.println("Hola");
        return Datos.PREGUNTAS;
    }
}
