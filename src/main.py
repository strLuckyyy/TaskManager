# src/main.py
from fastapi import FastAPI, HTTPException
from src.model import CreateTask, Task
from src.db import Database

app = FastAPI()
db = Database()  # instância do SQLite

@app.post("/tasks/", response_model=Task)
def create_task(task: CreateTask):
    return db.add_task(task)

@app.get("/tasks/", response_model=list[Task])
def get_tasks():
    return db.get_tasks()

@app.get("/tasks/{task_id}", response_model=Task)
def get_task(task_id: int):
    task = db.get_task_by_id(task_id)
    if not task:
        raise HTTPException(status_code=404, detail="Task not found")
    return task

@app.put("/tasks/{task_id}", response_model=Task)
def update_task(task_id: int, updated_task: CreateTask):
    task = db.update_task(task_id, updated_task)
    if not task:
        raise HTTPException(status_code=404, detail="Task not found")
    return task

@app.delete("/tasks/{task_id}")
def delete_task(task_id: int):
    success = db.delete_task(task_id)
    if not success:
        raise HTTPException(status_code=404, detail="Task not found")
    return {"detail": "Task deleted"}
