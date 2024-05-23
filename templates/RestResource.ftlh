package ${package_rest};

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
import ${package_pojo}.${table.classname};
import ${package_app}.IgniteClientHelper;

@Path("${table.name}/{${table.keynameLc}}")
@Produces("application/json")
public class ${table.classname}Resource {

	@Inject
	IgniteClientHelper ich;

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

	@GET
	public String get${table.classname}(@PathParam("id") ${table.keytype} ${table.keynameLc}) throws JsonProcessingException {
		String json = "{}";
		${table.classname} ${table.name} = ich.get${table.classname}Cache().get(${table.keynameLc});
		if (${table.name} != null) {
			json = ow.writeValueAsString(${table.name});
		}
		return json;
	}

	@POST
	@Consumes("application/json")
	@Produces("text/html")
	public String add${table.classname}(@PathParam("${table.keynameLc}") ${table.keytype} ${table.keynameLc}, String json) throws JsonProcessingException {
		System.out.println("");
		${table.classname} value = mapper.readValue(json, ${table.classname}.class);
		ich.get${table.classname}Cache().put(${table.keynameLc}, value);

		return "ok";
	}

	@DELETE
	@Produces("text/html")
	public String delete${table.classname}(@PathParam("${table.keynameLc}") ${table.keytype} ${table.keynameLc}) {
		ich.get${table.classname}Cache().remove(${table.keynameLc});
		return "ok";
	}
}
