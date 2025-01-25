from fastapi import Depends, FastAPI
from sqlalchemy.ext.asyncio import AsyncSession

from src.couchdb import get_couchdb
from src.database import create_db_and_tables, get_async_session
from src.kanji.schemas import SessionData
from src.users.router import router as auth_router
from src.kanji.models import Kanji
from src.user_session import services as user_session_service
from src.celery_check.celery_tasks import add
from src.user_session import models

from minio import Minio, S3Error
import aio_pika


# from elasticsearch import Elasticsearch

app = FastAPI(title="Hanji API")


app.include_router(router=auth_router)


@app.get("/")
async def main_route():
    return {"message": "Hey, It is me Goku"}


@app.get("/rabbitmq")
async def check_rabbitmq():
    try:
        connection = await aio_pika.connect_robust("amqp://guest:guest@localhost/")

        async with connection:
            if connection:
                return "RabbitMQ is working."
    except Exception as e:
        return {"error": str(e)}


@app.get("/minio")
async def check_minio():
    try:
        client = Minio(
            "localhost:9000",
            access_key="test-access",
            secret_key="test-access",
            secure=False,
        )

        buckets = client.list_buckets()
        return {
            "message": "MinIO is working.",
            "buckets": [bucket.name for bucket in buckets],
        }

    except S3Error as e:
        return {"MinIO error": str(e)}

    except Exception as e:
        return {"MinIO error": str(e)}


# @app.get("/elasticsearch")
# async def check_elasticsearch():
#     try:
#         client = Elasticsearch(
#             "http://localhost:9200", http_auth=("elastic", "elastic")
#         )
#
#         if client.ping():
#             return {"message": "Elasticsearch is working."}
#         else:
#             return {"erorr": "Elasticsearch server is not responding."}
#     except Exception as e:
#         return {"error": str(e)}


@app.get("/add/{x}/{y}")
async def add_numbers(x: int, y: int):
    result = add.apply_async((x, y))

    result_value = result.get(timeout=10)

    return {"result": result_value}


@app.post("/")
async def create_table():
    await create_db_and_tables()
    return "created"


@app.post("/start-session/")
async def start_session(
    user_id: str, pack_id: str, db: AsyncSession = Depends(get_async_session)
):
    result = await user_session_service.start_session(user_id, pack_id, db)
    return result


@app.post("/submit-attempts/")
async def submit_attempts(
    session_id: str,
    data: SessionData,
    db: AsyncSession = Depends(get_async_session),
    couchdb=Depends(get_couchdb),
):
    await user_session_service.submit_attempts(session_id, data, db, couchdb)
    return {"message": "Session processed successfully"}
