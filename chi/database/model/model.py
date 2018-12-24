from cassandra.cqlengine.models import Model
from cassandra.cqlengine import columns
from uuid import uuid4
import bcrypt

class User(Model):
    PASSWORD_ENCODING = 'UTF-8'
    ADMINISTRATOR = 'administrator'
    READWRITE_USER = 'readwrite'
    READONLY_USER = 'readonly'
    POSSIBLE_ROLES = [ADMINISTRATOR, READWRITE_USER, READONLY_USER]

    id = columns.UUID(primary_key=True, default=uuid4, partition_key=True)
    username = columns.Text(required=True, index=True)
    password = columns.Bytes(required=True)
    role = columns.Text(required=True, default=READWRITE_USER)

    @staticmethod
    def encrypt_password(val):
        return bcrypt.hashpw(val.encode(User.PASSWORD_ENCODING), bcrypt.gensalt())

    def is_password_match(self, val):
        return bcrypt.hashpw(val.encode(User.PASSWORD_ENCODING), self.password) == self.password

class Site(Model):
    id = columns.UUID(primary_key=True, default=uuid4, partition_key=True)
    name = columns.Text(required=True)
    map_url = columns.Text(required=True)


class Node(Model):
    id = columns.UUID(primary_key=True, default=uuid4, partition_key=True)
    name = columns.Text(required=True)
    site_id = columns.UUID(required=True)
    map_x = columns.Float(required=True)
    map_y = columns.Float(required=True)


class Control(Model):
    id = columns.UUID(primary_key=True, default=uuid4, partition_key=True)
    name = columns.Text(required=True)
    node_id = columns.UUID(required=True)
    site_id = columns.UUID(required=True)
    map_x = columns.Float(required=True)
    map_y = columns.Float(required=True)


class Probe(Model):
    id = columns.UUID(primary_key=True, default=uuid4, partition_key=True)
    name = columns.Text(required=True)
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


class TokenBind(Model):
    token = columns.UUID(primary_key=True, default=uuid4, partition_key=True)
    username = columns.Text(required=True, index=True)
    grant_time = columns.DateTime(required=True)