from chi.config.config import Config


class DummyConfig(Config):
    VALUE_KEY = 'value'
    DEFAULT_VALUE = 'value'

    def __init__(self, file):
        super().__init__(file)

    def _init_default(self):
        self.settings[DummyConfig.VALUE_KEY] = 'value'

    @property
    def value(self):
        return self.settings[DummyConfig.VALUE_KEY]

    @value.setter
    def value(self, n):
        self.settings[DummyConfig.VALUE_KEY] = n