from datetime import timedelta
from dataclasses import dataclass
from flask import Flask, request, jsonify
from flask_jwt_extended import JWTManager
from flask_jwt_extended import create_access_token, create_refresh_token
from flask_jwt_extended import get_jwt_identity
from flask_jwt_extended import jwt_required
from flask_sqlalchemy import SQLAlchemy
import uuid

application = Flask(__name__)
application.config["JWT_SECRET_KEY"] = "JwE}KJ{T2%~#2DMA3T}}P~ZRwUzFGm"
application.config["JWT_ACCESS_TOKEN_EXPIRES"] = timedelta(hours=1)
application.config["JWT_REFRESH_TOKEN_EXPIRES"] = timedelta(days=7)
jwt = JWTManager(application)

user = 'u2178260_root'
password = 'u813WuaWHoi34dSd'
host = '31.31.196.166'
port = '3306'
database = 'u2178260_makeaway'


def get_connection():
    url = "mysql://{0}:{1}@{2}:{3}/{4}".format(
        user, password, host, port, database
    )
    return url


application.config['SQLALCHEMY_DATABASE_URI'] = get_connection()

db = SQLAlchemy()
db.init_app(application)


@dataclass
class User(db.Model):
    id: int
    username: str
    email: str
    password: str

    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(80), unique=True, nullable=False)
    email = db.Column(db.String(80))
    password = db.Column(db.String(100))


@dataclass
class TaskToDo(db.Model):
    id: uuid
    title: str
    is_completed: bool

    id = db.Column(db.String(36), primary_key=True, nullable=False, unique=True, default=uuid.uuid4)
    title = db.Column(db.String(1000))
    is_completed = db.Column(db.Boolean, default=False)


@dataclass
class TaskKanban(db.Model):
    id: uuid
    status: str
    title: str
    is_completed: bool

    id = db.Column(db.String(36), primary_key=True, nullable=False, unique=True, default=uuid.uuid4)
    status = db.Column(db.String(100))
    title = db.Column(db.String(1000))
    is_completed = db.Column(db.Boolean, default=False)


@dataclass
class TaskMatrix(db.Model):
    id: uuid
    category: str
    title: str
    is_completed: bool

    id = db.Column(db.String(36), primary_key=True, nullable=False, unique=True, default=uuid.uuid4)
    category = db.Column(db.String(100))
    title = db.Column(db.String(1000))
    is_completed = db.Column(db.Boolean, default=False)


@dataclass
class DesksTasksJoin(db.Model):
    id: int
    desk_id: uuid
    task_id: uuid
    user_id: uuid

    id = db.Column(db.Integer, primary_key=True, nullable=False, unique=True)
    desk_id = db.Column(db.String(36))
    task_id = db.Column(db.String(36))
    user_id = db.Column(db.String(36), nullable=False, default=uuid.uuid4)


@dataclass
class DashboardDesk(db.Model):
    id: int
    name: str
    deskType: str
    deadline: str
    description: str

    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(100))
    deskType = db.Column(db.String(40))
    deadline = db.Column(db.String(40))
    description = db.Column(db.String(255))


@dataclass
class ToDoDesk(db.Model):
    id: uuid
    name: str
    deskType: str
    deadline: str
    description: str
    user_id: uuid

    id = db.Column(db.String(36), primary_key=True, nullable=False, unique=True, default=uuid.uuid4)
    name = db.Column(db.String(100))
    deskType = db.Column(db.String(40))
    deadline = db.Column(db.String(40))
    description = db.Column(db.String(255))
    user_id = db.Column(db.String(36), nullable=False, default=uuid.uuid4)


@dataclass
class KanbanDesk(db.Model):
    id: uuid
    name: str
    deskType: str
    deadline: str
    description: str
    user_id: uuid

    id = db.Column(db.String(36), primary_key=True, nullable=False, unique=True, default=uuid.uuid4)
    name = db.Column(db.String(100))
    deskType = db.Column(db.String(40))
    deadline = db.Column(db.String(40))
    description = db.Column(db.String(255))
    user_id = db.Column(db.String(36), nullable=False, default=uuid.uuid4)


@dataclass
class MatrixDesk(db.Model):
    id: uuid
    name: str
    deskType: str
    deadline: str
    description: str
    user_id: uuid

    id = db.Column(db.String(36), primary_key=True, nullable=False, unique=True, default=uuid.uuid4)
    name = db.Column(db.String(100))
    deskType = db.Column(db.String(40))
    deadline = db.Column(db.String(40))
    description = db.Column(db.String(255))
    user_id = db.Column(db.String(36), nullable=False, default=uuid.uuid4)


@application.route("/refresh_db")
def refresh_db():
    with application.app_context():
        db.drop_all()
        db.create_all()
    return "Успешно!"


@application.route("/clear_desk_table")
def clear_desk_table():
    try:
        ToDoDesk.query.delete()
        KanbanDesk.query.delete()
        MatrixDesk.query.delete()
        return jsonify("ok")
    except Exception:
        return jsonify("...")


@application.route("/")
def main():
    return "Hello world!"


@application.route("/register", methods=["POST"])
def register():
    data = request.get_json()
    username, email, password = data.get('username'), data.get('email'), data.get('password')
    try:
        user = User(username=username, email=email, password=password)
        db.session.add(user)
        db.session.commit()

        result = {
            'access_token': create_access_token(identity=username),
            'refresh_token': create_refresh_token(identity=username)
        }

        return jsonify(result), 201
    except Exception:
        return jsonify({"msg": "test"}), 404


@application.route("/login", methods=["POST"])
def login():
    data = request.get_json()
    username, password = data.get('username'), data.get('password')

    result = {
        'access_token': create_access_token(identity=username),
        'refresh_token': create_refresh_token(identity=username)
    }

    user = User.query.filter_by(username=username).first()

    if user is not None and user.username != username or user.password != password:
        return jsonify({"msg": "Bad username or password"}), 401
    else:
        return jsonify(result), 201


@application.route("/auth/refresh-token", methods=["GET"])
@jwt_required(refresh=True)
def refresh_token():
    current_user = get_jwt_identity()
    ret = {
        'access_token': create_access_token(identity=current_user),
        'refresh_token': create_refresh_token(identity=current_user)
    }
    return jsonify(ret), 200


@application.route("/get_desks", methods=["GET"])
@jwt_required()
def get_desks():
    current_user = get_jwt_identity()
    user = User.query.filter_by(username=current_user).first()
    # print(user.id)
    result = {"items": []}
    desks = ToDoDesk.query.filter_by(user_id=user.id).all() + KanbanDesk.query.filter_by(user_id=user.id).all() + MatrixDesk.query.filter_by(user_id=user.id).all()
    print(KanbanDesk.query.filter_by(user_id=user.id).all())
    # остальные типы досок
    for desk in desks:
        desk_data = {
            "id": desk.id,
            "name": desk.name,
            "deskType": desk.deskType,
            "deadline": desk.deadline,
            "description": desk.description
        }
        result["items"].append(desk_data)
    return jsonify(result), 201


@application.route("/save_desk", methods=["POST"])
def save_desk():
    data = request.get_json()
    name, deskType, deadline, description, tasks = data.get('name'), data.get('deskType'), data.get(
        'deadline'), data.get(
        'description'), data.get('tasks')
    print(tasks)
    try:
        desk = ToDoDesk(name=name, deskType=deskType, deadline=deadline, description=description)
        db.session.add(desk)
        db.session.commit()
        return jsonify({"message": "Сохранено"}), 201
    except Exception as e:
        print(e)
        return jsonify({"message": "Ошибка загрузки"}), 404


@application.route("/users")
def get_users():
    users = User.query.all()
    return jsonify(users)


@application.route("/desks")
def get_desks_from_db():
    desks = ToDoDesk.query.all() + KanbanDesk.query.all() + MatrixDesk.query.all()
    print(KanbanDesk.query.all())
    return jsonify(desks)


@application.route("/desks/todo")
def get_todo_desks_from_db():
    desks = ToDoDesk.query.all()
    return jsonify(desks)


@application.route("/desks/kanban")
def get_kanban_desks_from_db():
    desks = KanbanDesk.query.all()
    return jsonify(desks)


@application.route("/tasks")
def get_tasks_from_db():
    desks = TaskToDo.query.all()
    return jsonify(desks)


@application.route("/desks/todo/<id>")
@jwt_required()
def get_todo_tasks(id):
    current_user = get_jwt_identity()
    user = User.query.filter_by(username=current_user).first()
    tasksIds = [item.task_id for item in DesksTasksJoin.query.filter_by(desk_id=id, user_id=user.id).all()]
    tasks = TaskToDo.query.filter(TaskToDo.id.in_(tasksIds)).all()
    response = []
    for task in tasks:
        response.append({"id": task.id, "title": task.title, "is_completed": task.is_completed})
    return jsonify(response)


@application.route("/desk/todo", methods=["POST"])
@jwt_required()
def save_todo_desk():
    current_user = get_jwt_identity()
    user = User.query.filter_by(username=current_user).first()
    data = request.get_json()
    name, deskType, deadline, description, tasks = data.get('name'), data.get('deskType'), data.get(
        'deadline'), data.get(
        'description'), data.get('tasks')
    print("DESKTYPE " + deskType)
    try:
        desk = ToDoDesk(name=name, deskType=deskType, deadline=deadline, description=description, user_id=user.id)
        db.session.add(desk)
        db.session.commit()
        if tasks is not None:
            for task in tasks:
                tmp = TaskToDo(title=task['title'])
                db.session.add(tmp)
                desksTasks = DesksTasksJoin(desk_id=desk.id, task_id=tmp.id, user_id=user.id)
                db.session.add(desksTasks)
                db.session.commit()
            return jsonify({"id": desk.id}), 201
        else:
            return jsonify({"id": desk.id}), 201
    except Exception as e:
        return jsonify({"message": e}), 404


@application.route("/desks/kanban/<id>")
@jwt_required()
def get_kanban_tasks(id):
    current_user = get_jwt_identity()
    user = User.query.filter_by(username=current_user).first()
    tasksIds = [item.task_id for item in DesksTasksJoin.query.filter_by(desk_id=id, user_id=user.id).all()]
    tasks = TaskKanban.query.filter(TaskKanban.id.in_(tasksIds)).all()
    response = []
    for task in tasks:
        response.append({"id": task.id, "title": task.title,
                         "status": task.status, "is_completed": task.is_completed})
    return jsonify(response)


@application.route("/desk/kanban", methods=["POST"])
@jwt_required()
def save_kanban_desk():
    current_user = get_jwt_identity()
    user = User.query.filter_by(username=current_user).first()
    data = request.get_json()
    name, deskType, deadline, description, tasks = data.get('name'), data.get('deskType'), data.get(
        'deadline'), data.get(
        'description'), data.get('tasksKanban')
    try:
        desk = KanbanDesk(name=name, deskType=deskType, deadline=deadline, description=description, user_id=user.id)
        db.session.add(desk)
        db.session.commit()
        if tasks is not None:
            for task in tasks:
                tmp = TaskKanban(title=task['title'], status=task['status'])
                db.session.add(tmp)
                desksTasks = DesksTasksJoin(desk_id=desk.id, task_id=tmp.id, user_id=user.id)
                db.session.add(desksTasks)
                db.session.commit()
            return jsonify({"id": desk.id}), 201
        else:
            return jsonify({"id": desk.id}), 201
    except Exception as e:
        return jsonify({"message": e}), 404


@application.route("/desks/matrix/<id>")
@jwt_required()
def get_matrix_tasks(id):
    current_user = get_jwt_identity()
    user = User.query.filter_by(username=current_user).first()
    tasksIds = [item.task_id for item in DesksTasksJoin.query.filter_by(desk_id=id, user_id=user.id).all()]
    tasks = TaskMatrix.query.filter(TaskMatrix.id.in_(tasksIds)).all()
    response = []
    for task in tasks:
        response.append({"id": task.id, "title": task.title,
                         "category": task.category, "is_completed": task.is_completed})
    return jsonify(response)


@application.route("/desk/matrix", methods=["POST"])
@jwt_required()
def save_matrix_desk():
    current_user = get_jwt_identity()
    user = User.query.filter_by(username=current_user).first()
    data = request.get_json()
    name, deskType, deadline, description, tasks = data.get('name'), data.get('deskType'), data.get(
        'deadline'), data.get(
        'description'), data.get('tasksMatrix')
    try:
        desk = MatrixDesk(name=name, deskType=deskType, deadline=deadline, description=description, user_id=user.id)
        db.session.add(desk)
        db.session.commit()
        if tasks is not None:
            for task in tasks:
                tmp = TaskMatrix(title=task['title'], category=task['category'])
                db.session.add(tmp)
                desksTasks = DesksTasksJoin(desk_id=desk.id, task_id=tmp.id, user_id=user.id)
                db.session.add(desksTasks)
                db.session.commit()
            return jsonify({"id": desk.id}), 201
        else:
            return jsonify({"id": desk.id}), 201
    except Exception as e:
        return jsonify({"message": e}), 404


@application.route("/user/info", methods=["GET"])
@jwt_required()
def get_user_info():
    try:
        current_user = get_jwt_identity()
        user = User.query.filter_by(username=current_user).first()
        result = {
            "username": user.username,
            "email": user.email
        }
        return jsonify(result), 201
    except Exception as e:
        print(e)
        return jsonify({"msg": "test"}), 404
    
@application.route("/tasks/<id>", methods=["POST"])
@jwt_required()
def mark_task_as(id):
    try:
        data = request.get_json()
        is_completed = data.get("is_completed")

        current_user = get_jwt_identity()
        user = User.query.filter_by(username=current_user).first()
        tasksIds = [item.task_id for item in DesksTasksJoin.query.filter_by(user_id=user.id).all()]
        print(tasksIds)
        todo = TaskToDo.query.filter(TaskToDo.id.in_(tasksIds)).all()
        kanban = TaskKanban.query.filter(TaskKanban.id.in_(tasksIds)).all()
        matrix = TaskMatrix.query.filter(TaskMatrix.id.in_(tasksIds)).all()

        tasks = todo + kanban + matrix
        print(tasks)
        task = list(filter(lambda task: task.id == id, tasks))

        task[0].is_completed = is_completed
        db.session.commit()
        return jsonify({"result": "success!"})

    except Exception as e:
        print(e)
        return jsonify({"msg": "test"}), 404


if __name__ == "__main__":
    application.run(host='0.0.0.0', debug=True)
