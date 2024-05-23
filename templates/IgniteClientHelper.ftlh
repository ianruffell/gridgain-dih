package ${package_app};

import static org.apache.ignite.cluster.ClusterState.ACTIVE;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.gridgain.dih.app.DemoConfiguration;

<#list tables as table>
import ${package_pojo}.${table.classname};
import ${package_pojo}.${table.classname}CacheConfiguration;
</#list>

public class IgniteClientHelper implements AutoCloseable {

	public static final String SQL_SCHEMA = "${cacheschema}";

	private final Ignite ignite;
	
	<#list tables as table>
	IgniteCache<${table.keytype}, ${table.classname}> ${table.name}Cache;
	<#sep>
	
	</#sep>
	</#list>

	public static void main(String args[]) throws Exception {
		try (IgniteClientHelper ich = new IgniteClientHelper()) {
			System.out.println("IgniteClientHelper");
		}
	}

	public IgniteClientHelper() throws Exception {
		this(true);
	}

	public IgniteClientHelper(boolean destroyCaches) throws Exception {
		System.setProperty("IGNITE_QUIET", "true");

		IgniteConfiguration cfg = new DemoConfiguration();
		cfg.setClientMode(true);

		ignite = Ignition.start(cfg);
		ignite.cluster().state(ACTIVE);
		ignite.cluster().tag("Demo Cluster");

		if (destroyCaches) {
			System.out.println("Deleting Caches...");
	    	<#list tables as table>
			ignite.destroyCache("${table.name}");
			</#list>
		}
		System.out.println("Creating Caches...");
    	<#list tables as table>
		${table.name}Cache = ignite.getOrCreateCache(new ${table.classname}CacheConfiguration<${table.keytype}, ${table.classname}>());
		<#sep>
		
		</#sep>
		</#list>
		
		if (destroyCaches) {
			System.out.println("Loading Caches...");
    		<#list tables as table>
			${table.name}Cache.loadCache(null);
			<#sep>
			
			</#sep>
			</#list>
		}
	}
	
	<#list tables as table>
	public IgniteCache<${table.keytype}, ${table.classname}> get${table.classname}Cache() {
		return ${table.name}Cache;
	}
	<#sep>
	
	</#sep>
	</#list>

	public Ignite getIgnite() {
		return ignite;
	}

	@Override
	public void close() throws Exception {
		ignite.close();
	}


}
