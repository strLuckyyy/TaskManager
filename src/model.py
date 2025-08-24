from pydantic import BaseModel
from enum import Enum

class Status(str, Enum):
    TODO = "todo"
    IN_PROGRESS = "in_progress"
    DONE = "done"

class CreateTask(BaseModel):
    title: str
    description: str | None = None
    status: Status = Status.TODO    
    completed: bool = False

class Task(CreateTask):
    id: int