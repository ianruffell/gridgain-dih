package com.gridgain.gen.model;

import static org.apache.ignite.cache.CacheMode.${cachemode};

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.cache.configuration.Factory;
import javax.sql.DataSource;

import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.cache.store.jdbc.JdbcTypeField;
import org.apache.ignite.cache.store.jdbc.dialect.MySQLDialect;
import org.apache.ignite.configuration.CacheConfiguration;

import com.mysql.cj.jdbc.MysqlDataSource;

public class ${table.classname}CacheConfiguration<K, V> extends CacheConfiguration<${table.keytype}, ${table.classname}> {
    private static final long serialVersionUID = 0L;

    public ${table.classname}CacheConfiguration() {
        setName("${table.name}");
        setIndexedTypes(${table.keytype}.class, ${table.classname}.class);
        setCacheMode(${cachemode});
        setSqlSchema("${cacheschema}");
		setReadThrough(true);
		setWriteThrough(true);

		CacheJdbcPojoStoreFactory<${table.keytype}, ${table.classname}> factory = new CacheJdbcPojoStoreFactory<>();
		factory.setDialect(new MySQLDialect());
		factory.setDataSourceFactory((Factory<DataSource>) () -> {
			MysqlDataSource mysqlDataSrc = new MysqlDataSource();
			mysqlDataSrc.setURL("jdbc:mysql://${db.hostname}:${db.port?long?c}/${db.db}");
			mysqlDataSrc.setUser("${db.username}");
			mysqlDataSrc.setPassword("${db.password}");
			return mysqlDataSrc;
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
    }
}