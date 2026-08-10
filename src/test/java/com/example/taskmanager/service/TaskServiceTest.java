package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.enums.Priority;
import com.example.taskmanager.enums.Status;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateTask() {

        // Arrange

        TaskRequest request = new TaskRequest();
        request.setTitle("Learn Spring");
        request.setDescription("Practice");
        request.setStatus(Status.PENDING);
        request.setPriority(Priority.HIGH);

        User user = new User();
        user.setId(1L);
        user.setEmail("abc@gmail.com");

        Task task = new Task();
        task.setId(1L);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setUser(user);

        TaskResponse response = new TaskResponse(
                1L,
                "Learn Spring",
                "Practice",
                Status.PENDING,
                Priority.HIGH,
                "abc@gmail.com"
        );

        when(userRepository.findByEmail("abc@gmail.com"))
                .thenReturn(Optional.of(user));

        when(taskRepository.save(any(Task.class)))
                .thenReturn(task);

        when(taskMapper.toResponse(task))
                .thenReturn(response);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName())
                .thenReturn("abc@gmail.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        // Act

        TaskResponse result = taskService.createTask(request);

        // Assert

        assertEquals("Learn Spring", result.getTitle());
        assertEquals(Status.PENDING, result.getStatus());
        assertEquals(Priority.HIGH, result.getPriority());
        assertEquals("abc@gmail.com", result.getOwnerEmail());

        verify(taskRepository, times(1)).save(any(Task.class));
        verify(taskMapper, times(1)).toResponse(task);
    }
}