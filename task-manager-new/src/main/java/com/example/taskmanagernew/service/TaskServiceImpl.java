package com.example.taskmanagernew.service;

import com.example.taskmanagernew.model.Task;
import com.example.taskmanagernew.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, Task taskDetails) {
        Task task = getTaskById(id);
        if (task != null) {
            task.setName(taskDetails.getName());
            task.setDescription(taskDetails.getDescription());
            task.setCompleted(taskDetails.isCompleted());
            return taskRepository.save(task);
        }
        return null;
    }

    @Override
    public boolean deleteTask(Long id) {
        Task task = getTaskById(id);
        if (task != null) {
            taskRepository.delete(task);
            return true;
        }
        return false;
    }
}
