package com.example.taskmanager.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.taskmanager.enums.Priority;
import com.example.taskmanager.enums.Status;
import org.springframework.data.domain.Page;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
@RestController
@RequestMapping("/api/tasks")
@Tag(
        name = "Task Controller",
        description = "CRUD APIs for Task Management"
)
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @Operation(summary = "Create a new Task")
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request) {

        return new ResponseEntity<>(
                taskService.createTask(request),
                HttpStatus.CREATED
        );

    }

    @Operation(summary = "Get all Tasks")
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {

        return ResponseEntity.ok(
                taskService.getAllTasks()
        );

    }

    @Operation(summary = "Get Task By Id")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                taskService.getTaskById(id)
        );

    }
    @Operation(summary = "Update Task")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {

        return ResponseEntity.ok(
                taskService.updateTask(id, request)
        );

    }
    @Operation(summary = "Delete Task")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(
            @PathVariable Long id) {

        taskService.deleteTask(id);

        return ResponseEntity.ok("Task Deleted Successfully");

    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponse>> getByStatus(
            @PathVariable Status status) {

        return ResponseEntity.ok(
                taskService.getTasksByStatus(status)
        );

    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<TaskResponse>> getByPriority(
            @PathVariable Priority priority) {

        return ResponseEntity.ok(
                taskService.getTasksByPriority(priority)
        );

    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskResponse>> searchTask(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                taskService.searchTask(keyword)
        );

    }

    @GetMapping("/page")
    public ResponseEntity<Page<TaskResponse>> getTasks(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy

    ) {

        return ResponseEntity.ok(
                taskService.getTasks(page, size, sortBy)
        );

    }

}