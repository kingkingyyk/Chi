from .test_model import TestModelBase
from chi.database.model.model import Site, Node, Probe, Reading
from datetime import datetime


class TestReading(TestModelBase):

    def create_model_object(self):
        self.site = Site.create(name='mysite', map_url='someurl')
        self.site.save()

        self.node = Node.create(name='mynode', site_id=self.site.id, map_x=-4.0, map_y=4.0)
        self.node.save()

        self.probe = Probe.create(name='myprobe', node_id=self.node.id, site_id=self.site.id,
                             map_x=-8.0, map_y=8.0, unit='C')
        self.probe.save()

        self.ts = datetime.now()
        self.value = 16.0
        reading = Reading.create(probe_id=self.probe.id, received_time=self.ts, raw_value=self.value)
        self.id = reading.id

    def test_query(self):
        reading = Reading.objects(id=self.id, received_time=self.ts).first()
        assert reading.id == self.id
        assert reading.probe_id == self.probe.id
        assert reading.received_time == self.ts
        assert reading.raw_value == self.value

    def test_delete(self):
        reading = Reading.objects(id=self.id, received_time=self.ts).first()
        reading.delete()

        assert Reading.objects(id=self.id, received_time=self.ts).count() == 0