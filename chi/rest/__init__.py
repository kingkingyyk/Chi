from aiohttp import web
import chi.rest.admin
import chi.rest.user
from chi.rest.rest_config import RESTConfig
import threading

class RESTServer (threading.Thread):

    def __init__(self):
        threading.Thread.__init__(self)

    def run(self):
        app = web.Application()
        app.router.add_get('/admin/shutdown', admin.shutdown)
        app.router.add_get('/users', user.get_all)

        rest_config = RESTConfig()
        web.run_app(app=app, host='0.0.0.0', port=rest_config.port)


server_thread = RESTServer()
#server_thread.start()