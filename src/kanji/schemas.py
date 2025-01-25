from datetime import datetime
from typing import List
from pydantic import BaseModel


class KanjiAttempt(BaseModel):
    kanji_id: int
    dtw_score: float
    errors: int
    strokes: int


class SessionData(BaseModel):
    user_id: str
    pack_id: str
    attempts: List[KanjiAttempt]

    # started_at: datetime
