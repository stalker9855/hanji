# import uuid
# from sqlalchemy import event
#
# from src.user_progress.models import UserProgress
# from src.users.models import User
#
#
# @event.listens_for(User, "after_insert")
# def create_user_progress(mapper, connection, target):
#     new_progress = UserProgress(
#         progress_id = uuid.uuid4(),
#         user_id=target.id,
#         error_count=0,
#     )
#
#     connection.execute(
#         UserProgress.__table__.insert().values(
#             progress_id=new_progress.progress_id,
#             user_id=new_progress.user_id,
#             error_count=new_progress.error_count
#         )
#     )
