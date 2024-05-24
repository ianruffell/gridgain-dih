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
import com.gridgain.dih.kafka.model.Trade;

@Path("trade/{id}")
@Produces("application/json")
public class TradeResource {

	@Inject
	KafkaCacheHelper kch;

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

	@GET
	public String getTrade(@PathParam("id") String id) throws JsonProcessingException {
		String json = "{}";
		Trade trade = kch.getTradeCache().get(id);
		if (trade != null) {
			json = ow.writeValueAsString(trade);
		}
		return json;
	}

	@POST
	@Consumes("application/json")
	@Produces("text/html")
	public String addTrade(@PathParam("id") String id, String json) throws JsonProcessingException {
		System.out.println("");
		Trade value = mapper.readValue(json, Trade.class);
		kch.getTradeCache().put(id, value);

		return "ok";
	}

	@DELETE
	@Produces("text/html")
	public String deleteTrade(@PathParam("id") String id) {
		kch.getTradeCache().remove(id);
		return "ok";
	}
}
