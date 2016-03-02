package com.devside.esb.json.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.predic8.schema.ComplexType;
import com.predic8.schema.Element;
import com.predic8.schema.Schema;
import com.predic8.schema.SchemaComponent;
import com.predic8.schema.Sequence;


public class ComplexTypeSchema extends TypeSchema {
	
	
	List<ElementSchema> properties = new  LinkedList<ElementSchema>();
	
	
	public ComplexTypeSchema(ComplexType complexType,DefinitionDictionary dictionary,List<DefinitionDictionary> dictionaryList,Element wrapperElement) {
		this(complexType, dictionary, dictionaryList);
		this.name = wrapperElement.getName() + "embedded";
		this.qName = wrapperElement.getQname().toString() + "_embedded" ;
	}
	
	public ComplexTypeSchema(ComplexType complexType,DefinitionDictionary dictionary,List<DefinitionDictionary> dictionaryList) {
		super(complexType,dictionary);
		SchemaComponent component = complexType.getModel();
		if (component instanceof Sequence){
			Sequence sequence = (Sequence) component;
			for (SchemaComponent sComp : sequence.getParticles()) {
				ElementSchema e = new ElementSchema((Element)sComp, dictionary,dictionaryList);
				properties.add(e);
			}
		} 
	}
	
	@Override
	protected String getSuffix() {
		return "_CT";
	}
	@Override
	public String toString() {
		if (name.toLowerCase().startsWith("arrayof") 
				&& properties.size() == 1
				&& properties.get(0).maxOccurs.equals("unbounded") )
			return arrayToString();
		return regularToString();
	}
	
	private String arrayToString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("\""+name+"\":");
		sBuilder.append("{");
		sBuilder.append("\"type\":\"array\",");
		sBuilder.append("\"items\":{");
		ElementSchema arrayItem = properties.get(0);
		if (arrayItem.type !=null){
			if (!arrayItem.customType){
				sBuilder.append("\"type\":\"" + arrayItem.getConvertedType() + "\"");
				sBuilder.append(",");
				String tmpId = "wsdl.namespace";
				if (dictionary.getSchema() != null)
					tmpId = dictionary.getSchema().getTargetNamespace();
				sBuilder.append("\"id\":\"" + tmpId + "/" + name + "\"");
			}else {
				sBuilder.append("\"$ref\":\"" + arrayItem.type + "\"");
			}
		}
		sBuilder.append("}");
		sBuilder.append("}");
		return sBuilder.toString();
	}
	
	
	public String regularToString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("\""+name+"\":");
		sBuilder.append("{");
		sBuilder.append("\"type\":\"object\"");
		sBuilder.append(",");
		sBuilder.append("\"additionalProperties\":false,");
		sBuilder.append("\"id\":\"" + dictionary.getSchema().getTargetNamespace() + "/" + name + "\"");
		boolean hasRequired = false;
		if (!properties.isEmpty()){
			sBuilder.append(","); 
			sBuilder.append("\"properties\":{");
			int i = 0;
			for (ElementSchema e : properties) {
				if (i!=0) sBuilder.append(",");
				sBuilder.append(e);
				if (e.isRequired()) hasRequired = true;
				i++;
			}
			sBuilder.append("}");
		}
		if (hasRequired){
			sBuilder.append(","); 
			sBuilder.append("\"required\":[");
			int i = 0;
			for (ElementSchema e : properties) {
				if (!e.isRequired()) continue;
				if (i!=0) sBuilder.append(",");
				sBuilder.append("\"" + e.name + "\"");
				i++;
			}
			sBuilder.append("]");
		}
		
		sBuilder.append("}");
		return sBuilder.toString();
	}
	
}
