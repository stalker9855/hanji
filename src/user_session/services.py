from datetime import datetime, timedelta, timezone
from uuid import uuid4
from fastapi import HTTPException
from fastapi.encoders import jsonable_encoder

from sqlalchemy import insert, select, update
from sqlalchemy.ext.asyncio import AsyncSession

from src.kanji.schemas import SessionData
from src.user_session.models import UserSession


async def start_session(user_id: str, pack_id: str, db: AsyncSession):
    query = (
        insert(UserSession)
        .values(session_id=uuid4(), user_id=user_id, pack_id=pack_id)
        .returning(UserSession.session_id)
    )
    result = await db.execute(query)
    session_id = result.scalar()
    await db.commit()
    return {"session_id": session_id}


async def submit_attempts(
    session_id: str, data: SessionData, db: AsyncSession, couchdb
):
    query = select(UserSession).where(UserSession.session_id == session_id)

    result = await db.execute(query)
    if not result.scalar():
        raise HTTPException(status_code=404, detail="Session not found")

    user_progress = couchdb.get(data.user_id) or {
        "_id": data.user_id,
        "kanji_progress": {},
    }
    if "kanji_progress" not in user_progress:
        user_progress["kanji_progress"] = {}

    for attempt in data.attempts:
        pack_data = user_progress["kanji_progress"].setdefault(data.pack_id, {})
        kanji_data = pack_data.setdefault(
            attempt.kanji_id,
            {
                "attempts": 0,
                "errors": 0,
                "srs": {
                    "srs_level": 1,
                    "ease_factor": 2.5,
                    "next_review": datetime.now(timezone.utc).isoformat(),
                },
            },
        )

        kanji_data["attempts"] += 1
        kanji_data["errors"] += attempt.errors

        grade = 5 if attempt.dtw_score > 90 else 3 if attempt.dtw_score > 70 else 1
        interval = kanji_data["srs"]["srs_level"] * kanji_data["srs"]["ease_factor"]
        kanji_data["srs"]["srs_level"] = max(
            1, kanji_data["srs"]["srs_level"] + (1 if grade > 3 else -1)
        )
        kanji_data["srs"]["ease_factor"] = max(
            1.3, kanji_data["srs"]["ease_factor"] + (0.1 - (5 - grade) * 0.02)
        )
        next_review = jsonable_encoder(
            datetime.now(timezone.utc) + timedelta(days=int(interval))
        )
        kanji_data["srs"]["next_review"] = next_review

    couchdb.save(user_progress)

    query = (
        update(UserSession)
        .where(UserSession.session_id == session_id)
        .values(completed_at=datetime.utcnow(), session_id=session_id, completed=True)
    )
    await db.execute(query)
    await db.commit()

    return {"message": "Session processed successfully"}
