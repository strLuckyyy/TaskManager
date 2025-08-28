from pydantic import BaseModel, field_validator
from enum import Enum

class Status(str, Enum):
    TODO = "todo"
    IN_PROGRESS = "in_progress"
    DONE = "done"

class CreateTask(BaseModel):
    @field_validator("status", mode="before")
    def _status_case_insensitive(cls, v: str | Status | None) -> Status | str | None:
        if isinstance(v, str):
            v_norm = v.lower().replace(" ", "_").replace("-", "_")
            for member in Status:
                if member.value == v_norm or member.name.lower() == v_norm:
                    return member
        return v
    
    title: str
    description: str | None = None
    status: Status = Status.TODO
    completed: bool = False

class Task(CreateTask):
    id: int
