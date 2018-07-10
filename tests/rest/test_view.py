import pytest
from chi.globals import main
from chi.globals.main import ChiServer
from chi.database.db_config import DatabaseConfig
from chi.database.database import Database
from ..database.model.test_model import TestModelBase

class TestView(TestModelBase):


    @pytest.fixture
    def client(self):
        main.chi_server = ChiServer()
        client = main.chi_server.rest_server.app.test_client()
        yield client
