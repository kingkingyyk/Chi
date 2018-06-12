from .rest_server import RESTServer
from .rest_config import RESTConfig

server_thread = RESTServer(RESTConfig())
#server_thread.start()