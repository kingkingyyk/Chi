from chi.config.config import Config
import shutil, os


class ConfigTestBase(object):

    def teardown_method(self, method):
        if os.path.exists(Config._CONFIG_FOLDER):
            shutil.rmtree(Config._CONFIG_FOLDER)