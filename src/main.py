from fastapi import FastAPI, HTTPException
from src.model import CreateTask, Task, Status


app = FastAPI()

tasks: list[Task] = []
task_id_counter = 1

@app.post("/tasks/", response_model=Task)
def create_task(task: CreateTask):
    global task_id_counter
    new_task = Task(id=task_id_counter, **task.model_dump())
    tasks.append(new_task)
    task_id_counter += 1
    return new_task

@app.get("/tasks/", response_model=list[Task])
def get_tasks():
    return tasks

@app.get("/tasks/{task_id}", response_model=Task)
def get_task(task_id: int):
    for task in tasks:
        if task.id == task_id:
            return task
    raise HTTPException(status_code=404, detail="Task not found")

@app.put("/tasks/{task_id}", response_model=Task)
def update_task(task_id: int, updated_task: CreateTask):
    for i, task in enumerate(tasks):
        if task.id == task_id:
            task_data = updated_task.model_dump()
            task_data["completed"] = task_data["status"] == Status.DONE

            new_task = Task(id=task_id, **task_data)
            tasks[i] = new_task
            return new_task
    raise HTTPException(status_code=404, detail="Task not found")

@app.delete("/tasks/{task_id}")
def delete_task(task_id: int):
    for i, task in enumerate(tasks):
        if task.id == task_id:
            tasks.pop(i)
            return {"detail": "Task deleted"}
    raise HTTPException(status_code=404, detail="Task not found")