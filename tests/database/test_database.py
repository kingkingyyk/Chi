from chi.database.database import Database
from chi.database.db_config import DatabaseConfig
from chi.database.model.model import User
from ..config.config_test_base import ConfigTestBase

class TestDatabase(ConfigTestBase):

    def test_database(self):
        db_config = DatabaseConfig()
        db_config._init_default()
        database = Database(db_config)
        database.reset()

        username = 'myuser'
        password = '1234abcdef'
        user = User.create(username=username, password=password)
        user.save()

        user = User.objects().all()[0]
        assert user.username == username
        assert user.password == password

        database.shutdown()