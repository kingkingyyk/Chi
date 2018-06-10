from aiohttp import web
import chi.rest.admin
import chi.rest.user

app = web.Application()
app.router.add_get('/admin/shutdown', admin.shutdown)
app.router.add_get('/users', user.get_all)