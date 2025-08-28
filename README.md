# Task Manager API (FastAPI + SQLite)

Uma API simples de gerenciamento de tarefas desenvolvida com **FastAPI** e **Python**, com persistência de dados via **SQLite**.  
Permite criar, listar, atualizar, deletar tarefas e status de conclusão.

---

## 📂 Estrutura do projeto
```
src/
│── main.py       # Endpoints da API (CRUD)
│── model.py      # Modelos Pydantic: Task, CreateTask, Status
│── db.py         # Classe Database para SQLite
tasks.db          # Database local criado
```

- **main.py** → contém todos os endpoints:
  - `POST /tasks/` → criar tarefa
  - `GET /tasks/` → listar todas as tarefas
  - `GET /tasks/{task_id}` → buscar tarefa por ID
  - `PUT /tasks/{task_id}` → atualizar tarefa
  - `DELETE /tasks/{task_id}` → deletar tarefa

- **model.py** → define os modelos de dados:
  - `Status` → Enum com valores: `todo`, `in_progress`, `done`
  - `CreateTask` → modelo de entrada (sem `id`)
  - `Task` → modelo completo (com `id`) usado nas respostas

- **db.py** → contém a classe `Database` que gerencia o SQLite:
  - Cria a tabela `tasks` se não existir
  - Métodos de CRUD (`add_task`, `get_tasks`, `get_task_by_id`, `update_task`, `delete_task`)

---

## ⚡ Instalação

1. Clone o repositório:
```bash
git clone https://github.com/strLuckyyy/TaskManager.git
cd TaskManager
```

2. Crie um ambiente virtual (opcional mas recomendado):
```
python -m venv venv

source venv/bin/activate   # Linux/Mac
venv\Scripts\activate      # Windows
```

3. Instale as dependências:
```
pip install fastapi uvicorn
```

---

## 🚀 Rodando a API localmente
```
uvicorn src.main:app --reload --port 8001
```
* O servidor ficará disponível em: http://127.0.0.1:8001
* Documentação interativa Swagger UI: http://127.0.0.1:8001/docs
* Documentação Redoc: http://127.0.0.1:8001/redoc

---

## 📌 Testando a API
### Criar tarefa

```
POST /tasks/
{
  "title": "Teste",
  "description": "Fazendo teste",
  "status": "todo",
  "completed": false
}
```
Resposta esperada:
```
{
  "id": 1,
  "title": "Teste",
  "description": "Fazendo teste",
  "status": "todo",
  "completed": false
}
```
* Se status for "done", o campo "completed" será True automaticamente.
* Deve haver o status "completed" independente

### Listar tarefas
```
GET /tasks/
```
* Retorna lista de todas as tarefas.

### Buscar tarefa por ID
```
GET /tasks/1
```
* Retorna a tarefa específica ou erro 404 se não existir.

### Atualizar tarefa
```
PUT /tasks/1
{
  "title": "Teste Update",
  "description": "Updating the task",
  "status": "done",
  "completed": "true"
}
```
* Atualiza a tarefa. Se status = "done", "completed" vira True automaticamente.

### Deletar tarefa
```
DELETE /tasks/1
```
* Remove a tarefa com o ID especificado. Retorna erro 404 se não existir.

---

## ⚡ Chamando a API do Frontend

O frontend pode consumir a API via requisições HTTP (fetch, axios, curl, PHP file_get_contents etc.).

Exemplo de requisição em JS (axios):
```
axios.post('http://127.0.0.1:8000/tasks/', {
  title: 'Nova tarefa',
  description: 'Descrição da tarefa',
  status: 'todo',
  completed: false
}).then(res => console.log(res.data))
```

### Todos os endpoints retornam JSON com os campos:
* id → número da tarefa
* title → título
* description → descrição
* status → enum (todo, in_progress, done)
* completed → bool

---
