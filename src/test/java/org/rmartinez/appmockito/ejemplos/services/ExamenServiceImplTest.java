package org.rmartinez.appmockito.ejemplos.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

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

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {
    @Mock
    ExamenRepositoryImpl repository;
    @Mock
    PreguntaRepositoryImpl preguntaRepository;
    @InjectMocks
    ExamenServiceImpl service;

    @Captor
    ArgumentCaptor<Long> captor;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
        //repository = mock(ExamenRepository.class);
        //preguntaRepository = mock(PreguntaRepository.class);
        //service = new ExamenServiceImpl(repository, preguntaRepository);
    }

    @Test
    void findExamenPorNombre() {

        when(repository.findAll()).thenReturn(Datos.EXAMENES);
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

    @Test
    void testPreguntasExamen() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamen(5L)).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenPorNombreConPrguntas("Matematicas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("trigonometría"));
    }

    @Test
    void testPreguntasExamenVerify() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamen(5L)).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenPorNombreConPrguntas("Matematicas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("trigonometría"));
        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamen(5L);

    }

    @Test
    void testNoExisteExamenVerify() {
        // Given
        when(repository.findAll()).thenReturn(Collections.emptyList());
        when(preguntaRepository.findPreguntasPorExamen(5L)).thenReturn(Datos.PREGUNTAS);

        // When
        Examen examen = service.findExamenPorNombreConPrguntas("Matematicas");

        // Then
        assertNull(examen);
        assertTrue(examen.getPreguntas().contains("trigonometría"));
        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamen(5L);
    }

    @Test
    void testguardarExamen() {
        // Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        when(repository.guardar(any(Examen.class))).then(new Answer<Examen>(){

            Long secuencia = 8L;
            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });

        // When
        Examen examen = service.guardar(newExamen);

        // Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Física", examen.getNombre());

        verify(repository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }

    @Test
    void testManejoException() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
        when(preguntaRepository.findPreguntasPorExamen(isNull())).thenThrow(IllegalArgumentException.class);
        Exception exception = assertThrows(IllegalArgumentException.class,() -> {
            service.findExamenPorNombreConPrguntas("Matematicas");
        });
        assertEquals(IllegalArgumentException.class, exception.getClass());

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamen(isNull());
    }

    @Test
    void testArgumentMatchers() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamen(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPrguntas("Matematicas");

        verify(repository).findAll();
        //verify(preguntaRepository).findPreguntasPorExamen(eq(5L)); //Valida que sea 5L
        //verify(preguntaRepository).findPreguntasPorExamen(argThat(arg -> arg != null && arg.equals(5L))); //Valida que sea 5L y != null
        verify(preguntaRepository).findPreguntasPorExamen(argThat(arg -> arg != null && arg >= 5L));
    }

    public static class MiArgsMatchers implements ArgumentMatcher<Long>{
        private Long argument;
        @Override
        public boolean matches(Long argument) {
            this.argument= argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString() {
            return "Es para un mensaje personalizado de error que imprime mochito en caso de que falle el test, debe ser un numero positivo";
        }
    }

    @Test
    void testArgumentMatchers2() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES_NEGATIVO);
        when(preguntaRepository.findPreguntasPorExamen(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPrguntas("Matematicas");

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamen(argThat(new MiArgsMatchers()));
    }

    @Test
    void testArgumentCaptor() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        //when(preguntaRepository.findPreguntasPorExamen(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPrguntas("Matematicas");

        //ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

        verify(preguntaRepository).findPreguntasPorExamen(captor.capture());

        assertEquals(5L, captor.getValue());
    }

    //doThrow, se usa para metodos void
    @Test
    void testDoThrow() {
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);
        doThrow(IllegalArgumentException.class).when(preguntaRepository).guardarVarias(anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            service.guardar(examen);
        });
    }

    //doAnswer
    @Test
    void name() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        //when(preguntaRepository.findPreguntasPorExamen(anyLong())).thenReturn(Datos.PREGUNTAS);
        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 5L? Datos.PREGUNTAS : Collections.emptyList();
        }).when(preguntaRepository).findPreguntasPorExamen(anyLong());

        Examen examen = service.findExamenPorNombreConPrguntas("Matematicas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("geometría"));
        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());

        verify(preguntaRepository).findPreguntasPorExamen(anyLong());
    }

    //doCallRealMethod
    @Test
    void testDoCallRealMethod() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        //when(preguntaRepository.findPreguntasPorExamen(anyLong())).thenReturn(Datos.PREGUNTAS);

        doCallRealMethod().when(preguntaRepository).findPreguntasPorExamen(anyLong());
        Examen examen = service.findExamenPorNombreConPrguntas("Matematicas");
        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
    }

    //Spy y doReturn
    @Test
    void testSpy() {
        ExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
        PreguntaRepository preguntaRepository = spy(PreguntaRepositoryImpl.class);
        ExamenService examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);

        List<String> preguntas = Arrays.asList("aritmética");
        //when(preguntaRepository.findPreguntasPorExamen(anyLong())).thenReturn(preguntas);
        //when(preguntaRepository.findPreguntasPorExamen(anyLong())).thenReturn(Datos.PREGUNTAS);
        doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamen(anyLong());


        Examen examen = examenService.findExamenPorNombreConPrguntas("Matematicas");
        assertEquals(5, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamen(anyLong());
    }
}