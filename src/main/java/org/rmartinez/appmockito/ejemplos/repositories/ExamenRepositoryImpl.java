package org.rmartinez.appmockito.ejemplos.repositories;

import org.rmartinez.appmockito.ejemplos.Datos;
import org.rmartinez.appmockito.ejemplos.models.Examen;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryImpl implements ExamenRepository{
    @Override
    public Examen guardar(Examen examen) {
        return Datos.EXAMEN;
    }

    @Override
    public List<Examen> findAll() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Datos.EXAMENES;
    }
}
