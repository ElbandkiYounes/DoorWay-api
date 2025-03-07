package com.doorway.Service;

import com.doorway.Exception.NotFoundException;
import com.doorway.Model.TechnicalQuestion;
import com.doorway.Payload.TechnicalQuestionPayload;
import com.doorway.Repository.TechnicalQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TechnicalQuestionServiceImplTest {

    @Mock
    private TechnicalQuestionRepository technicalQuestionRepository;

    @InjectMocks
    private TechnicalQuestionServiceImpl technicalQuestionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addTechnicalQuestion() {
        TechnicalQuestionPayload payload = TechnicalQuestionPayload.builder()
                .question("What is Java?")
                .build();
        TechnicalQuestion question = payload.toEntity();
        when(technicalQuestionRepository.save(any(TechnicalQuestion.class))).thenReturn(question);

        TechnicalQuestion result = technicalQuestionService.addTechnicalQuestion(payload);

        assertNotNull(result);
        assertEquals("What is Java?", result.getQuestion());
        verify(technicalQuestionRepository, times(1)).save(any(TechnicalQuestion.class));
    }

    @Test
    void getTechnicalQuestion() {
        TechnicalQuestion question = TechnicalQuestion.builder()
                .id(1L)
                .question("What is Java?")
                .build();
        when(technicalQuestionRepository.findById(1L)).thenReturn(Optional.of(question));

        TechnicalQuestion result = technicalQuestionService.getTechnicalQuestion(1L);

        assertNotNull(result);
        assertEquals("What is Java?", result.getQuestion());
        verify(technicalQuestionRepository, times(1)).findById(1L);
    }

    @Test
    void getTechnicalQuestion_NotFound() {
        when(technicalQuestionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> technicalQuestionService.getTechnicalQuestion(1L));
        verify(technicalQuestionRepository, times(1)).findById(1L);
    }

    @Test
    void getAllTechnicalQuestions() {
        List<TechnicalQuestion> questions = Arrays.asList(
                TechnicalQuestion.builder().id(1L).question("What is Java?").build(),
                TechnicalQuestion.builder().id(2L).question("What is Spring Boot?").build()
        );
        when(technicalQuestionRepository.findAll()).thenReturn(questions);

        List<TechnicalQuestion> result = technicalQuestionService.getAllTechnicalQuestions();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(technicalQuestionRepository, times(1)).findAll();
    }

    @Test
    void updateTechnicalQuestion() {
        TechnicalQuestion existingQuestion = TechnicalQuestion.builder()
                .id(1L)
                .question("What is Java?")
                .build();
        TechnicalQuestionPayload payload = TechnicalQuestionPayload.builder()
                .question("What is Spring Boot?")
                .build();
        when(technicalQuestionRepository.findById(1L)).thenReturn(Optional.of(existingQuestion));
        when(technicalQuestionRepository.save(any(TechnicalQuestion.class))).thenReturn(payload.toEntity(existingQuestion));

        TechnicalQuestion result = technicalQuestionService.updateTechnicalQuestion(1L, payload);

        assertNotNull(result);
        assertEquals("What is Spring Boot?", result.getQuestion());
        verify(technicalQuestionRepository, times(1)).findById(1L);
        verify(technicalQuestionRepository, times(1)).save(any(TechnicalQuestion.class));
    }

    @Test
    void deleteTechnicalQuestion() {
        TechnicalQuestion question = TechnicalQuestion.builder()
                .id(1L)
                .question("What is Java?")
                .build();
        when(technicalQuestionRepository.findById(1L)).thenReturn(Optional.of(question));

        technicalQuestionService.deleteTechnicalQuestion(1L);

        verify(technicalQuestionRepository, times(1)).findById(1L);
        verify(technicalQuestionRepository, times(1)).delete(question);
    }
}