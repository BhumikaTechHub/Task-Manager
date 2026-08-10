package com.example.taskmanager.dto;

import com.example.taskmanager.enums.Priority;
import com.example.taskmanager.enums.Status;

public class TaskResponse {

    private Long id;

    private String title;

    private String description;

    private Status status;

    private Priority priority;
    private String ownerEmail;

    public TaskResponse() {
    }

    public TaskResponse(Long id,
                        String title,
                        String description,
                        Status status,
                        Priority priority, String ownerEmail) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.ownerEmail = ownerEmail;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}