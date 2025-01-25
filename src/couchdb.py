import couchdb
from src.users.models import User
from src.settings import settings


def get_couchdb():
    server = couchdb.Server(settings.COUCH_URL)
    if settings.COUCH_DB not in server:
        server.create(settings.COUCH_DB)
    return server[settings.COUCH_DB]


def create_user_progress_document(user_id: str, user_data: User):
    db = get_couchdb()
    user_docuemnt = {
        "_id": user_id,
        "user_data": user_data,
    }

    db.save(user_docuemnt)
    return user_docuemnt
