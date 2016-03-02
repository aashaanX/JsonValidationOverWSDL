package com.devside.esb.json.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.predic8.schema.ComplexType;
import com.predic8.schema.Element;
import com.predic8.schema.Schema;
import com.predic8.schema.SimpleType;
import com.predic8.schema.TypeDefinition;

public class DefinitionDictionary {
	
	Schema schema;
	List<TypeSchema> typeMap = new ArrayList<TypeSchema>();
	List<ElementSchema> schemaElements = new LinkedList<ElementSchema>();
	
	
	public DefinitionDictionary(Schema schema,List<DefinitionDictionary> dictList) {
		this.schema = schema;


	 	List<SimpleType> simpleTypes = schema.getSimpleTypes();
	 	for (SimpleType simpleType : simpleTypes) {
	 		SimpleTypeSchema sTypeSchema = new SimpleTypeSchema(simpleType, this);  
	 		typeMap.add(sTypeSchema);
			System.out.println("name : " + simpleType.getName() + " class : " + simpleType.getRestriction() );
		}
		
		List<String> calculateDependency = new ComplexTypeDependencyResolver().resolve(schema);
		
	 	List<ComplexType> complexTypes = schema.getComplexTypes();
	 	for (String s : calculateDependency) {
	 		for (ComplexType complexType : complexTypes) {
	 			if (s.equals(complexType.getQname().toString())){
	 				ComplexTypeSchema cTypeSchema = new ComplexTypeSchema(complexType,this,dictList);
	 				typeMap.add(cTypeSchema);
	 			}
			}
		}

	 	for (Element e : schema.getElements()) {
	 		if (e.getEmbeddedType()!= null){
	 			TypeDefinition embeddedType = e.getEmbeddedType();
	 			if (embeddedType instanceof ComplexType){
	 				ComplexType c = (ComplexType) embeddedType;
	 				ComplexTypeSchema cTypeSchema = new ComplexTypeSchema(c,this,dictList,e);
	 				typeMap.add(cTypeSchema);
	 			}else if (embeddedType instanceof SimpleType){}
	 		}
			schemaElements.add(new ElementSchema(e, this,dictList));
		}
	
	}
	
	public TypeSchema getType(String qName){
		for(TypeSchema ctSchema : typeMap){
			if (qName.equals(ctSchema.getQName()))
				return ctSchema;
		}
		return null;
	}
	
	public List<ElementSchema> getSchemaElements() {
		return schemaElements;
	}
	
	public int getTotalCount(){
		return typeMap.size() + schemaElements.size();
	}
	
	@Override
	public String toString() {
		int i = 0;
		Iterator<TypeSchema> iterator = typeMap.iterator();
		StringBuilder sBuilder = new StringBuilder();
		while (iterator.hasNext()) {
			TypeSchema cType = (TypeSchema) iterator.next();
			if (i != 0) sBuilder.append(",");
			sBuilder.append(cType);
			i++;
		}
		return sBuilder.toString();
	}
	
	public Schema getSchema() {
		return schema;
	}

}
