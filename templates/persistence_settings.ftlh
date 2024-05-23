<persistence keyspace="${db.schema}" table="${table.name}" ttl="86400">
	<keyspaceOptions>
		REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor' : 1}
		AND DURABLE_WRITES = true
	</keyspaceOptions>
	<tableOption>
		comment = 'Cache test'
		AND read_repair_chance = 0.2
	</tableOption>
	<keyPersistence class="${table.keyPackage}.${table.keytype}" strategy="Primitive" column="${table.keyname}"
		/>
	<valuePersistence class="${package_pojo}.${table.classname}" strategy="POJO">
		<!-- Mapping from POJO field to Cassandra table column -->
		<#list table.columns as column>
		<field name="${column.name}" />
		</#list>
	</valuePersistence>
</persistence>