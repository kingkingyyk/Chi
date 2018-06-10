from cassandra.cqlengine.models import Model
from cassandra.cqlengine import columns
from uuid import uuid4


class User(Model):
    id = columns.UUID(primary_key=True, default=uuid4, partition_key=True)
    username = columns.Text(primary_key=True, required=True)
    password = columns.Text(required=True)


class Site(Model):
    id = columns.UUID(primary_key=True, default=uuid4, partition_key=True)
    name = columns.Text(primary_key=True, required=True)
    map_url = columns.Text(required=True)


class Node(Model):
    id = columns.UUID(primary_key=True, default=uuid4, partition_key=True)
    name = columns.Text(primary_key=True, required=True)
    site_id = columns.UUID(required=True)
    map_x = columns.Float(required=True)
    map_y = columns.Float(required=True)


class Control(Model):
    id = columns.UUID(primary_key=True, default=uuid4, partition_key=True)
    name = columns.Text(primary_key=True, required=True)
    node_id = columns.UUID(required=True)
    site_id = columns.UUID(required=True)
    map_x = columns.Float(required=True)
    map_y = columns.Float(required=True)


class Probe(Model):
    id = columns.UUID(primary_key=True, default=uuid4, partition_key=True)
    name = columns.Text(primary_key=True, required=True)
    node_id = columns.UUID(required=True)
    site_id = columns.UUID(required=True)
    map_x = columns.Float(required=True)
    map_y = columns.Float(required=True)
    unit = columns.Text()
    transformation_expr = columns.Text()


class Reading(Model):
    id = columns.UUID(primary_key=True, default=uuid4, partition_key=True)
    probe_id = columns.UUID(required=True)
    received_time = columns.DateTime(primary_key=True, clustering_order='DESC')
    raw_value = columns.Double(required=True)