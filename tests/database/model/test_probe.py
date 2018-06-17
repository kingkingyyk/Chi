from .test_model import TestModelBase
from chi.database.model.model import Site, Node, Probe


class TestProbe(TestModelBase):

    def create_model_object(self):
        self.site = Site.create(name='mysite', map_url='someurl')
        self.site.save()

        self.node = Node.create(name='mynode', site_id=self.site.id, map_x=-4.0, map_y=4.0)
        self.node.save()

        self.name = 'myprobe'
        self.x = -8.0
        self.y = 8.0
        self.unit = 'C'
        probe = Probe.create(name=self.name, node_id=self.node.id, site_id=self.site.id,
                             map_x=self.x, map_y=self.y, unit=self.unit)
        probe.save()
        self.id = probe.id

    def test_query(self):
        probe = Probe.objects(id=self.id).first()
        assert probe.id == self.id
        assert probe.name == self.name
        assert probe.node_id == self.node.id
        assert probe.site_id == self.site.id
        assert probe.map_x == self.x
        assert probe.map_y == self.y
        assert probe.unit == self.unit
        assert probe.transformation_expr is None

    def test_update(self):
        new_site = Site.create(name='mynewsite', map_url='someotherurl')
        new_site.save()

        new_node = Node.create(name='mynewnode', site_id=new_site.id, map_x=0.0, map_y=0.0)
        new_node.save()

        new_name = 'another-control'
        new_x = -8.0
        new_y = 8.0
        new_unit = 'm/s'
        new_trans_expr = 'abc'

        Probe.objects(id=self.id).update(name=new_name, node_id=new_node.id, site_id=new_site.id,
                                         map_x=new_x, map_y=new_y, unit=new_unit, transformation_expr=new_trans_expr)

        probe = Probe.objects(id=self.id).first()
        assert probe.id == self.id
        assert probe.name == new_name
        assert probe.node_id == new_node.id
        assert probe.site_id == new_site.id
        assert probe.map_x == new_x
        assert probe.map_y == new_y
        assert probe.unit == new_unit
        assert probe.transformation_expr == new_trans_expr

    def test_delete(self):
        probe = Probe.objects(id=self.id).first()
        probe.delete()

        assert Probe.objects(id=self.id).count() == 0