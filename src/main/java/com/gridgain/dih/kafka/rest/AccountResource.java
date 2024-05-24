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
import com.gridgain.dih.kafka.model.Account;

@Path("account/{id}")
@Produces("application/json")
public class AccountResource {

	@Inject
	KafkaCacheHelper kch;

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

	@GET
	public String getAccount(@PathParam("id") String id) throws JsonProcessingException {
		String json = "{}";
		Account account = kch.getAccountCache().get(id);
		if (account != null) {
			json = ow.writeValueAsString(account);
		}
		return json;
	}

	@POST
	@Consumes("application/json")
	@Produces("text/html")
	public String addAccount(@PathParam("id") String id, String json) throws JsonProcessingException {
		System.out.println("");
		Account value = mapper.readValue(json, Account.class);
		kch.getAccountCache().put(id, value);

		return "ok";
	}

	@DELETE
	@Produces("text/html")
	public String deleteAccount(@PathParam("id") String id) {
		kch.getAccountCache().remove(id);
		return "ok";
	}
}
