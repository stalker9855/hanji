from celery import Celery


celery_app = Celery("tasks", broker="amqp://guest:guest@localhost", backend="rpc://")


celery_app.conf.update(
    task_routes={"src.celery_check.celery_tasks.add": {"queue": "addition"}}
)
