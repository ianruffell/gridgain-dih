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
import ${package_pojo}.${table.classname};
import ${package_app}.IgniteClientHelper;

@Path("${table.name}/{${table.keynameLc}}")
@Produces("application/json")
public class ${table.classname}Resource {

	@Inject
	IgniteClientHelper ich;

	private static final ObjectMapper mapper = new ObjectMapper();

	@GET
	public ${table.classname} get${table.classname}(@PathParam("${table.keynameLc}") ${table.keytype} ${table.keynameLc}) throws JsonProcessingException {
		${table.classname} ${table.name} = ich.get${table.classname}Cache().get(${table.keynameLc});
		return ${table.name};
	}

	@POST
	@Consumes("application/json")
	public ${table.classname} add${table.classname}(@PathParam("${table.keynameLc}") ${table.keytype} ${table.keynameLc}, String json) throws JsonProcessingException {
		${table.classname} ${table.name} = mapper.readValue(json, ${table.classname}.class);
		ich.get${table.classname}Cache().put(${table.keynameLc}, ${table.name});

		return ${table.name};
	}

	@DELETE
	public void delete${table.classname}(@PathParam("${table.keynameLc}") ${table.keytype} ${table.keynameLc}) {
		ich.get${table.classname}Cache().remove(${table.keynameLc});
	}
}
