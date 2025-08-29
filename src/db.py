# src/db.py
import sqlite3
from src.model import CreateTask, Task, Status

class Database:
    def __init__(self, db_file: str = "tasks.db"):
        self.conn = sqlite3.connect(db_file, check_same_thread=False)
        self.create_table()

    def create_table(self):
        query = """
        CREATE TABLE IF NOT EXISTS tasks (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL,
            description TEXT,
            status TEXT NOT NULL,
            completed BOOLEAN NOT NULL
        )
        """
        self.conn.execute(query)
        self.conn.commit()

    def add_task(self, task: CreateTask) -> Task:
        completed = task.status == Status.DONE
        cursor = self.conn.cursor()
        cursor.execute(
            "INSERT INTO tasks (title, description, status, completed) VALUES (?, ?, ?, ?)",
            (task.title, task.description, task.status, completed)
        )
        self.conn.commit()
        last = cursor.lastrowid
        
        if last is None:
            raise RuntimeError("Failed to retrieve lastrowid after insert")
        task_id = last
        data = task.model_dump(exclude={"completed"})
        return Task(id=task_id, **data, completed=completed)

    def get_tasks(self) -> list[Task]:
        cursor = self.conn.cursor()
        cursor.execute("SELECT id, title, description, status, completed FROM tasks")
        rows = cursor.fetchall()
        return [
            Task(
                id=row[0],
                title=row[1],
                description=row[2],
                status=row[3],
                completed=bool(row[4])
            )
            for row in rows
        ]

    def get_task_by_id(self, task_id: int) -> Task | None:
        cursor = self.conn.cursor()
        cursor.execute(
            "SELECT id, title, description, status, completed FROM tasks WHERE id = ?",
            (task_id,)
        )
        row = cursor.fetchone()
        if row:
            return Task(
                id=row[0],
                title=row[1],
                description=row[2],
                status=row[3],
                completed=bool(row[4])
            )
        return None

    def update_task(self, task_id: int, task: CreateTask) -> Task | None:
        completed = task.status == Status.DONE
        cursor = self.conn.cursor()
        cursor.execute(
            "UPDATE tasks SET title=?, description=?, status=?, completed=? WHERE id=?",
            (task.title, task.description, task.status, completed, task_id)
        )
        self.conn.commit()
        if cursor.rowcount:
            return Task(id=task_id, **task.model_dump(), completed=completed)
        return None

    def delete_task(self, task_id: int) -> bool:
        cursor = self.conn.cursor()
        cursor.execute("DELETE FROM tasks WHERE id=?", (task_id,))
        self.conn.commit()
        return cursor.rowcount > 0
