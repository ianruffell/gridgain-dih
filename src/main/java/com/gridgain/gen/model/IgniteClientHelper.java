package com.gridgain.gen.model;

import static org.apache.ignite.cluster.ClusterState.ACTIVE;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.gridgain.imdb.app.DemoConfiguration;

public class IgniteClientHelper implements AutoCloseable {

	public static final String SQL_SCHEMA = "PUBLIC";

	private final Ignite ignite;

	public static void main(String args[]) throws Exception {
		try (IgniteClientHelper ich = new IgniteClientHelper()) {
			System.out.println("IgniteClientHelper");
		}
	}

	public IgniteClientHelper() throws Exception {
		this(true);
	}

	public IgniteClientHelper(boolean destroyCaches) throws Exception {
		IgniteConfiguration cfg = new DemoConfiguration();
		cfg.setClientMode(true);

		ignite = Ignition.start(cfg);
		ignite.cluster().state(ACTIVE);
		ignite.cluster().tag("Demo Cluster");

		if (destroyCaches) {
			System.out.println("Deleting Caches...");
			ignite.destroyCache("city");
			ignite.destroyCache("country");
			ignite.destroyCache("countrylanguage");
		}
		System.out.println("Creating Caches...");
		IgniteCache<Integer, City> cityCache = ignite.getOrCreateCache(new CityCacheConfiguration<Integer, City>());
		cityCache.loadCache(null);
		
		IgniteCache<String, Country> countryCache = ignite.getOrCreateCache(new CountryCacheConfiguration<String, Country>());
		countryCache.loadCache(null);
		
		IgniteCache<String, Countrylanguage> countrylanguageCache = ignite.getOrCreateCache(new CountrylanguageCacheConfiguration<String, Countrylanguage>());
		countrylanguageCache.loadCache(null);
	}

	public Ignite getIgnite() {
		return ignite;
	}

	@Override
	public void close() throws Exception {
		ignite.close();
	}


}
