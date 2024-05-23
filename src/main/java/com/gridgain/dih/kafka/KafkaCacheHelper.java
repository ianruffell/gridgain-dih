package com.gridgain.dih.kafka;

import static org.apache.ignite.cache.CacheMode.PARTITIONED;
import static org.apache.ignite.cluster.ClusterState.ACTIVE;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.gridgain.dih.app.DemoConfiguration;
import com.gridgain.dih.kafka.model.Account;
import com.gridgain.dih.kafka.model.Holding;
import com.gridgain.dih.kafka.model.HoldingKey;
import com.gridgain.dih.kafka.model.Product;
import com.gridgain.dih.kafka.model.ProductPrice;
import com.gridgain.dih.kafka.model.Trade;

public class KafkaCacheHelper implements AutoCloseable {

	public static final String SQL_SCHEMA = "PUBLIC";

	private final Ignite ignite;

	IgniteCache<String, Account> accountCache;
	IgniteCache<HoldingKey, Holding> holdingCache;
	IgniteCache<String, Product> productCache;
	IgniteCache<String, ProductPrice> productPriceCache;
	IgniteCache<String, Trade> tradeCache;

	public KafkaCacheHelper() throws Exception {
		this(true);
	}

	public KafkaCacheHelper(boolean destroyCaches) throws Exception {
		System.setProperty("IGNITE_QUIET", "true");

		IgniteConfiguration cfg = new DemoConfiguration();
		cfg.setClientMode(true);

		ignite = Ignition.start(cfg);
		ignite.cluster().state(ACTIVE);
		ignite.cluster().tag("Demo Cluster");

		if (destroyCaches) {
			System.out.println("Deleting Caches...");
			ignite.destroyCache("Account");
			ignite.destroyCache("Holding");
			ignite.destroyCache("Product");
			ignite.destroyCache("ProductPrice");
			ignite.destroyCache("Trade");
		}
		System.out.println("Creating Caches...");
		accountCache = ignite.getOrCreateCache(new AccountCacheConfiguration<String, Account>());
		holdingCache = ignite.getOrCreateCache(new HoldingCacheConfiguration<HoldingKey, Holding>());
		productCache = ignite.getOrCreateCache(new ProductCacheConfiguration<String, Product>());
		productPriceCache = ignite.getOrCreateCache(new ProductPriceCacheConfiguration<String, ProductPrice>());
		tradeCache = ignite.getOrCreateCache(new TradeCacheConfiguration<String, Trade>());
	}

	public Ignite getIgnite() {
		return ignite;
	}

	@Override
	public void close() throws Exception {
		ignite.close();
	}

	public class AccountCacheConfiguration<K, V> extends CacheConfiguration<String, Account> {
		private static final long serialVersionUID = 0L;

		public AccountCacheConfiguration() {
			setName("Account");
			setIndexedTypes(String.class, Account.class);
			setCacheMode(PARTITIONED);
			setSqlSchema("PUBLIC");
			setBackups(1);
		}
	}

	public class HoldingCacheConfiguration<K, V> extends CacheConfiguration<HoldingKey, Holding> {
		private static final long serialVersionUID = 0L;

		public HoldingCacheConfiguration() {
			setName("Holding");
			setIndexedTypes(HoldingKey.class, Holding.class);
			setCacheMode(PARTITIONED);
			setSqlSchema("PUBLIC");
			setBackups(1);
		}
	}

	public class ProductCacheConfiguration<K, V> extends CacheConfiguration<String, Product> {
		private static final long serialVersionUID = 0L;

		public ProductCacheConfiguration() {
			setName("Product");
			setIndexedTypes(String.class, Product.class);
			setCacheMode(PARTITIONED);
			setSqlSchema("PUBLIC");
			setBackups(1);
		}
	}

	public class ProductPriceCacheConfiguration<K, V> extends CacheConfiguration<String, ProductPrice> {
		private static final long serialVersionUID = 0L;

		public ProductPriceCacheConfiguration() {
			setName("ProductPrice");
			setIndexedTypes(String.class, ProductPrice.class);
			setCacheMode(PARTITIONED);
			setSqlSchema("PUBLIC");
			setBackups(1);
		}
	}

	public class TradeCacheConfiguration<K, V> extends CacheConfiguration<String, Trade> {
		private static final long serialVersionUID = 0L;

		public TradeCacheConfiguration() {
			setName("Trade");
			setIndexedTypes(String.class, Trade.class);
			setCacheMode(PARTITIONED);
			setSqlSchema("PUBLIC");
			setBackups(1);
		}
	}

}
