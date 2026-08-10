package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.enums.Priority;
import com.example.taskmanager.enums.Status;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private static final Logger logger =
            LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskService(
            TaskRepository taskRepository,
            UserRepository userRepository,
            TaskMapper taskMapper
    ) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    /**
     * Returns the currently authenticated user.
     */
    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    /**
     * Create a new task.
     */
    public TaskResponse createTask(TaskRequest request) {

        User user = getCurrentUser();

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setUser(user);

        Task savedTask = taskRepository.save(task);

        logger.info(
                "Task '{}' created by {}",
                savedTask.getTitle(),
                user.getEmail()
        );

        return taskMapper.toResponse(savedTask);
    }

    /**
     * Get all tasks of the logged-in user.
     */
    public List<TaskResponse> getAllTasks() {

        User user = getCurrentUser();

        return taskRepository.findByUser(user)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    /**
     * Get task by ID.
     */
    public TaskResponse getTaskById(Long id) {

        User user = getCurrentUser();

        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id : " + id));

        return taskMapper.toResponse(task);
    }

    /**
     * Update task.
     */
    public TaskResponse updateTask(Long id, TaskRequest request) {

        User user = getCurrentUser();

        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id : " + id));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());

        Task updatedTask = taskRepository.save(task);

        logger.info(
                "Task {} updated by {}",
                updatedTask.getId(),
                user.getEmail()
        );

        return taskMapper.toResponse(updatedTask);
    }

    /**
     * Delete task.
     */
    public void deleteTask(Long id) {

        User user = getCurrentUser();

        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id : " + id));

        logger.warn(
                "Task {} deleted by {}",
                task.getId(),
                user.getEmail()
        );

        taskRepository.delete(task);
    }

    /**
     * Filter tasks by status.
     */
    public List<TaskResponse> getTasksByStatus(Status status) {

        User user = getCurrentUser();

        return taskRepository.findByUserAndStatus(user, status)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    /**
     * Filter tasks by priority.
     */
    public List<TaskResponse> getTasksByPriority(Priority priority) {

        User user = getCurrentUser();

        return taskRepository.findByUserAndPriority(user, priority)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    /**
     * Search tasks by title.
     */
    public List<TaskResponse> searchTask(String keyword) {

        User user = getCurrentUser();

        return taskRepository.findByUserAndTitleContainingIgnoreCase(user, keyword)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    /**
     * Get paginated tasks.
     */
    public Page<TaskResponse> getTasks(
            int page,
            int size,
            String sortBy
    ) {

        User user = getCurrentUser();

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy)
        );

        return taskRepository.findByUser(user, pageable)
                .map(taskMapper::toResponse);
    }

}