from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    POSTGRES_HOST: str
    POSTGRES_PORT: int
    POSTGRES_USER: str
    POSTGRES_PASSWORD: str
    POSTGRES_DB: str

    REDIS_HOST: str
    REDIS_PORT: int

    COUCH_USERNAME: str
    COUCH_PASSWORD: str
    COUCH_PORT: str
    COUCH_HOST: str
    COUCH_DB: str

    @property
    def DATABASE_URL(self):
        return f"postgresql+asyncpg://{self.POSTGRES_USER}:{self.POSTGRES_PASSWORD}@{self.POSTGRES_HOST}:{self.POSTGRES_PORT}/{self.POSTGRES_DB}"

    @property
    def REDIS_URL(self):
        return f"redis://{self.REDIS_HOST}:{self.REDIS_PORT}"

    @property
    def COUCH_URL(self):
        return f"http://{self.COUCH_USERNAME}:{self.COUCH_PASSWORD}@{self.COUCH_HOST}:{self.COUCH_PORT}"

    model_config = SettingsConfigDict(env_file=".env")


settings = Settings()
