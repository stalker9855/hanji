from src.celery_check.celery_check_test import celery_app


@celery_app.task()
def add(x, y):
    return x + y
