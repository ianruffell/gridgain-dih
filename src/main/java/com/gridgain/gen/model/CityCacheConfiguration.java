package com.gridgain.gen.model;

import static org.apache.ignite.cache.CacheMode.PARTITIONED;

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

public class CityCacheConfiguration<K, V> extends CacheConfiguration<Integer, City> {
    private static final long serialVersionUID = 0L;

    public CityCacheConfiguration() {
        setName("city");
        setIndexedTypes(Integer.class, City.class);
        setCacheMode(PARTITIONED);
        setSqlSchema("PUBLIC");
		setReadThrough(true);
		setWriteThrough(true);

		CacheJdbcPojoStoreFactory<Integer, City> factory = new CacheJdbcPojoStoreFactory<>();
		factory.setDialect(new MySQLDialect());
		factory.setDataSourceFactory((Factory<DataSource>) () -> {
			MysqlDataSource mysqlDataSrc = new MysqlDataSource();
			mysqlDataSrc.setURL("jdbc:mysql://localhost:3306/world");
			mysqlDataSrc.setUser("root");
			mysqlDataSrc.setPassword("password");
			return mysqlDataSrc;
		});

		JdbcType cityType = new JdbcType();
		cityType.setCacheName("city");
		cityType.setKeyType(Integer.class);
		cityType.setValueType(City.class);
		cityType.setDatabaseTable("city");

		cityType.setKeyFields(new JdbcTypeField(java.sql.Types.INTEGER, "ID", Integer.class, "ID"));

		cityType.setValueFields(
				new JdbcTypeField(java.sql.Types.INTEGER, "id", Integer.class, "id"), 
				new JdbcTypeField(java.sql.Types.VARCHAR, "name", String.class, "name"), 
				new JdbcTypeField(java.sql.Types.VARCHAR, "countrycode", String.class, "countrycode"), 
				new JdbcTypeField(java.sql.Types.VARCHAR, "district", String.class, "district"), 
				new JdbcTypeField(java.sql.Types.INTEGER, "population", Integer.class, "population")
				);

		factory.setTypes(cityType);

		setCacheStoreFactory(factory);

		QueryEntity qryEntity = new QueryEntity();

		qryEntity.setKeyType(Integer.class.getName());
		qryEntity.setValueType(City.class.getName());
		qryEntity.setKeyFieldName("ID");

		Set<String> keyFields = new HashSet<>();
		keyFields.add("ID");
		qryEntity.setKeyFields(keyFields);

		LinkedHashMap<String, String> fields = new LinkedHashMap<>();
		fields.put("id", "java.lang.Integer");
		fields.put("name", "java.lang.String");
		fields.put("countrycode", "java.lang.String");
		fields.put("district", "java.lang.String");
		fields.put("population", "java.lang.Integer");

		qryEntity.setFields(fields);

		setQueryEntities(Collections.singletonList(qryEntity));
    }
}