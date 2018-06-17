from ...config.config_test_base import ConfigTestBase
from chi.database.database import Database
from chi.database.db_config import DatabaseConfig


class TestModelBase(ConfigTestBase):

    def setup_method(self, method):
        db_config = DatabaseConfig()
        db_config._init_default()

        self.database = Database(db_config)
        self.database.truncate()

        self.create_model_object()

    def teardown_method(self, method):
        self.database.shutdown()

    def create_model_object(self):
        pass