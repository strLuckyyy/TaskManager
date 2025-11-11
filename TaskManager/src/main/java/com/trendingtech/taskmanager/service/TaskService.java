package com.trendingtech.taskmanager.service;

import com.trendingtech.taskmanager.dto.TaskRequest;
import com.trendingtech.taskmanager.dto.TaskResponse;
import com.trendingtech.taskmanager.model.Task;
import com.trendingtech.taskmanager.model.User;
import com.trendingtech.taskmanager.repository.TaskRepository;
import com.trendingtech.taskmanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponse createTask(TaskRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());
        task.setUser(user);

        Task saved = taskRepository.save(task);
        return mapToResponse(saved);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());

        // opcional: permitir trocar o usuário da task
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setUser(user);
        }

        Task updated = taskRepository.save(task);
        return mapToResponse(updated);
    }

    @Transactional
    public Task updateTaskUser(Long taskId, Long newUserId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada: " + taskId));

        User user = userRepository.findById(newUserId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + newUserId));

        task.setUser(user);
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    private TaskResponse mapToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setCompleted(task.isCompleted());
        response.setUserId(task.getUser().getId());
        return response;
    }
}
