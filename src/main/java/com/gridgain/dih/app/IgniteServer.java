package com.gridgain.dih.app;

import org.apache.ignite.Ignition;

public class IgniteServer {
	public static void main(String[] args) {

		System.setProperty("IGNITE_QUIET", "true");

		DemoConfiguration cfg = new DemoConfiguration();
		Ignition.start(cfg);
	}
}
