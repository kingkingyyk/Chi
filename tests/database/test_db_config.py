from ..config.config_test_base import ConfigTestBase
from chi.database.db_config import DatabaseConfig

class TestDatabaseConfig(ConfigTestBase):

    def test_default_config(self):
        db_config = DatabaseConfig()

        assert db_config.cluster_name == DatabaseConfig._DEFAULT_CLUSTER_NAME
        assert db_config.keyspace == DatabaseConfig._DEFAULT_KEYSPACE
        assert db_config.replication == DatabaseConfig._DEFAULT_REPLICATIION
        assert db_config.executor_threads == DatabaseConfig._DEFAULT_EXECUTOR_THREADS
        assert db_config.connection_timeout == DatabaseConfig._DEFAULT_CONNECTION_TIMEOUT
        assert db_config.query_timeout == DatabaseConfig._DEFAULT_QUERY_TIMEOUT
        assert db_config.endpoints_port == DatabaseConfig._DEFAULT_ENDPOINT_PORT
        assert db_config.endpoints == DatabaseConfig._DEFAULT_ENDPOINT_HOSTS

    def update_config(self):
        db_config = DatabaseConfig()

        new_cluster_name = 'cluster'
        new_keyspace = 'new_chi'
        new_replication = 2
        new_executor_threads = 2
        new_connection_timeout = 30.0
        new_query_timeout = 3.0
        new_endpoints = ['localhost']
        new_endpoint_port = 1234

        db_config.cluster_name = new_cluster_name
        db_config.keyspace = new_keyspace
        db_config.replication = new_replication
        db_config.executor_threads = new_executor_threads
        db_config.connection_timeout = new_connection_timeout
        db_config.query_timeout = new_query_timeout
        db_config.endpoints = new_endpoints
        db_config.endpoints_port = new_endpoint_port

        assert db_config.cluster_name == new_cluster_name, 'Before persist'
        assert db_config.keyspace == new_keyspace, 'Before persist'
        assert db_config.replication == new_replication, 'Before persist'
        assert db_config.executor_threads == new_executor_threads, 'Before persist'
        assert db_config.connection_timeout == new_connection_timeout, 'Before persist'
        assert db_config.query_timeout == new_query_timeout, 'Before persist'
        assert db_config.endpoints_port == new_endpoints, 'Before persist'
        assert db_config.endpoints == new_endpoint_port, 'Before persist'

        db_config.persist()

        db_config = DatabaseConfig()

        assert db_config.cluster_name == new_cluster_name, 'After persist'
        assert db_config.keyspace == new_keyspace, 'After persist'
        assert db_config.replication == new_replication, 'After persist'
        assert db_config.executor_threads == new_executor_threads, 'After persist'
        assert db_config.connection_timeout == new_connection_timeout, 'After persist'
        assert db_config.query_timeout == new_query_timeout, 'After persist'
        assert db_config.endpoints_port == new_endpoints, 'After persist'
        assert db_config.endpoints == new_endpoint_port, 'After persist'