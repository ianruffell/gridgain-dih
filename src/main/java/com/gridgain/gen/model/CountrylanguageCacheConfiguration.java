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

public class CountrylanguageCacheConfiguration<K, V> extends CacheConfiguration<String, Countrylanguage> {
    private static final long serialVersionUID = 0L;

    public CountrylanguageCacheConfiguration() {
        setName("countrylanguage");
        setIndexedTypes(String.class, Countrylanguage.class);
        setCacheMode(PARTITIONED);
        setSqlSchema("PUBLIC");
		setReadThrough(true);
		setWriteThrough(true);

		CacheJdbcPojoStoreFactory<String, Countrylanguage> factory = new CacheJdbcPojoStoreFactory<>();
		factory.setDialect(new MySQLDialect());
		factory.setDataSourceFactory((Factory<DataSource>) () -> {
			MysqlDataSource mysqlDataSrc = new MysqlDataSource();
			mysqlDataSrc.setURL("jdbc:mysql://localhost:3306/world");
			mysqlDataSrc.setUser("root");
			mysqlDataSrc.setPassword("password");
			return mysqlDataSrc;
		});

		JdbcType countrylanguageType = new JdbcType();
		countrylanguageType.setCacheName("countrylanguage");
		countrylanguageType.setKeyType(String.class);
		countrylanguageType.setValueType(Countrylanguage.class);
		countrylanguageType.setDatabaseTable("countrylanguage");

		countrylanguageType.setKeyFields(new JdbcTypeField(java.sql.Types.VARCHAR, "CountryCode", Integer.class, "CountryCode"));

		countrylanguageType.setValueFields(
				new JdbcTypeField(java.sql.Types.VARCHAR, "countrycode", String.class, "countrycode"), 
				new JdbcTypeField(java.sql.Types.VARCHAR, "language", String.class, "language"), 
				new JdbcTypeField(java.sql.Types.VARCHAR, "isofficial", String.class, "isofficial"), 
				new JdbcTypeField(java.sql.Types.DECIMAL, "percentage", Double.class, "percentage")
				);

		factory.setTypes(countrylanguageType);

		setCacheStoreFactory(factory);

		QueryEntity qryEntity = new QueryEntity();

		qryEntity.setKeyType(String.class.getName());
		qryEntity.setValueType(Countrylanguage.class.getName());
		qryEntity.setKeyFieldName("CountryCode");

		Set<String> keyFields = new HashSet<>();
		keyFields.add("CountryCode");
		qryEntity.setKeyFields(keyFields);

		LinkedHashMap<String, String> fields = new LinkedHashMap<>();
		fields.put("countrycode", "java.lang.String");
		fields.put("language", "java.lang.String");
		fields.put("isofficial", "java.lang.String");
		fields.put("percentage", "java.lang.Double");

		qryEntity.setFields(fields);

		setQueryEntities(Collections.singletonList(qryEntity));
    }
}