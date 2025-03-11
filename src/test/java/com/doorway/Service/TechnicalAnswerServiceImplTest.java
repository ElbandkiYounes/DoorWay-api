package com.doorway.Service;

import com.doorway.Exception.NotFoundException;
import com.doorway.Model.Interview;
import com.doorway.Model.Language;
import com.doorway.Model.TechnicalAnswer;
import com.doorway.Model.TechnicalQuestion;
import com.doorway.Payload.TechnicalAnswerPayload;
import com.doorway.Repository.TechnicalAnswerRepository;
import com.doorway.Service.Interface.InterviewService;
import com.doorway.Service.Interface.TechnicalQuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnicalAnswerServiceImplTest {

    @Mock
    private TechnicalAnswerRepository technicalAnswerRepository;

    @Mock
    private TechnicalQuestionService technicalQuestionService;

    @Mock
    private InterviewService interviewService;

    @InjectMocks
    private TechnicalAnswerServiceImpl technicalAnswerService;

    private TechnicalAnswerPayload technicalAnswerPayload;
    private TechnicalAnswer technicalAnswer;
    private TechnicalQuestion technicalQuestion;
    private Interview interview;
    private UUID interviewId;
    private Long technicalQuestionId;
    private Long technicalAnswerId;

    @BeforeEach
    void setUp() {
        interviewId = UUID.randomUUID();
        technicalQuestionId = 1L;
        technicalAnswerId = 10L;

        technicalQuestion = TechnicalQuestion.builder().id(technicalQuestionId).build();
        interview = Interview.builder().id(interviewId).build();

        technicalAnswer = TechnicalAnswer.builder()
                .id(technicalAnswerId)
                .question(technicalQuestion)
                .interview(interview)
                .build();

        technicalAnswerPayload = TechnicalAnswerPayload.builder()
                .answer("Test Answer")
                .language(Language.JAVA)
                .build();
    }

    // ✅ Test: Create Technical Answer - Success
    @Test
    void testCreateTechnicalAnswer_Success() {
        when(technicalQuestionService.getTechnicalQuestion(technicalQuestionId)).thenReturn(technicalQuestion);
        when(interviewService.getInterviewById(interviewId)).thenReturn(interview);
        when(technicalAnswerRepository.findByQuestionIdAndInterviewId(technicalQuestionId, interviewId))
                .thenReturn(Optional.empty());
        when(technicalAnswerRepository.save(any())).thenReturn(technicalAnswer);

        TechnicalAnswer result = technicalAnswerService.createTechnicalAnswer(technicalAnswerPayload, technicalQuestionId, interviewId);

        assertNotNull(result);
        assertEquals(technicalAnswerId, result.getId());
        verify(technicalAnswerRepository, times(1)).save(any());
    }

    // ❌ Test: Create Technical Answer - Conflict
    @Test
    void testCreateTechnicalAnswer_ThrowsExceptionIfAlreadyExists() {
        when(technicalQuestionService.getTechnicalQuestion(technicalQuestionId)).thenReturn(technicalQuestion);
        when(interviewService.getInterviewById(interviewId)).thenReturn(interview);
        when(technicalAnswerRepository.findByQuestionIdAndInterviewId(technicalQuestionId, interviewId))
                .thenReturn(Optional.of(technicalAnswer));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                technicalAnswerService.createTechnicalAnswer(technicalAnswerPayload, technicalQuestionId, interviewId)
        );

        assertEquals("An answer for this question already exists in this interview.", exception.getMessage());
        verify(technicalAnswerRepository, never()).save(any());
    }

    // ✅ Test: Get Technical Answer by ID - Success
    @Test
    void testGetTechnicalAnswerById_Success() {
        when(technicalAnswerRepository.findById(technicalAnswerId)).thenReturn(Optional.of(technicalAnswer));

        TechnicalAnswer result = technicalAnswerService.getTechnicalAnswerById(technicalAnswerId);

        assertNotNull(result);
        assertEquals(technicalAnswerId, result.getId());
    }

    // ❌ Test: Get Technical Answer by ID - Not Found
    @Test
    void testGetTechnicalAnswerById_ThrowsExceptionIfNotFound() {
        when(technicalAnswerRepository.findById(technicalAnswerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () ->
                technicalAnswerService.getTechnicalAnswerById(technicalAnswerId)
        );

        assertEquals("Technical Answer not found", exception.getMessage());
    }

    // ✅ Test: Update Technical Answer - Success
    @Test
    void testUpdateTechnicalAnswer_Success() {
        when(technicalAnswerRepository.findById(technicalAnswerId)).thenReturn(Optional.of(technicalAnswer));
        when(technicalQuestionService.getTechnicalQuestion(technicalQuestionId)).thenReturn(technicalQuestion);
        when(interviewService.getInterviewById(interviewId)).thenReturn(interview);

        TechnicalAnswer updatedAnswer = TechnicalAnswer.builder()
                .id(technicalAnswerId)
                .question(technicalQuestion)
                .interview(interview)
                .answer(technicalAnswerPayload.getAnswer())
                .language(technicalAnswerPayload.getLanguage())
                .build();

        when(technicalAnswerRepository.save(any())).thenReturn(updatedAnswer);

        TechnicalAnswer result = technicalAnswerService.updateTechnicalAnswer(technicalQuestionId, technicalAnswerPayload, technicalAnswerId, interviewId);

        assertNotNull(result);
        assertEquals(technicalAnswerId, result.getId());
        verify(technicalAnswerRepository, times(1)).save(any());
    }

    // ❌ Test: Update Technical Answer - Not Found
    @Test
    void testUpdateTechnicalAnswer_ThrowsExceptionIfNotFound() {
        when(technicalAnswerRepository.findById(technicalAnswerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () ->
                technicalAnswerService.updateTechnicalAnswer(technicalQuestionId, technicalAnswerPayload, technicalAnswerId, interviewId)
        );

        assertEquals("Technical Answer not found", exception.getMessage());
        verify(technicalAnswerRepository, never()).save(any());
    }

    // ✅ Test: Delete Technical Answer - Success
    @Test
    void testDeleteTechnicalAnswer_Success() {
        when(technicalAnswerRepository.findById(technicalAnswerId)).thenReturn(Optional.of(technicalAnswer));

        technicalAnswerService.deleteTechnicalAnswer(technicalAnswerId);

        verify(technicalAnswerRepository, times(1)).deleteById(technicalAnswerId);
    }

    // ❌ Test: Delete Technical Answer - Not Found
    @Test
    void testDeleteTechnicalAnswer_ThrowsExceptionIfNotFound() {
        when(technicalAnswerRepository.findById(technicalAnswerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () ->
                technicalAnswerService.deleteTechnicalAnswer(technicalAnswerId)
        );

        assertEquals("Technical Answer not found", exception.getMessage());
        verify(technicalAnswerRepository, never()).deleteById(any());
    }
}
