package com.devside.esb.json.model;

import junit.framework.TestCase;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;

import com.devside.esb.json.model.JSONSchemaGenerator;
import com.devside.esb.json.model.TypeSchema;

public class SampleServiceTest extends TestCase {

	Schema schema;
	protected void setUp(){
		schema = JSONSchemaGenerator.generate(TypeSchema.class.getClassLoader().getResource("sample-service.wsdl").getPath());
	}
	
	public void testEchoSuccess(){
		schema.validate(new JSONObject("{\"username\":\"user01\",\"password\":\"pass01\"}"));
	}
	
	public void testEchoAdditionalField(){
		try {
			schema.validate(new JSONObject("{\"username\":\"user01\",\"password\":\"pass01\",\"falan\":\"filan\"}"));
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(e instanceof ValidationException);
		}
		
		
	}
	
//	public void testEchoMissingField(){
//		try {
//			Schema schema = JSONSchemaGenerator.generate(new Runny().getClass().getClassLoader().getResource("echo.wsdl").getPath());
//			schema.validate(new JSONObject("{}"));
//			assertTrue(false);
//		} catch (Exception e) {
//			assertTrue(e instanceof ValidationException);
//		}
//		
//	}
	
}
