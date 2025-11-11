package com.trendingtech.taskmanager.controller;

import com.trendingtech.taskmanager.dto.TaskRequest;
import com.trendingtech.taskmanager.dto.TaskResponse;
import com.trendingtech.taskmanager.model.Task;
import com.trendingtech.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    /* JSON TASK - create e update
    * http://localhost:8080/api/tasks - /id para pesquisas por id
{
  "title": "nome task",
  "description": "Descrição task",
  "completed": boolean,
  "userId": id de um usuario (ex: 1)
}
    */

    // CREATE
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest taskRequest) {
        TaskResponse createdTask = taskService.createTask(taskRequest);
        return ResponseEntity.ok(createdTask);
    }

    // READ - by id
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    // READ - get all tasks
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @RequestBody TaskRequest taskRequest
    ) {
        TaskResponse updatedTask = taskService.updateTask(id, taskRequest);
        return ResponseEntity.ok(updatedTask);
    }

    // UPDATE USER
    /* ex: PUT http://localhost:8080/api/tasks/5/user
{
    "userId": 2
}
    * */
    @PutMapping("/{taskId}/user")
    public ResponseEntity<Task> updateTaskUser(
            @PathVariable Long taskId,
            @RequestBody Map<String, Long> requestBody) {

        Long newUserId = requestBody.get("userId");
        Task updatedTask = taskService.updateTaskUser(taskId, newUserId);
        return ResponseEntity.ok(updatedTask);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
