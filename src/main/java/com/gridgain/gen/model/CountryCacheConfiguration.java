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

public class CountryCacheConfiguration<K, V> extends CacheConfiguration<String, Country> {
    private static final long serialVersionUID = 0L;

    public CountryCacheConfiguration() {
        setName("country");
        setIndexedTypes(String.class, Country.class);
        setCacheMode(PARTITIONED);
        setSqlSchema("PUBLIC");
		setReadThrough(true);
		setWriteThrough(true);

		CacheJdbcPojoStoreFactory<String, Country> factory = new CacheJdbcPojoStoreFactory<>();
		factory.setDialect(new MySQLDialect());
		factory.setDataSourceFactory((Factory<DataSource>) () -> {
			MysqlDataSource mysqlDataSrc = new MysqlDataSource();
			mysqlDataSrc.setURL("jdbc:mysql://localhost:3306/world");
			mysqlDataSrc.setUser("root");
			mysqlDataSrc.setPassword("password");
			return mysqlDataSrc;
		});

		JdbcType countryType = new JdbcType();
		countryType.setCacheName("country");
		countryType.setKeyType(String.class);
		countryType.setValueType(Country.class);
		countryType.setDatabaseTable("country");

		countryType.setKeyFields(new JdbcTypeField(java.sql.Types.VARCHAR, "Code", Integer.class, "Code"));

		countryType.setValueFields(
				new JdbcTypeField(java.sql.Types.VARCHAR, "code", String.class, "code"), 
				new JdbcTypeField(java.sql.Types.VARCHAR, "name", String.class, "name"), 
				new JdbcTypeField(java.sql.Types.VARCHAR, "continent", String.class, "continent"), 
				new JdbcTypeField(java.sql.Types.VARCHAR, "region", String.class, "region"), 
				new JdbcTypeField(java.sql.Types.DECIMAL, "surfacearea", Double.class, "surfacearea"), 
				new JdbcTypeField(java.sql.Types.INTEGER, "indepyear", Integer.class, "indepyear"), 
				new JdbcTypeField(java.sql.Types.INTEGER, "population", Integer.class, "population"), 
				new JdbcTypeField(java.sql.Types.DECIMAL, "lifeexpectancy", Double.class, "lifeexpectancy"), 
				new JdbcTypeField(java.sql.Types.DECIMAL, "gnp", Double.class, "gnp"), 
				new JdbcTypeField(java.sql.Types.DECIMAL, "gnpold", Double.class, "gnpold"), 
				new JdbcTypeField(java.sql.Types.VARCHAR, "localname", String.class, "localname"), 
				new JdbcTypeField(java.sql.Types.VARCHAR, "governmentform", String.class, "governmentform"), 
				new JdbcTypeField(java.sql.Types.VARCHAR, "headofstate", String.class, "headofstate"), 
				new JdbcTypeField(java.sql.Types.INTEGER, "capital", Integer.class, "capital"), 
				new JdbcTypeField(java.sql.Types.VARCHAR, "code2", String.class, "code2")
				);

		factory.setTypes(countryType);

		setCacheStoreFactory(factory);

		QueryEntity qryEntity = new QueryEntity();

		qryEntity.setKeyType(String.class.getName());
		qryEntity.setValueType(Country.class.getName());
		qryEntity.setKeyFieldName("Code");

		Set<String> keyFields = new HashSet<>();
		keyFields.add("Code");
		qryEntity.setKeyFields(keyFields);

		LinkedHashMap<String, String> fields = new LinkedHashMap<>();
		fields.put("code", "java.lang.String");
		fields.put("name", "java.lang.String");
		fields.put("continent", "java.lang.String");
		fields.put("region", "java.lang.String");
		fields.put("surfacearea", "java.lang.Double");
		fields.put("indepyear", "java.lang.Integer");
		fields.put("population", "java.lang.Integer");
		fields.put("lifeexpectancy", "java.lang.Double");
		fields.put("gnp", "java.lang.Double");
		fields.put("gnpold", "java.lang.Double");
		fields.put("localname", "java.lang.String");
		fields.put("governmentform", "java.lang.String");
		fields.put("headofstate", "java.lang.String");
		fields.put("capital", "java.lang.Integer");
		fields.put("code2", "java.lang.String");

		qryEntity.setFields(fields);

		setQueryEntities(Collections.singletonList(qryEntity));
    }
}