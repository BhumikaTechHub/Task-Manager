package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.enums.Priority;
import com.example.taskmanager.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // Get all tasks of a user
    List<Task> findByUser(User user);

    // Get a task by id that belongs to a user
    Optional<Task> findByIdAndUser(Long id, User user);

    // Filter by status
    List<Task> findByUserAndStatus(User user, Status status);

    // Filter by priority
    List<Task> findByUserAndPriority(User user, Priority priority);

    // Search by title
    List<Task> findByUserAndTitleContainingIgnoreCase(
            User user,
            String keyword
    );

    // Pagination & Sorting
    Page<Task> findByUser(
            User user,
            Pageable pageable
    );

}