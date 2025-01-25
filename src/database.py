from collections.abc import AsyncGenerator
from sqlalchemy import String
from sqlalchemy.ext.asyncio import AsyncSession, async_sessionmaker, create_async_engine
from sqlalchemy.orm import DeclarativeBase
from typing import Annotated
from src.settings import settings
import couchdb


engine = create_async_engine(url=settings.DATABASE_URL, echo=True)

async_session_factory = async_sessionmaker(engine)

str_256 = Annotated[str, 256]


class Base(DeclarativeBase):
    type_annotation_map = {str_256: String(256)}


async def create_db_and_tables():
    async with engine.begin() as conn:
        print(Base.metadata.tables)
        await conn.run_sync(Base.metadata.create_all)


async def get_async_session() -> AsyncGenerator[AsyncSession, None]:
    async with async_session_factory() as session:
        yield session
