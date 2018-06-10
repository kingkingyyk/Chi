from cassandra.cqlengine.management import sync_table
from cassandra.cluster import Cluster
import logging, sys, inspect
from .model.model import *


class Database(object):
    _LOGGER = logging.getLogger(__name__)

    def __init__(self, db_config):
        self.config = db_config
        self.cluster = Cluster( contact_points=self.config.endpoints,
                                port=self.config.endpoints_port,
                                executor_threads=self.config.executor_threads)
        self.session = self.cluster.connect()
        self._init_table()


    def _init_table(self):
        self.session.execute(f'CREATE KEYSPACE IF NOT EXISTS {db_config.keyspace}'
                             f' WITH REPLICATION = {db_config.replication}')
        self.session.set_keyspace(self.config.keyspace)
        sync_table(User)
        sync_table(Site)
        sync_table(Node)
        sync_table(Control)
        sync_table(Probe)
        sync_table(Reading)