from sqlalchemy import UUID, Boolean, DateTime, ForeignKey
from sqlalchemy.sql import func
from sqlalchemy.orm import Mapped, mapped_column, relationship
from src.database import Base
import uuid

from src.kanji.models import Pack
from src.users.models import User


class UserSession(Base):
    __tablename__ = "user_session"

    session_id: Mapped[uuid.UUID] = mapped_column(UUID(as_uuid=True), primary_key=True)

    user_id: Mapped[uuid.UUID] = mapped_column(
        ForeignKey("user.id", ondelete="CASCADE")
    )
    pack_id: Mapped[uuid.UUID] = mapped_column(
        ForeignKey("pack.pack_id", ondelete="CASCADE")
    )

    started_at: Mapped[DateTime] = mapped_column(
        DateTime, server_default=func.now(), nullable=False
    )
    completed_at: Mapped[DateTime] = mapped_column(DateTime, nullable=True)

    completed: Mapped[Boolean] = mapped_column(Boolean, default=False)

    user: Mapped[User] = relationship(back_populates="user_session")

    pack: Mapped[Pack] = relationship(back_populates="user_session")
