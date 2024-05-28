openapi: 3.0.0
info:
  title: GridGain Digital Itegration Hub App
  version: 1.0.0
  description: GridGain Digital Itegration Hub App
paths:
<#list tables as table>
  /${table.name}/{${table.keyname}}:
    get:
      description: A single ${table.classname}.
      summary: Get a ${table.classname} by ID
      operationId: get${table.classname}
      parameters:
        - name: ${table.keyname}
          in: path
          description: ${table.keyname} of ${table.classname} to fetch
          required: true
          schema:
            type: ${table.keytypeLc}
      responses:
        '200':
          description: ${table.classname} response
          content:
            application/json:
              schema:
                type: object
                items:
                  $ref: '#/components/schemas/${table.classname}'
    post:
      description: Create a ${table.classname}
      summary: Create a ${table.classname}
      operationId: add${table.classname}
      parameters:
        - name: ${table.keyname}
          in: path
          description: ${table.keyname} of ${table.classname} to add
          required: true
          schema:
            type: ${table.keytypeLc}
      requestBody:
        description: ${table.classname} to add
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/${table.classname}'
      responses:
        '200':
          description: ${table.classname} response
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/${table.classname}'
        default:
          description: unexpected error
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Error'
    delete:
      description: deletes a single ${table.classname} based on the ${table.keyname} supplied
      summary: Delete a ${table.classname}
      operationId: delete${table.classname}
      parameters:
        - name: ${table.keyname}
          in: path
          description: ${table.keyname} of ${table.classname} to delete
          required: true
          schema:
            type: ${table.keytypeLc}
      responses:
        '204':
          description: ${table.classname} deleted
        default:
          description: unexpected error
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Error'
</#list>
components:
  schemas:
<#list tables as table>
    ${table.classname}:
      type: object
      properties:
        <#list table.columns as column>
        ${column.name}:
          type: ${column.openapiType}
        </#list>
</#list>
    Error:
      type: object
      required:
        - code
        - message
      properties:
        code:
          type: integer
        message:
          type: string