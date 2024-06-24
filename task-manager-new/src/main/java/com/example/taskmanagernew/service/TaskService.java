package com.example.taskmanagernew.service;

import com.example.taskmanagernew.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();
    Task getTaskById(Long id);
    Task createTask(Task task);
    Task updateTask(Long id, Task taskDetails);
    boolean deleteTask(Long id);
}
