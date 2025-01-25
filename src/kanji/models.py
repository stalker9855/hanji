from enum import Enum
from sqlalchemy import ARRAY, UUID, VARCHAR, ForeignKey, Integer, String
from sqlalchemy.orm import Mapped, mapped_column, relationship
from sqlalchemy.dialects.postgresql import ENUM as PgEnum
from src.database import Base
import uuid


class Grade(Enum):
    N5 = "N5"
    N4 = "N4"
    N3 = "N3"
    N2 = "N2"
    N1 = "N1"


class Pack(Base):
    __tablename__ = "pack"

    pack_id: Mapped[uuid.UUID] = mapped_column(UUID(as_uuid=True), primary_key=True)
    name: Mapped[str] = mapped_column(String, nullable=False)
    description: Mapped[str] = mapped_column(String, nullable=True)

    user_session: Mapped["UserSession"] = relationship(back_populates="pack")

    pack_kanji: Mapped["PackKanji"] = relationship(back_populates="pack")


class Kanji(Base):
    __tablename__ = "kanji"

    kanji_id: Mapped[int] = mapped_column(primary_key=True)
    character: Mapped[str] = mapped_column(VARCHAR(1), unique=True, nullable=False)
    svg_path: Mapped[str] = mapped_column(String, unique=True, nullable=True)
    strokes: Mapped[int] = mapped_column(Integer, nullable=False)

    grade: Mapped[Grade] = mapped_column(
        PgEnum(Grade, name="grade_level_kanji", create_type=True), nullable=False
    )
    on_yomi: Mapped[list[str]] = mapped_column(ARRAY(String), nullable=False)
    kun_yomi: Mapped[list[str]] = mapped_column(ARRAY(String), nullable=False)
    meanings: Mapped[list[str]] = mapped_column(ARRAY(String), nullable=False)

    pack_kanji: Mapped["PackKanji"] = relationship(back_populates="kanji")


class PackKanji(Base):
    __tablename__ = "pack_kanji"

    id: Mapped[uuid.UUID] = mapped_column(UUID(as_uuid=True), primary_key=True)

    pack_id: Mapped[uuid.UUID] = mapped_column(
        ForeignKey("pack.pack_id", ondelete="CASCADE")
    )
    kanji_id: Mapped[uuid.UUID] = mapped_column(
        ForeignKey("kanji.kanji_id", ondelete="CASCADE")
    )

    pack: Mapped[Pack] = relationship(back_populates="pack_kanji")

    kanji: Mapped[Kanji] = relationship(back_populates="pack_kanji")
