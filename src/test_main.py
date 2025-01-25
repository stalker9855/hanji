import pytest
from httpx import AsyncClient
from .main import app
from src.couchdb import get_couchdb


@pytest.fixture(scope="function")
def couchdb_mock():
    # Mock CouchDB для тестирования
    db = {}

    def mock_get_couchdb():
        return db

    return mock_get_couchdb


@pytest.mark.asyncio
async def test_session_flow(couchdb_mock):
    async with AsyncClient(base_url="http://localhost:8000") as client:
        response = await client.post(
            "/start-session/",
            params={
                "user_id": "15a96ec9-2d41-47bb-9b88-56142887c803",
                "pack_id": "059ac414-247b-4de3-8baf-354878cbde58",
            },
        )
        assert response.status_code == 200
        session_id = response.json()["session_id"]

        attempts = [
            {"kanji_id": 1, "dtw_score": 80.0, "errors": 2, "strokes": 5},
            {"kanji_id": 2, "dtw_score": 35.0, "errors": 15, "strokes": 6},
            {"kanji_id": 3, "dtw_score": 100.0, "errors": 0, "strokes": 1},
        ]
        data = {
            "user_id": "15a96ec9-2d41-47bb-9b88-56142887c803",
            "pack_id": "059ac414-247b-4de3-8baf-354878cbde58",
            "attempts": attempts,
        }
        response = await client.post(
            f"/submit-attempts/?session_id={session_id}", json=data
        )
        print(response.text)
        print(data)
        assert response.status_code == 200
        assert response.json()["message"] == "Session processed successfully"
