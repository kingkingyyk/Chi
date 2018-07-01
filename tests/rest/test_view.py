import pytest
from chi.globals import main
from chi.globals.main import ChiServer


class TestView(object):

    @pytest.fixture
    def client(self):
        main.chi_server = ChiServer()
        client = main.chi_server.rest_server.app.test_client()
        yield client