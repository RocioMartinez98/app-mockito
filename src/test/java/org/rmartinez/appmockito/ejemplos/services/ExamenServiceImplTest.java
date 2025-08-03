package org.rmartinez.appmockito.ejemplos.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.rmartinez.appmockito.ejemplos.models.Examen;
import org.rmartinez.appmockito.ejemplos.repositories.ExamenRepository;
import org.rmartinez.appmockito.ejemplos.repositories.ExamenRepositoryOtro;
import org.rmartinez.appmockito.ejemplos.repositories.PreguntaRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExamenServiceImplTest {
    ExamenRepository repository;
    ExamenService service;
    PreguntaRepository preguntaRepository;
    @BeforeEach
    void setUp() {
        repository = mock(ExamenRepository.class);
        preguntaRepository = mock(PreguntaRepository.class);
        service = new ExamenServiceImpl(repository, preguntaRepository);
    }

    @Test
    void findExamenPorNombre() {
        List<Examen> datos = Arrays.asList(new Examen(5L, "Matematicas")
                , new Examen(6L,"Lengua")
                , new Examen(7L,"Ingles"));

        when(repository.findAll()).thenReturn(datos);
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");

        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.get().getNombre());
    }

    @Test
    void findExamenPorNombreListaVacia() {

        List<Examen> datos = Collections.emptyList();

        when(repository.findAll()).thenReturn(datos);
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");

        assertFalse(examen.isPresent());
    }


}