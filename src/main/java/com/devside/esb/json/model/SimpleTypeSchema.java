package com.devside.esb.json.model;

import java.util.HashSet;
import java.util.Set;

import com.predic8.schema.SimpleType;
import com.predic8.schema.restriction.BaseRestriction;
import com.predic8.schema.restriction.DurationRestriction;
import com.predic8.schema.restriction.IntRestriction;
import com.predic8.schema.restriction.StringRestriction;
import com.predic8.schema.restriction.facet.EnumerationFacet;
import com.predic8.schema.restriction.facet.Facet;
import com.predic8.schema.restriction.facet.PatternFacet;


public class SimpleTypeSchema extends TypeSchema{
	
	
	BaseRestriction restriction;
	Set<String> valueSet = new HashSet<String>();
	
	public SimpleTypeSchema(SimpleType simpleType,DefinitionDictionary dictionary) {
		super(simpleType,dictionary);
		restriction = simpleType.getRestriction();
	}
	
	@Override
	protected String getSuffix() {
		return "_ST";
	}
	
	@Override
	public String toString() {
		return regularToString();
	}
	
	public String regularToString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("\""+name+"\":");
		sBuilder.append("{");
		if (restriction instanceof StringRestriction){
			StringRestriction sRest = (StringRestriction) restriction;
			sBuilder.append("\"type\":\"string\",");
			if (!sRest.getEnumerationFacets().isEmpty()){
				StringBuilder rBuilder = new StringBuilder();
				for (EnumerationFacet entry : sRest.getEnumerationFacets()) {
					if (rBuilder.length() > 0) rBuilder.append(",");
					rBuilder.append("\"" + entry.getValue() + "\"");
				}
				sBuilder.append("\"enum\":["+ rBuilder.toString()+"],");
			}
		} else if (restriction instanceof IntRestriction){
			IntRestriction iRest = (IntRestriction) restriction;
			sBuilder.append("\"type\":\"string\",");
			if (!iRest.getEnumerationFacets().isEmpty()){
				StringBuilder rBuilder = new StringBuilder();
				for (EnumerationFacet entry : iRest.getEnumerationFacets()) {
					if (rBuilder.length() > 0) rBuilder.append(",");
					rBuilder.append( entry.getValue());
				}
				sBuilder.append("\"enum\":["+ rBuilder.toString()+"],");
			}
		} else if (restriction instanceof DurationRestriction){
			DurationRestriction dRest = (DurationRestriction) restriction;
			sBuilder.append("\"type\":\"string\",");
			if (!dRest.getFacets().isEmpty()){
				StringBuilder rBuilder = new StringBuilder();
				for (Facet entry : dRest.getEnumerationFacets()) {
					if (entry instanceof PatternFacet)
						sBuilder.append("\"pattern\":\"^"+ rBuilder.toString()+"$\",");
				}
				
			}
		}
		sBuilder.append("\"id\":\"" + dictionary.getSchema().getTargetNamespace() + "/" + name + "\"");
		sBuilder.append("}");
		return sBuilder.toString();
	}
	
}
