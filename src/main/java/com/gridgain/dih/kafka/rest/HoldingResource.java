package com.gridgain.dih.kafka.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gridgain.dih.kafka.KafkaCacheHelper;
import com.gridgain.dih.kafka.model.Holding;
import com.gridgain.dih.kafka.model.HoldingKey;

@Path("holding/{accountId}/{symbol}")
@Produces("application/json")
public class HoldingResource {

	@Inject
	KafkaCacheHelper kch;

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

	@GET
	public String getHolding(@PathParam("accountId") String accountId, @PathParam("symbol") String symbol)
			throws JsonProcessingException {
		String json = "{}";
		Holding holding = kch.getHoldingCache().get(new HoldingKey(accountId, symbol));
		if (holding != null) {
			json = ow.writeValueAsString(holding);
		}
		return json;
	}

	@POST
	@Consumes("application/json")
	@Produces("text/html")
	public String addHolding(@PathParam("accountId") String accountId, @PathParam("symbol") String symbol, String json)
			throws JsonProcessingException {
		System.out.println("");
		Holding value = mapper.readValue(json, Holding.class);
		kch.getHoldingCache().put(new HoldingKey(accountId, symbol), value);

		return "ok";
	}

	@DELETE
	@Produces("text/html")
	public String deleteHolding(@PathParam("accountId") String accountId, @PathParam("symbol") String symbol) {
		kch.getHoldingCache().remove(new HoldingKey(accountId, symbol));
		return "ok";
	}
}
