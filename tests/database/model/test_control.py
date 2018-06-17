from .test_model import TestModelBase
from chi.database.model.model import Site, Node, Control


class TestControl(TestModelBase):

    def create_model_object(self):
        self.site = Site.create(name='mysite', map_url='someurl')
        self.site.save()

        self.node = Node.create(name='mynode', site_id=self.site.id, map_x=-4.0, map_y=4.0)
        self.node.save()

        self.name = 'mycontrol'
        self.x = -8.0
        self.y = 8.0
        control = Control.create(name=self.name, node_id=self.node.id, site_id=self.site.id,
                                 map_x=self.x, map_y=self.y)
        control.save()
        self.id = control.id

    def test_query(self):
        control = Control.objects(id=self.id).first()
        assert control.id == self.id
        assert control.name == self.name
        assert control.node_id == self.node.id
        assert control.site_id == self.site.id
        assert control.map_x == self.x
        assert control.map_y == self.y

    def test_update(self):
        new_site = Site.create(name='mynewsite', map_url='someotherurl')
        new_site.save()

        new_node = Node.create(name='mynewnode', site_id=new_site.id, map_x=0.0, map_y=0.0)
        new_node.save()

        new_name = 'another-control'
        new_x = -8.0
        new_y = 8.0

        Control.objects(id=self.id).update(name=new_name, node_id=new_node.id, site_id=new_site.id,
                                            map_x=new_x, map_y=new_y)

        control = Control.objects(id=self.id).first()
        assert control.id == self.id
        assert control.name == new_name
        assert control.site_id == new_site.id
        assert control.node_id == new_node.id
        assert control.map_x == new_x
        assert control.map_y == new_y

    def test_delete(self):
        control = Control.objects(id=self.id).first()
        control.delete()

        assert Control.objects(id=self.id).count() == 0