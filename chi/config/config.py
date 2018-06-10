import os, json
import logging
from chi.data.observer import Observer
from pathlib import Path


class Config(Observer):
    _CONFIG_FOLDER = os.path.join(str(Path.home()), '.chi', 'conf')
    _LOGGER = logging.getLogger(__name__)

    def __init__(self, file):
        super().__init__()
        self.config_file = os.path.join(Config._CONFIG_FOLDER, file)
        self.settings = {}
        if os.path.exists(self.config_file):
            self._load_config()
        else:
            self._init_default()
            self.persist()

    def _init_default(self):
        self._init_default()

    def _load_config(self):
        Config._LOGGER.info('Loading configuration from '+self.config_file)
        with open(self.config_file, 'r') as config_file_obj:
            self.settings = json.load(config_file_obj)
        Config._LOGGER.info('Loaded configuration from '+self.config_file)

    def persist(self):
        Config._LOGGER.info('Exporting configuration to '+self.config_file)
        os.makedirs(self._CONFIG_FOLDER, exist_ok=True)
        with open(self.config_file, 'w') as config_file_obj:
            json.dump(self.settings, config_file_obj)
        Config._LOGGER.info('Exported configuration to '+self.config_file)

