from ..database.db_config import DatabaseConfig
from ..database.database import Database

global IS_SHUTDOWN
global DATABASE

IS_SHUTDOWN = False
DATABASE = Database(DatabaseConfig())