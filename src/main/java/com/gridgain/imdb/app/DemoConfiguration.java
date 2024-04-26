package com.gridgain.imdb.app;

import static org.apache.ignite.configuration.DeploymentMode.CONTINUOUS;

import java.util.ArrayList;

import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;

public class DemoConfiguration extends IgniteConfiguration {

	public DemoConfiguration() {
		System.setProperty("java.net.preferIPv4Stack", "true");

		setWorkDirectory("/tmp/ignitework");
		setPeerClassLoadingEnabled(true);
		setDeploymentMode(CONTINUOUS);

		TcpDiscoverySpi tcpDiscoverySpi = new org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi();
		TcpDiscoveryVmIpFinder tcpDiscoveryVmIpFinder = new org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder();
		ArrayList<String> list = new ArrayList<String>();
		list.add("127.0.0.1:47500..47510");

		tcpDiscoveryVmIpFinder.setAddresses(list);
		tcpDiscoverySpi.setIpFinder(tcpDiscoveryVmIpFinder);

		setDiscoverySpi(tcpDiscoverySpi);

		// Uncomment for EE or UE
		// GridGainConfiguration gainConfiguration = new GridGainConfiguration();
		// gainConfiguration.setLicenseUrl("gridgain-license.xml");
		// setPluginConfigurations(gainConfiguration);
	}

}
