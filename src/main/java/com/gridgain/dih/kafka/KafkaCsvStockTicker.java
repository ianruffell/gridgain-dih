package com.gridgain.dih.kafka;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.github.javafaker.Faker;
import com.gridgain.dih.kafka.avro.Account;
import com.gridgain.dih.kafka.avro.Holding;
import com.gridgain.dih.kafka.avro.Product;
import com.gridgain.dih.kafka.avro.ProductPrice;
import com.gridgain.dih.kafka.avro.Trade;
import com.gridgain.dih.kafka.model.TradeType;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;

public class KafkaCsvStockTicker implements Runnable {

	public static final String START_DATE = "09/23/2013";
	public static final String[] STOCKS = { "AAPL", "AMD", "AMZN", "CSCO", "META", "MSFT", "NFLX", "QCOM", "TSLA", "IBM" };
	public static final String[] STOCK_NAMES = { "Apple", "AMD", "Amazon", "Cisco", "Meta", "Microsoft", "Netflix",
			"Qualcom", "Tesla", "IBM" };

	public static final boolean DEBUG = false;
	public static final int TICKS_PER_DAY = 1000;
	public static final int NUM_ACCOUNTS = 25;
	public static final long START_HOLDING = 10000;

	public static final String ACCOUNT_TOPIC = "Account";
	public static final String PRODUCT_TOPIC = "Product";
	public static final String PRODUCT_PRICE_TOPIC = "ProductPrice";
	public static final String TRADE_TOPIC = "Trade";
	public static final String HOLDING_TOPIC = "Holding";
	public static final String BOOTSTRAP_URL = "localhost:9092";
	public static final String SCHEMA_REGISTRY_URL = "http://localhost:8081";
	private KafkaProducer<String, Account> accountProducer;
	private KafkaProducer<String, Product> productProducer;
	private KafkaProducer<String, ProductPrice> productPriceProducer;
	private KafkaProducer<String, Trade> tradeProducer;
	private KafkaProducer<String, Holding> holdingProducer;

	private final List<Account> accounts = new ArrayList<>();
	private final List<Product> products = new ArrayList<>();
	private final Random random = new Random();

	private ScheduledFuture<?> scheduledFuture;
	private int second = 0;
	private Map<String, StockTicker> tickers = new HashMap<>();

	public static void main(String args[]) throws Exception {
		new KafkaCsvStockTicker();
	}
	
	public KafkaCsvStockTicker() throws Exception {
		// Create caches
		try (KafkaCacheHelper kch = new KafkaCacheHelper(true)) {
		}
		
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_URL);
		props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, SCHEMA_REGISTRY_URL);
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
		props.put("value.converter.schemas.enable", "true");

		accountProducer = new KafkaProducer<>(props);
		productProducer = new KafkaProducer<>(props);
		productPriceProducer = new KafkaProducer<>(props);
		tradeProducer = new KafkaProducer<>(props);
		holdingProducer = new KafkaProducer<>(props);

		for (int i = 0; i < STOCKS.length; i++) {
			String stock = STOCKS[i];
			InputStream inputStream = KafkaCsvStockTicker.class.getClassLoader()
					.getResourceAsStream("data/" + stock + ".csv");
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(reader);
			List<CSVRecord> records = parser.getRecords();

			DayData dayData = null;
			Map<String, DayData> data = new HashMap<>();

			for (CSVRecord csvRecord : records) {
				dayData = DayData.get(csvRecord);
				data.put(dayData.getDate(), dayData);
			}

			tickers.put(stock, new StockTicker(stock, data, START_DATE));
			Product product = new Product(stock, STOCK_NAMES[i]);
			products.add(product);
			send(product);
		}

		System.out.println("Generating Accounts...");
		Faker faker = new Faker();
		for (int i = 0; i < NUM_ACCOUNTS; i++) {
			Account account = new Account(UUID.randomUUID().toString(), faker.name().fullName());
			accounts.add(account);
			send(account);

			for (int j = 0; j < STOCKS.length - 1; j++) {
				String pId = tickers.get(STOCKS[j]).getSymbol();
				Holding h = new Holding(UUID.randomUUID().toString(), account.getId(), System.currentTimeMillis(),
						START_HOLDING, pId);
				send(h);
			}
		}
		scheduledFuture = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this, 5000, 5000,
				TimeUnit.MILLISECONDS);
	}

	public void close() throws Exception {
		scheduledFuture.cancel(true);
		productPriceProducer.flush();
		tradeProducer.flush();
		productPriceProducer.close();
		tradeProducer.close();
	}

	@Override
	public void run() {

		System.out.println("Generating price & trades");
		second++;
		if (second == TICKS_PER_DAY) {
			for (Entry<String, StockTicker> entry : tickers.entrySet()) {
				try {
					entry.getValue().nextDay();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				second = 0;
			}
		}

		for (Entry<String, StockTicker> entry : tickers.entrySet()) {
			StockTicker stockTicker = entry.getValue();
			double currentPrice = stockTicker.tick();
			ProductPrice productPrice = new ProductPrice(UUID.randomUUID().toString(),
					System.currentTimeMillis(), stockTicker.getSymbol(), currentPrice);
			send(productPrice);
		}

		if (random.nextBoolean()) {
			int stockId = random.nextInt(STOCKS.length - 1);
			String stock = STOCKS[stockId];
			StockTicker stockTicker = tickers.get(stock);

			String type = TradeType.BUY.name();
			if (random.nextBoolean()) {
				type = TradeType.SELL.name();
			}

			Trade trade = new Trade(UUID.randomUUID().toString(), accounts.get(random.nextInt(NUM_ACCOUNTS)).getId(),
					stock, random.nextInt(500), stockTicker.getCurrentPrice(), type,
					System.currentTimeMillis());
			send(trade);
		}
	}

	public void send(Account account) {
		ProducerRecord<String, Account> record = new ProducerRecord<>(ACCOUNT_TOPIC, account.getId(), account);
		accountProducer.send(record);
	}

	public void send(Product product) {
		ProducerRecord<String, Product> record = new ProducerRecord<>(PRODUCT_TOPIC, product.getSymbol(), product);
		productProducer.send(record);
	}

	public void send(ProductPrice productPrice) {
		System.out.println("send - " + productPrice.toString());
		try {
			ProducerRecord<String, ProductPrice> record = new ProducerRecord<>(PRODUCT_PRICE_TOPIC,
					productPrice.getId(), productPrice);
			productPriceProducer.send(record);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void send(Trade trade) {
		System.out.println(trade.toString());
		ProducerRecord<String, Trade> record = new ProducerRecord<>(TRADE_TOPIC, trade.getId(), trade);
		tradeProducer.send(record);
	}

	public void send(Holding holdings) {
		ProducerRecord<String, Holding> record = new ProducerRecord<>(HOLDING_TOPIC, holdings.getId(), holdings);
		holdingProducer.send(record);
	}

}
