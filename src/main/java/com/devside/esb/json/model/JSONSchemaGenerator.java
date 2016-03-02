package com.devside.esb.json.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;

import com.predic8.schema.Element;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.Part;
import com.predic8.wsdl.PortType;
import com.predic8.wsdl.WSDLParser;

public class JSONSchemaGenerator {
	
	
	public static Schema generate(String wsdlURL){

	    WSDLParser parser = new WSDLParser();
	 
	    Definitions defs = parser
	        .parse(wsdlURL);
	    
	    List<String> calculatedDependency = new SchemaDependencyResolver().resolve(defs);
	 
	    return process(defs,calculatedDependency);

	
		
		
	}
	
	public static Schema generate(InputStream is){

	    WSDLParser parser = new WSDLParser();
	 
	    Definitions defs = parser.parse(is);
	    
	    List<String> calculatedDependency = new SchemaDependencyResolver().resolve(defs);
	 
	    return process(defs,calculatedDependency);

	
		
		
	}
	
	private static void addSafe(List<ElementSchema> elements ,ElementSchema element){
		for (ElementSchema e : elements) {
			if (e.getName().equals(element.getName()))
				return;
		}
		elements.add(element);
	}

	private static Schema process(Definitions defs,List<String> calculatedDependency) {
		List<ElementSchema> elements = new ArrayList<ElementSchema>();
	    List<DefinitionDictionary> dicList = new LinkedList<DefinitionDictionary>();

	    for (String schemaNamespace : calculatedDependency) {
	    	for (com.predic8.schema.Schema schema : defs.getSchemas()){
	    		if (schemaNamespace.equals(schema.getTargetNamespace())){
	    			DefinitionDictionary dic = new DefinitionDictionary(schema,dicList);
	    			Iterator<DefinitionDictionary> iterator = dicList.iterator();
	    			boolean skipAdd = false;
	    			while (iterator.hasNext()) {
						DefinitionDictionary definitionDictionary = (DefinitionDictionary) iterator
								.next();
						if (definitionDictionary.getSchema().getTargetNamespace().equals(dic.getSchema().getTargetNamespace())){
							if (dic.getTotalCount() > definitionDictionary.getTotalCount()){
								iterator.remove();
								break;
							}else {
								skipAdd = true;
							}
						}
					}
	    			if (!skipAdd)
	    				dicList.add(dic);
	    		}
	    			
	 	    }
		}
	    
	    
	    
	    for (PortType pt : defs.getPortTypes()) {
	      for (Operation op : pt.getOperations()) {
	        for (Part part : op.getInput().getMessage().getParts()) {
	        	Element inputElement = part.getElement();
	        	if (inputElement == null){//literal
	        		inputElement = new Element();
	        		inputElement.setMinOccurs("1");
	        		inputElement.setMaxOccurs("1");
	        		inputElement.setName(part.getName());
	        		inputElement.setType(part.getType().getQname());
	        		
	        	}
	        	ElementSchema e = new ElementSchema(inputElement,null,dicList);
	 	        addSafe(elements, e);
			}
	        for (Part part : op.getOutput().getMessage().getParts()) {
	        	Element outputElement = part.getElement();
	        	if ( outputElement == null){//literal
	        		outputElement = new Element();
	        		outputElement.setMinOccurs("1");
	        		outputElement.setMaxOccurs("1");
	        		outputElement.setName(part.getName());
	        		outputElement.setType(part.getType().getQname());
	        		
	        	}
	        	ElementSchema e = new ElementSchema(outputElement,null,dicList);
	        	addSafe(elements, e);
	        }
	      }
	    }
	    
	    StringBuilder sBuilder = new StringBuilder();
	    for (ElementSchema e : elements) {
	    	if (sBuilder.length()> 0)sBuilder.append(",");
	    	sBuilder.append(e);
	    	for (DefinitionDictionary d : dicList) {
	    		Iterator<ElementSchema> iterator = d.getSchemaElements().iterator();
	    		while (iterator.hasNext()) {
					ElementSchema elementSchema = (ElementSchema) iterator
							.next();
					if (elementSchema.getName().equals(e.getName()))
						iterator.remove();
				}
	    	}
		}
	    StringBuilder eBuilder = new StringBuilder();
	    StringBuilder dBuilder = new StringBuilder();
	    for (DefinitionDictionary d : dicList) {
	    	if (d.toString().length() == 0) continue;
	    	if (dBuilder.length()>0)dBuilder.append(",");
	    	dBuilder.append(d);
	    	for (ElementSchema e : d.getSchemaElements()){
	    		if (eBuilder.length() > 0)eBuilder.append(",");
	    		eBuilder.append(e);
	    	}
		}
	    
	    String schemaStr = "{	\"$schema\": \"http://json-schema.org/draft-04/schema#\","
//	    		+  "\"id\": \"http://jsonschema.net\","
				+ "\"type\": \"object\","
				+ "\"additionalProperties\":false,"
	    		+  "\"definitions\": {"+ ((eBuilder.length() > 0)? eBuilder.toString() + "," : "") + dBuilder+"},"
				+ "\"properties\": {" +  sBuilder.toString() + "}}";
	    System.out.println(schemaStr);
		return SchemaLoader.load(new JSONObject(schemaStr));
	}

}
