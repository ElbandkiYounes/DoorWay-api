package com.doorway.Service;

import com.doorway.Exception.NotFoundException;
import com.doorway.Model.ExcellencePrinciple;
import com.doorway.Model.PrincipleQuestion;
import com.doorway.Payload.PrincipleQuestionPayload;
import com.doorway.Repository.PrincipleQuestionRepository;
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

class PrincipleQuestionServiceImplTest {

    @Mock
    private PrincipleQuestionRepository principleQuestionRepository;

    @InjectMocks
    private PrincipleQuestionServiceImpl principleQuestionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPrincipleQuestionsByPrinciple() {
        ExcellencePrinciple principle = ExcellencePrinciple.CONFIDENT_MODESTY;
        List<PrincipleQuestion> questions = Arrays.asList(
                PrincipleQuestion.builder().id(1L).principle(principle).question("Question 1").build(),
                PrincipleQuestion.builder().id(2L).principle(principle).question("Question 2").build()
        );
        when(principleQuestionRepository.findByPrinciple(principle)).thenReturn(questions);

        List<PrincipleQuestion> result = principleQuestionService.getPrincipleQuestionsByPrinciple(principle);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(principleQuestionRepository, times(1)).findByPrinciple(principle);
    }

    @Test
    void getPrincipleQuestions() {
        List<PrincipleQuestion> questions = Arrays.asList(
                PrincipleQuestion.builder().id(1L).question("Question 1").build(),
                PrincipleQuestion.builder().id(2L).question("Question 2").build()
        );
        when(principleQuestionRepository.findAll()).thenReturn(questions);

        List<PrincipleQuestion> result = principleQuestionService.getPrincipleQuestions();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(principleQuestionRepository, times(1)).findAll();
    }

    @Test
    void getPrincipleQuestion() {
        PrincipleQuestion question = PrincipleQuestion.builder()
                .id(1L)
                .question("Question 1")
                .build();
        when(principleQuestionRepository.findById(1L)).thenReturn(Optional.of(question));

        PrincipleQuestion result = principleQuestionService.getPrincipleQuestion(1L);

        assertNotNull(result);
        assertEquals("Question 1", result.getQuestion());
        verify(principleQuestionRepository, times(1)).findById(1L);
    }

    @Test
    void getPrincipleQuestion_NotFound() {
        when(principleQuestionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> principleQuestionService.getPrincipleQuestion(1L));
        verify(principleQuestionRepository, times(1)).findById(1L);
    }

    @Test
    void addPrincipleQuestion() {
        PrincipleQuestionPayload payload = PrincipleQuestionPayload.builder()
                .question("Question 1")
                .build();
        PrincipleQuestion question = payload.toEntity();
        when(principleQuestionRepository.save(any(PrincipleQuestion.class))).thenReturn(question);

        PrincipleQuestion result = principleQuestionService.addPrincipleQuestion(payload);

        assertNotNull(result);
        assertEquals("Question 1", result.getQuestion());
        verify(principleQuestionRepository, times(1)).save(any(PrincipleQuestion.class));
    }

    @Test
    void updatePrincipleQuestion() {
        PrincipleQuestion existingQuestion = PrincipleQuestion.builder()
                .id(1L)
                .question("Question 1")
                .build();
        PrincipleQuestionPayload payload = PrincipleQuestionPayload.builder()
                .question("Updated Question")
                .build();
        when(principleQuestionRepository.findById(1L)).thenReturn(Optional.of(existingQuestion));
        when(principleQuestionRepository.save(any(PrincipleQuestion.class))).thenReturn(payload.toEntity(existingQuestion));

        PrincipleQuestion result = principleQuestionService.updatePrincipleQuestion(1L, payload);

        assertNotNull(result);
        assertEquals("Updated Question", result.getQuestion());
        verify(principleQuestionRepository, times(1)).findById(1L);
        verify(principleQuestionRepository, times(1)).save(any(PrincipleQuestion.class));
    }

    @Test
    void deletePrincipleQuestion() {
        PrincipleQuestion question = PrincipleQuestion.builder()
                .id(1L)
                .question("Question 1")
                .build();
        when(principleQuestionRepository.findById(1L)).thenReturn(Optional.of(question));

        principleQuestionService.deletePrincipleQuestion(1L);

        verify(principleQuestionRepository, times(1)).findById(1L);
        verify(principleQuestionRepository, times(1)).delete(question);
    }
}