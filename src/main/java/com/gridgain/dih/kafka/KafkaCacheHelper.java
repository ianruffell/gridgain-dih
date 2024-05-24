package com.gridgain.dih.kafka;

import static org.apache.ignite.cache.CacheMode.PARTITIONED;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.CacheConfiguration;

import com.gridgain.dih.kafka.model.Account;
import com.gridgain.dih.kafka.model.Holding;
import com.gridgain.dih.kafka.model.HoldingKey;
import com.gridgain.dih.kafka.model.Product;
import com.gridgain.dih.kafka.model.ProductPrice;
import com.gridgain.dih.kafka.model.Trade;

public class KafkaCacheHelper {

	public static final String SQL_SCHEMA = "PUBLIC";

	private final IgniteCache<String, Account> accountCache;
	private final IgniteCache<HoldingKey, Holding> holdingCache;
	private final IgniteCache<String, Product> productCache;
	private final IgniteCache<String, ProductPrice> productPriceCache;
	private final IgniteCache<String, Trade> tradeCache;

	public KafkaCacheHelper(Ignite ignite) throws Exception {
		this(ignite, true);
	}

	public KafkaCacheHelper(Ignite ignite, boolean destroyCaches) throws Exception {
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

	public IgniteCache<String, Account> getAccountCache() {
		return accountCache;
	}

	public IgniteCache<HoldingKey, Holding> getHoldingCache() {
		return holdingCache;
	}

	public IgniteCache<String, Product> getProductCache() {
		return productCache;
	}

	public IgniteCache<String, ProductPrice> getProductPriceCache() {
		return productPriceCache;
	}

	public IgniteCache<String, Trade> getTradeCache() {
		return tradeCache;
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
