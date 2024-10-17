package com.gridgain.dih.replicate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.codehaus.plexus.util.FileUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class GenerateAll {

	private final Configuration freemarkerCfg;
	private final Metadata[] metadatas = { new OracleMetadata() };
	//, new MongoDBMetadata(), new CassandraMetadata(), new MySqlMetadata(), new PostgresMetadata() };

	public static void main(String[] args) throws Exception {
		GenerateAll generateAll = new GenerateAll();
		generateAll.run();
	}

	public GenerateAll() throws IOException {
		freemarkerCfg = new Configuration(Configuration.VERSION_2_3_32);
		freemarkerCfg.setDirectoryForTemplateLoading(new File("templates"));
		freemarkerCfg.setDefaultEncoding("UTF-8");
		freemarkerCfg.setNumberFormat("computer");
		freemarkerCfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		freemarkerCfg.setLogTemplateExceptions(true);
		freemarkerCfg.setWrapUncheckedExceptions(true);
		freemarkerCfg.setFallbackOnNullLoopVariable(false);
		freemarkerCfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
	}

	public void run() throws Exception {
		Map<String, Object> root = metadatas[0].getTemplateModel();
		String pojoOutputPath = metadatas[0].getPojoOutputPath();
		FileUtils.forceMkdir(new File(pojoOutputPath));
		String restOutputPath = metadatas[0].getRestOutputPath();
		FileUtils.forceMkdir(new File(restOutputPath));
		String appOutputPath = metadatas[0].getAppOutputPath();
		FileUtils.forceMkdir(new File(appOutputPath));

		FileUtils.forceMkdir(new File("./openapi"));

		List<Table> tables = new ArrayList<>();
		for (Metadata metadata : metadatas) {
			List<Table> dbTables = metadata.getTables();
			tables.addAll(dbTables);

			for (Table table : dbTables) {
				root.put("table", table);
				root.put("db", metadata.getDB());

				Template cacheConfig = freemarkerCfg.getTemplate("cache_config_template.ftlh");
				Writer out = new FileWriter(
						new File(pojoOutputPath + "/" + table.getClassname() + "CacheConfiguration.java"));
				cacheConfig.process(root, out);

				Template pojo = freemarkerCfg.getTemplate("pojo.ftlh");
				out = new FileWriter(new File(pojoOutputPath + "/" + table.getClassname() + ".java"));
				pojo.process(root, out);

				Template restRes = freemarkerCfg.getTemplate("RestResource.ftlh");
				out = new FileWriter(new File(restOutputPath + "/" + table.getClassname() + "Resource.java"));
				restRes.process(root, out);

				if (metadata.getDB().getType().equals(DB.DBType.CASSANDRA.name())) {
					Template ps = freemarkerCfg.getTemplate("persistence_settings.ftlh");
					out = new FileWriter(
							new File("src/main/resources/" + table.getClassname() + "_persistence_settings.xml"));
					ps.process(root, out);
				} else if (metadata.getDB().getType().equals(DB.DBType.MONGODB.name())) {
					Template ps = freemarkerCfg.getTemplate("MongoCacheStore.ftlh");
					out = new FileWriter(
							new File(pojoOutputPath + "/" + table.getClassname() + "MongoCacheStore.java"));
					ps.process(root, out);

				}
			}
		}

		root.put("tables", tables);

		Template ich = freemarkerCfg.getTemplate("IgniteClientHelper.ftlh");
		Writer out = new FileWriter(new File(appOutputPath + "/IgniteClientHelper.java"));
		ich.process(root, out);

		Template as = freemarkerCfg.getTemplate("ApiServer.ftlh");
		out = new FileWriter(new File(appOutputPath + "/ApiServer.java"));
		as.process(root, out);

		Template oapi = freemarkerCfg.getTemplate("openapi.ftlh");
		out = new FileWriter(new File("./openapi/openapi.yaml"));
		oapi.process(root, out);
	}

}
