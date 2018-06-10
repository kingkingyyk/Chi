from cassandra.cqlengine.management import sync_table, create_keyspace_simple, drop_keyspace
from cassandra.cluster import Cluster
from cassandra.policies import RoundRobinPolicy
from cassandra.cqlengine import connection
from cassandra.cqlengine import models
import logging, time
from .model.model import *


class Database(object):
    _LOGGER = logging.getLogger(__name__)

    def __init__(self, db_config):
        self.config = db_config
        models.DEFAULT_KEYSPACE = self.config.keyspace
        self._cluster = Cluster(contact_points=self.config.endpoints,
                                port=self.config.endpoints_port,
                                executor_threads=self.config.executor_threads,
                                load_balancing_policy=RoundRobinPolicy(),
                                control_connection_timeout=self.config.query_timeout)
        self._session = self._cluster.connect()
        self._session.default_timeout = self.config.connection_timeout
        self._session_name = self.config.keyspace+'_'+str(time.time())
        connection.register_connection(name=self._session_name,
                                       session=self._session,
                                       default=True)
        self._init_table()

    def _init_table(self):
        Database._LOGGER.info('Initializing keyspace...')
        create_keyspace_simple(self.config.keyspace, self.config.replication, connections=[self._session_name])

        self._session.set_keyspace(self.config.keyspace)
        logging.info('Keyspace initialization done, synchronizing tables...')
        sync_table(User, keyspaces=[self.config.keyspace], connections=[self._session_name])
        sync_table(Site, keyspaces=[self.config.keyspace], connections=[self._session_name])
        sync_table(Node, keyspaces=[self.config.keyspace], connections=[self._session_name])
        sync_table(Control, keyspaces=[self.config.keyspace], connections=[self._session_name])
        sync_table(Probe, keyspaces=[self.config.keyspace], connections=[self._session_name])
        sync_table(Reading, keyspaces=[self.config.keyspace], connections=[self._session_name])
        Database._LOGGER.info('Done synchronizing tables.')

    def reset(self):
        Database._LOGGER.info('Dropping keyspace...')
        drop_keyspace(self.config.keyspace, connections=[self._session_name])
        Database._LOGGER.info('Keyspace is dropped.')
        self._init_table()

    def shutdown(self):
        self._session.shutdown()