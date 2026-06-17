package com.eeshanoor.utils;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import java.io.InputStream;

/**
 * Utility for JSON schema validation in API tests.
 */
public class SchemaValidator {

    public static void validate(Response response, String schemaFileName) {
        InputStream schema = SchemaValidator.class
            .getClassLoader()
            .getResourceAsStream("schemas/" + schemaFileName);
        if (schema == null) {
            throw new IllegalArgumentException("Schema not found: " + schemaFileName);
        }
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schema));
    }
}