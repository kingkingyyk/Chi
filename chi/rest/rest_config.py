from ..config.config import Config


class RESTConfig(Config):
    _CONFIG_FILE = 'rest.json'
    _PORT_KEY = 'port'
    _DEFAULT_PORT = 7999

    def __init__(self):
        super().__init__(RESTConfig._CONFIG_FILE)

    def _init_default(self):
        self.settings = {RESTConfig._PORT_KEY: RESTConfig._DEFAULT_PORT}

    @property
    def port(self):
        return self.settings[RESTConfig._PORT_KEY]

    @port.setter
    def port(self, n):
        self.settings[RESTConfig._PORT_KEY] = n

