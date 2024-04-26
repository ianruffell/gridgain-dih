package com.gridgain.imdb.app;

import org.apache.ignite.Ignition;

public class IgniteServer {
	public static void main(String[] args) {

		DemoConfiguration cfg = new DemoConfiguration();
		Ignition.start(cfg);
	}
}
