package ${package_pojo};

import static org.apache.ignite.cache.CacheMode.${cachemode};

<#if db.type == "JDBC">
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.sql.SQLException;
import java.sql.Date;
<#if table.containsTimestamp == true>
import java.sql.Timestamp;
</#if>

import javax.cache.configuration.Factory;
import javax.sql.DataSource;

import org.apache.ignite.cache.QueryEntity;

</#if>
<#if db.type == "CASSANDRA">
import java.io.File;
import org.apache.ignite.cache.store.cassandra.CassandraCacheStoreFactory;
import org.apache.ignite.cache.store.cassandra.datasource.DataSource;
import org.apache.ignite.cache.store.cassandra.persistence.KeyValuePersistenceSettings;
</#if>
<#if db.type == "MONGODB">
import javax.cache.configuration.FactoryBuilder;
</#if>
<#if db.type == "JDBC">
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.cache.store.jdbc.JdbcTypeField;
import org.apache.ignite.cache.store.jdbc.dialect.${db.dialect};
</#if>
import org.apache.ignite.configuration.CacheConfiguration;

<#if db.type == "JDBC">
import ${db.datasourcePackage}.${db.datasourceClass};
</#if>

public class ${table.classname}CacheConfiguration<K, V> extends CacheConfiguration<${table.keytype}, ${table.classname}> {
    private static final long serialVersionUID = 0L;

    public ${table.classname}CacheConfiguration() {
        setName("${table.name}");
        setIndexedTypes(${table.keytype}.class, ${table.classname}.class);
        setCacheMode(${cachemode});
        setSqlSchema("${cacheschema}");
		setBackups(1);
		setReadThrough(true);
		setWriteThrough(true);

        <#if db.type == "JDBC">
		CacheJdbcPojoStoreFactory<${table.keytype}, ${table.classname}> factory = new CacheJdbcPojoStoreFactory<>();
		factory.setDialect(new ${db.dialect}());
		factory.setDataSourceFactory((Factory<DataSource>) () -> {
		    ${db.datasourceClass} dataSrc = null;
		    try {
			    dataSrc = new ${db.datasourceClass}();
			    dataSrc.setURL("${db.jdbcUrl?no_esc}");
				dataSrc.setUser("${db.user}");
				dataSrc.setPassword("${db.password}");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return dataSrc;
		});

		JdbcType ${table.name}Type = new JdbcType();
		${table.name}Type.setCacheName("${table.name}");
		${table.name}Type.setKeyType(${table.keytype}.class);
		${table.name}Type.setValueType(${table.classname}.class);
		${table.name}Type.setDatabaseTable("${table.name}");

		${table.name}Type.setKeyFields(new JdbcTypeField(java.sql.Types.${table.keysqltype}, "${table.keyname}", Integer.class, "${table.keyname}"));

		${table.name}Type.setValueFields(
				<#list table.columns as column>
				new JdbcTypeField(java.sql.Types.${column.sqltype}, "${column.name}", ${column.ignitetype}.class, "${column.name}")<#sep>, </#sep>
				</#list>
				);

		factory.setTypes(${table.name}Type);

		setCacheStoreFactory(factory);

		QueryEntity qryEntity = new QueryEntity();

		qryEntity.setKeyType(${table.keytype}.class.getName());
		qryEntity.setValueType(${table.classname}.class.getName());
		qryEntity.setKeyFieldName("${table.keyname}");

		Set<String> keyFields = new HashSet<>();
		keyFields.add("${table.keyname}");
		qryEntity.setKeyFields(keyFields);

		LinkedHashMap<String, String> fields = new LinkedHashMap<>();
		<#list table.columns as column>
		fields.put("${column.name}", "java.lang.${column.ignitetype}");
		</#list>

		qryEntity.setFields(fields);

		setQueryEntities(Collections.singletonList(qryEntity));
		</#if>
		
        <#if db.type == "CASSANDRA">
		DataSource dataSource = new DataSource();

		dataSource.setContactPoints("localhost");
		KeyValuePersistenceSettings persistenceSettings = new KeyValuePersistenceSettings(
				new File("src/main/resources/${table.classname}_persistence_settings.xml"));
		CassandraCacheStoreFactory<${table.keytype}, ${table.classname}> factory = new CassandraCacheStoreFactory<>();
		factory.setDataSource(dataSource);
		factory.setPersistenceSettings(persistenceSettings);

		setCacheStoreFactory(factory);
		</#if>
		
		 <#if db.type == "MONGODB">
		setCacheStoreFactory(FactoryBuilder.factoryOf(${table.classname}MongoCacheStore.class));
		</#if>

    }
}