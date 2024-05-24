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
import com.gridgain.dih.kafka.model.ProductPrice;

@Path("roductprice/{id}")
@Produces("application/json")
public class ProductPriceResource {

	@Inject
	KafkaCacheHelper kch;

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

	@GET
	public String getProductPrice(@PathParam("id") String id) throws JsonProcessingException {
		String json = "{}";
		ProductPrice roductprice = kch.getProductPriceCache().get(id);
		if (roductprice != null) {
			json = ow.writeValueAsString(roductprice);
		}
		return json;
	}

	@POST
	@Consumes("application/json")
	@Produces("text/html")
	public String addProductPrice(@PathParam("id") String id, String json) throws JsonProcessingException {
		System.out.println("");
		ProductPrice value = mapper.readValue(json, ProductPrice.class);
		kch.getProductPriceCache().put(id, value);

		return "ok";
	}

	@DELETE
	@Produces("text/html")
	public String deleteProductPrice(@PathParam("id") String id) {
		kch.getProductPriceCache().remove(id);
		return "ok";
	}
}
