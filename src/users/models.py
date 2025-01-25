from fastapi_users.db import SQLAlchemyBaseUserTableUUID
from sqlalchemy import UUID, VARCHAR, DateTime, ForeignKey
from sqlalchemy.dialects.postgresql import JSONB
from sqlalchemy.sql import func
from sqlalchemy.orm import Mapped, mapped_column, relationship
from src.database import Base
import uuid


class User(SQLAlchemyBaseUserTableUUID, Base):
    __tablename__ = "user"

    user_log: Mapped["UserLog"] = relationship(back_populates="user")

    user_session: Mapped["UserSession"] = relationship(back_populates="user")


class UserLog(Base):
    __tablename__ = "user_log"

    log_id: Mapped[uuid.UUID] = mapped_column(UUID(as_uuid=True), primary_key=True)
    user_id: Mapped[uuid.UUID] = mapped_column(
        ForeignKey("user.id", ondelete="CASCADE")
    )
    action: Mapped[str] = mapped_column(VARCHAR(50), nullable=False)
    log_time: Mapped[DateTime] = mapped_column(
        DateTime, nullable=False, server_default=func.now()
    )

    old_data: Mapped[dict] = mapped_column(JSONB, nullable=True)
    new_data: Mapped[dict] = mapped_column(JSONB, nullable=True)

    user: Mapped["User"] = relationship(back_populates="user_log")
