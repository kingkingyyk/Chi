from ..config.config_test_base import ConfigTestBase
from chi.rest.rest_config import RESTConfig


class TestRESTConfig(object):

    def test_default_settings(self):
        rest_config = RESTConfig()
        assert rest_config.port == RESTConfig._DEFAULT_PORT


    def test_persist_settings(self):
        new_port = 10000

        rest_config = RESTConfig()
        rest_config.port = new_port
        assert rest_config.port == new_port

        rest_config.persist()

        rest_config = RESTConfig()
        assert rest_config.port == new_port