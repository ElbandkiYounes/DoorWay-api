package com.doorway.Service;

import com.doorway.Exception.NotFoundException;
import com.doorway.Model.Interview;
import com.doorway.Model.PrincipleAnswer;
import com.doorway.Model.PrincipleQuestion;
import com.doorway.Payload.PrincipleAnswerPayload;
import com.doorway.Repository.PrincipleAnswerRepository;
import com.doorway.Service.Interface.PrincipleQuestionService;
import com.doorway.Service.Interface.InterviewService;
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
class PrincipleAnswerServiceImplTest {

    @Mock
    private PrincipleAnswerRepository principleAnswerRepository;

    @Mock
    private PrincipleQuestionService principleQuestionService;

    @Mock
    private InterviewService interviewService;

    @InjectMocks
    private PrincipleAnswerServiceImpl principleAnswerService;

    private PrincipleAnswerPayload principleAnswerPayload;
    private PrincipleAnswer principleAnswer;
    private PrincipleQuestion principleQuestion;
    private Interview interview;
    private UUID interviewId;
    private Long principleQuestionId;
    private Long principleAnswerId;

    @BeforeEach
    void setUp() {
        interviewId = UUID.randomUUID();
        principleQuestionId = 1L;
        principleAnswerId = 10L;

        principleQuestion = PrincipleQuestion.builder().id(principleQuestionId).build();
        interview = Interview.builder().id(interviewId).build();

        principleAnswer = PrincipleAnswer.builder()
                .id(principleAnswerId)
                .question(principleQuestion)
                .interview(interview)
                .build();

        principleAnswerPayload = PrincipleAnswerPayload.builder()
                .answer("Test Answer")
                .build();
    }

    // ✅ Test: Create Principle Answer - Success
    @Test
    void testCreatePrincipleAnswer_Success() {
        when(principleQuestionService.getPrincipleQuestion(principleQuestionId)).thenReturn(principleQuestion);
        when(interviewService.getInterviewById(interviewId)).thenReturn(interview);
        when(principleAnswerRepository.findByQuestionIdAndInterviewId(principleQuestionId, interviewId))
                .thenReturn(Optional.empty());
        when(principleAnswerRepository.save(any())).thenReturn(principleAnswer);

        PrincipleAnswer result = principleAnswerService.createPrincipleAnswer(principleAnswerPayload, interviewId, principleQuestionId);

        assertNotNull(result);
        assertEquals(principleAnswerId, result.getId());
        verify(principleAnswerRepository, times(1)).save(any());
    }

    // ❌ Test: Create Principle Answer - Conflict
    @Test
    void testCreatePrincipleAnswer_ThrowsExceptionIfAlreadyExists() {
        when(principleQuestionService.getPrincipleQuestion(principleQuestionId)).thenReturn(principleQuestion);
        when(interviewService.getInterviewById(interviewId)).thenReturn(interview);
        when(principleAnswerRepository.findByQuestionIdAndInterviewId(principleQuestionId, interviewId))
                .thenReturn(Optional.of(principleAnswer));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                principleAnswerService.createPrincipleAnswer(principleAnswerPayload, interviewId, principleQuestionId)
        );

        assertEquals("An answer for this question already exists in this interview.", exception.getMessage());
        verify(principleAnswerRepository, never()).save(any());
    }

    // ✅ Test: Get Principle Answer by ID - Success
    @Test
    void testGetPrincipleAnswerById_Success() {
        when(principleAnswerRepository.findById(principleAnswerId)).thenReturn(Optional.of(principleAnswer));

        PrincipleAnswer result = principleAnswerService.getPrincipleAnswerById(principleAnswerId);

        assertNotNull(result);
        assertEquals(principleAnswerId, result.getId());
    }

    // ❌ Test: Get Principle Answer by ID - Not Found
    @Test
    void testGetPrincipleAnswerById_ThrowsExceptionIfNotFound() {
        when(principleAnswerRepository.findById(principleAnswerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () ->
                principleAnswerService.getPrincipleAnswerById(principleAnswerId)
        );

        assertEquals("PrincipleAnswer not found", exception.getMessage());
    }

    // ✅ Test: Update Principle Answer - Success
    @Test
    void testUpdatePrincipleAnswer_Success() {
        when(principleAnswerRepository.findById(principleAnswerId)).thenReturn(Optional.of(principleAnswer));
        when(principleQuestionService.getPrincipleQuestion(principleQuestionId)).thenReturn(principleQuestion);
        when(interviewService.getInterviewById(interviewId)).thenReturn(interview);

        PrincipleAnswer updatedAnswer = PrincipleAnswer.builder()
                .id(principleAnswerId)
                .question(principleQuestion)
                .interview(interview)
                .answer(principleAnswerPayload.getAnswer())
                .build();

        when(principleAnswerRepository.save(any())).thenReturn(updatedAnswer);

        PrincipleAnswer result = principleAnswerService.updatePrincipleAnswer(principleAnswerId, principleAnswerPayload, interviewId, principleQuestionId);

        assertNotNull(result);
        assertEquals(principleAnswerId, result.getId());
        verify(principleAnswerRepository, times(1)).save(any());
    }

    // ❌ Test: Update Principle Answer - Not Found
    @Test
    void testUpdatePrincipleAnswer_ThrowsExceptionIfNotFound() {
        when(principleAnswerRepository.findById(principleAnswerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () ->
                principleAnswerService.updatePrincipleAnswer(principleAnswerId, principleAnswerPayload, interviewId, principleQuestionId)
        );

        assertEquals("PrincipleAnswer not found", exception.getMessage());
        verify(principleAnswerRepository, never()).save(any());
    }

    // ✅ Test: Delete Principle Answer - Success
    @Test
    void testDeletePrincipleAnswer_Success() {
        when(principleAnswerRepository.findById(principleAnswerId)).thenReturn(Optional.of(principleAnswer));

        principleAnswerService.deletePrincipleAnswer(principleAnswerId);

        verify(principleAnswerRepository, times(1)).deleteById(principleAnswerId);
    }

    // ❌ Test: Delete Principle Answer - Not Found
    @Test
    void testDeletePrincipleAnswer_ThrowsExceptionIfNotFound() {
        when(principleAnswerRepository.findById(principleAnswerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () ->
                principleAnswerService.deletePrincipleAnswer(principleAnswerId)
        );

        assertEquals("PrincipleAnswer not found", exception.getMessage());
        verify(principleAnswerRepository, never()).deleteById(any());
    }
}
