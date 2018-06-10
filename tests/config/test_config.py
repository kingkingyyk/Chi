from chi.config.config import Config
from .dummy_config import DummyConfig
from .config_test_base import ConfigTestBase
import json, inspect, shutil, os


class TestConfig(ConfigTestBase):

    def test_default_config(self):
        dummy_config = DummyConfig(inspect.stack()[0][3]+'.json')
        assert dummy_config.value is DummyConfig.DEFAULT_VALUE, 'dummy_config.value isn\'t value'

        with open(dummy_config.config_file) as config_file_obj:
            config_output = json.load(config_file_obj)
        expected_config_output = {DummyConfig.VALUE_KEY: DummyConfig.DEFAULT_VALUE}
        assert config_output == expected_config_output, 'Default saved output is incorrect'

    def test_update_attribute(self):
        dummy_config = DummyConfig(inspect.stack()[0][3]+'.json')
        new_value = 'new_value'
        dummy_config.value = new_value
        assert dummy_config.value == new_value, "dummy_config.value isn't "+new_value

    def test_save_attribute(self):
        dummy_config = DummyConfig(inspect.stack()[0][3]+'.json')
        new_value = 'new_value'
        dummy_config.value = new_value
        dummy_config.persist()

        with open(dummy_config.config_file) as config_file_obj:
            config_output = json.load(config_file_obj)
        expected_config_output = {DummyConfig.VALUE_KEY: new_value}
        assert config_output == expected_config_output, 'Default saved output is incorrect'
