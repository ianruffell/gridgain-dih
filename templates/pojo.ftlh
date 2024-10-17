package ${package_pojo};

import org.apache.ignite.cache.query.annotations.QuerySqlField;
<#if table.containsTimestamp == true>
import java.sql.Timestamp;
</#if>
import java.sql.Date;

public class ${table.classname} {

	<#list table.columns as column>
	@QuerySqlField
	private ${column.ignitetype} ${column.name};
	</#list>

	public ${table.classname} () {
	}

    public ${table.classname} (<#list table.columns as column>${column.ignitetype} ${column.name}<#sep>, </#sep></#list>) {
    	<#list table.columns as column>
		this.${column.name} = ${column.name};
		</#list>
    }

	<#list table.columns as column>
    public ${column.ignitetype} get${column.ccname}() {
        return ${column.name};
    }

    public void set${column.ccname}(${column.ignitetype} ${column.name}) {
        this.${column.name} = ${column.name};
    }
	</#list>

}