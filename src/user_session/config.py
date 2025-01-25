from fastapi_users import FastAPIUsers
from sqlalchemy import UUID
import redis.asyncio
from fastapi_users.authentication import (
    AuthenticationBackend,
    BearerTransport,
    RedisStrategy,
)

from . import manager, models

from src.settings import settings


bearer_transport = BearerTransport("token")

redis = redis.asyncio.from_url(settings.REDIS_URL, decode_responses=True)


def get_redis_strategy() -> RedisStrategy:
    return RedisStrategy(redis, lifetime_seconds=3600)


auth_backend = AuthenticationBackend(
    name="redis", transport=bearer_transport, get_strategy=get_redis_strategy
)


fastapi_users = FastAPIUsers[models.User, UUID](
    manager.get_user_manager, [auth_backend]
)
