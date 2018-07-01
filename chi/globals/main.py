from ..database.db_config import DatabaseConfig
from ..database.database import Database
from ..rest.rest_config import RESTConfig
from ..rest.rest_server import RESTServer


class ChiServer(object):

    def __init__(self):
        self.is_shutting_down = False
        self.database = Database(DatabaseConfig())
        self.rest_server = RESTServer(RESTConfig())

    def shutdown(self):
        if not self.is_shutting_down:
            self.database.shutdown()
            #self.rest_server.shutdown()
            self.is_shutting_down = True
            return True
        return False


global chi_server