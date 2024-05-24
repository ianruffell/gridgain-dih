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
import com.gridgain.dih.kafka.model.Product;

@Path("product/{id}")
@Produces("application/json")
public class ProductResource {

	@Inject
	KafkaCacheHelper kch;

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

	@GET
	public String getProduct(@PathParam("id") String id) throws JsonProcessingException {
		String json = "{}";
		Product product = kch.getProductCache().get(id);
		if (product != null) {
			json = ow.writeValueAsString(product);
		}
		return json;
	}

	@POST
	@Consumes("application/json")
	@Produces("text/html")
	public String addProduct(@PathParam("id") String id, String json) throws JsonProcessingException {
		System.out.println("");
		Product value = mapper.readValue(json, Product.class);
		kch.getProductCache().put(id, value);

		return "ok";
	}

	@DELETE
	@Produces("text/html")
	public String deleteProduct(@PathParam("id") String id) {
		kch.getProductCache().remove(id);
		return "ok";
	}
}
