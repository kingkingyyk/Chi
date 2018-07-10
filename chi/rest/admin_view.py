import json
from flask_classful import FlaskView, route
from .representations import output_json
from chi.globals import main

class AdminView(FlaskView):
    representations = {'application/json': output_json}

    @route('/shutdown', methods=['POST'])
    def shutdown(self):
        return json.dumps({'status' : main.chi_server.shutdown()})