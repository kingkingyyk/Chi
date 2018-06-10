from aiohttp import web
import json

async def shutdown(request):
    return web.Response(body=json.dumps({'status':True}), status=200)

