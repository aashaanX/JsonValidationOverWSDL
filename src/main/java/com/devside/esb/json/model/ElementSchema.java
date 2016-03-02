package com.devside.esb.json.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.codehaus.groovy.transform.tailrec.HasRecursiveCalls;
import org.mockito.internal.exceptions.util.ScenarioPrinter;

import com.predic8.schema.ComplexType;
import com.predic8.schema.Element;
import com.predic8.schema.Schema;
import com.predic8.schema.SchemaComponent;
import com.predic8.schema.Sequence;

public class ElementSchema {
	
	Schema schema;
	String name;
	boolean required;
	String minOccurs;
	String maxOccurs;
	String type;
	boolean customType = false;
	List<ElementSchema> properties = new  LinkedList<ElementSchema>();
	
	
	public ElementSchema(Element e,DefinitionDictionary dictionary,List<DefinitionDictionary> dicList) {
		this.name = e.getName();
		this.minOccurs = e.getMinOccurs();
		this.maxOccurs = e.getMaxOccurs();
		this.required = !e.getMinOccurs().equals("0");
		
		TypeSchema typeSchema = null;
		if (e.getType() != null){
			for (DefinitionDictionary dict : dicList) {
				typeSchema = dict.getType(e.getType().toString());
				if (typeSchema != null){
					this.schema = dict.getSchema();
					break;
				}
			}
		} 
		if (typeSchema == null && dictionary != null && e.getType()!= null){
			typeSchema = dictionary.getType(e.getType().toString());
			if (typeSchema != null){
				this.schema = dictionary.getSchema();
			}
		}
		
		if (typeSchema == null && e.getType()== null){// embedded
			if (dictionary != null){
				typeSchema = dictionary.getType(e.getQname().toString() + "_embedded");
				this.schema = dictionary.getSchema();
			}else {
				for (DefinitionDictionary dict : dicList) {
					typeSchema = dict.getType(e.getQname().toString() + "_embedded");
					if (typeSchema != null){
						this.schema = dict.getSchema();
						break;
					}
				}
			}
		}
		
		if (typeSchema != null){
			customType = true;
			type = "#/definitions/" + typeSchema.getName();
		}else {
			if (dictionary != null)
				this.schema = dictionary.getSchema();
			if (e.getType() != null && type == null)
				this.type = e.getType().getLocalPart();
		}
		
		
	}
	
	String getConvertedType(){
		if (type.equals("byte") 
				|| type.equals("double")
				|| type.equals("int")
				|| type.equals("decimal")
				|| type.equals("float")
				|| type.equals("long")
				|| type.equals("short")
				|| type.equals("char")
				|| type.equals("unsignedByte")
				|| type.equals("unsignedInt")
				|| type.equals("unsignedLong")
				|| type.equals("unsignedShort")
				|| type.equals("short")
				){
			return "number";
		}else if (type.equals("QName")
				|| type.equals("dateTime")
				|| type.equals("base64Binary")
				|| type.equals("char")
				|| type.equals("duration")
				|| type.equals("guid")
				|| type.equals("anyURI")
				|| type.equals("string")){
			return "string";
		}else if (type.equals("boolean")){
			return "boolean";
		}else {
			return null;
		}
		
	}

	boolean isValidPrimitiveType(){
		return  !customType && getConvertedType() != null;
				
	}
	
	public String getName() {
		return name;
	}
	
	
	@Override
	public String toString() {
		if (name.toLowerCase().equals("arrayof") 
				&& properties.size() == 1
				&& properties.get(0).maxOccurs.equals("unbounded") )
			return arrayToString();
		return regularToString();
	}
	
	/**
	 * {@link Deprecated}
	 * @return
	 */
	private String arrayToString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("\""+name+"\":");
		sBuilder.append("{");
		sBuilder.append("\"type\":\"array\",");
		sBuilder.append("\"items\":{");
		if (type !=null){
			if (!customType){
				sBuilder.append("\"type\":\"" + getConvertedType() + "\"");
				sBuilder.append(",");
				String tmpId = "wsdl.namespace";
				if (schema != null)
					tmpId = schema.getTargetNamespace();
				sBuilder.append("\"id\":\"" + tmpId + "/" + name + "\"");
			}else {
				sBuilder.append("\"$ref\":\"" + type + "\"");
			}
		}
		sBuilder.append("}");
		sBuilder.append("}");
		return sBuilder.toString();
	}

	private String regularToString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("\""+name+"\":");
		sBuilder.append("{");
		if (maxOccurs.toLowerCase().equals("unbounded")){
			sBuilder.append("\"type\":\"array\",");
			sBuilder.append("\"items\":{");
		}
		if (type !=null){
			if (!customType){
				sBuilder.append("\"type\":\"" + getConvertedType() + "\"");
				sBuilder.append(",");
				String tmpId = "wsdl.namespace";
				if (schema != null)
					tmpId = schema.getTargetNamespace();
				sBuilder.append("\"id\":\"" + tmpId + "/" + name + "\"");
			}else {
				sBuilder.append("\"$ref\":\"" + type + "\"");
			}
		}
		if (maxOccurs.toLowerCase().equals("unbounded")){
			sBuilder.append("}");
		}
		sBuilder.append("}");
		return sBuilder.toString();
	}
	
	public boolean isRequired() {
		return required;
	}
	

}
