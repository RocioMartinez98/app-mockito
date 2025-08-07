package org.rmartinez.appmockito.ejemplos.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.rmartinez.appmockito.ejemplos.Datos;
import org.rmartinez.appmockito.ejemplos.models.Examen;
import org.rmartinez.appmockito.ejemplos.repositories.ExamenRepository;
import org.rmartinez.appmockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.rmartinez.appmockito.ejemplos.repositories.PreguntaRepository;
import org.rmartinez.appmockito.ejemplos.repositories.PreguntaRepositoryImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplSpyTest {
    @Spy
    ExamenRepositoryImpl repository;
    @Spy
    PreguntaRepositoryImpl preguntaRepository;
    @InjectMocks
    ExamenServiceImpl service;

    @Test
    void testSpy() {

        List<String> preguntas = Arrays.asList("aritmética");
        //when(preguntaRepository.findPreguntasPorExamen(anyLong())).thenReturn(preguntas);
        //when(preguntaRepository.findPreguntasPorExamen(anyLong())).thenReturn(Datos.PREGUNTAS);
        doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamen(anyLong());


        Examen examen = service.findExamenPorNombreConPrguntas("Matematicas");
        assertEquals(5, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamen(anyLong());
    }
}