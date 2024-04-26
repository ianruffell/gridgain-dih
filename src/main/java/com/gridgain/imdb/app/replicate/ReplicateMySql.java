package com.gridgain.imdb.app.replicate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.codehaus.plexus.util.FileUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class ReplicateMySql {

	public static void main(String[] args)
			throws Exception {
		new ReplicateMySql();
	}

	public ReplicateMySql() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException,
			TemplateException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {
		Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + Settings.MYSQL_DB.hostname + ":" + Settings.MYSQL_DB.port + "/" + Settings.MYSQL_DB.db + "?"
					+ "user=" + Settings.MYSQL_DB.username + "&password=" + Settings.MYSQL_DB.password);
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet tablers = metaData.getTables(null, null, null, new String[] { "Table" });

			List<Table> tables = new ArrayList<>();

			FileUtils.forceMkdir(new File(Settings.getOutputPath()));

			Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
			cfg.setDirectoryForTemplateLoading(new File("templates"));
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			cfg.setLogTemplateExceptions(true);
			cfg.setWrapUncheckedExceptions(true);
			cfg.setFallbackOnNullLoopVariable(false);
			cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault());

			List<String> tableNames = new ArrayList<>();
			while (tablers.next()) {
				String table = tablers.getString("TABLE_NAME");
				if (!table.equals("sys_config")) {
					tableNames.add(table);
				}
			}

			for (String tableName : tableNames) {
				Table table = null;
				ResultSet columns = metaData.getColumns(null, null, tableName, null);
				while (columns.next()) {
					String name = columns.getString("COLUMN_NAME");
					String type = columns.getString("TYPE_NAME");
					boolean nullable = columns.getBoolean("IS_NULLABLE");
					Column column = new Column(name, type, nullable);

					if (table == null) {
						table = new Table(tableName, name, column.getIgnitetype(), column.getSqltype());
					}
					table.addColumn(column);
				}
				System.out.println(table.toString());
				
				tables.add(table);

				Map<String,Object> root = Settings.getTemplateModel();
				root.put("table", table);

				Template cacheConfig = cfg.getTemplate("cache_config_template.ftlh");
				Writer out = new FileWriter(
						new File(Settings.getOutputPath() + "/" + table.getClassname()
								+ "CacheConfiguration.java"));
				cacheConfig.process(root, out);

				Template pojo = cfg.getTemplate("pojo.ftlh");
				out = new FileWriter(
						new File(Settings.getOutputPath() + "/" + table.getClassname() + ".java"));
				pojo.process(root, out);
			}

			Map<String, Object> root = Settings.getTemplateModel();
			root.put("tables", tables);
			Template ich = cfg.getTemplate("IgniteClientHelper.ftlh");
			Writer out = new FileWriter(
					new File(Settings.getOutputPath() + "/IgniteClientHelper.java"));
			ich.process(root, out);
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
}
