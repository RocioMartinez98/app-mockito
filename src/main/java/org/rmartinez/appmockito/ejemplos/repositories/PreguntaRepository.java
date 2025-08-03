package org.rmartinez.appmockito.ejemplos.repositories;

import java.util.List;

public interface PreguntaRepository {
    List<String> findPreguntasPorExamen(Long id);


}
