package com.devside.esb.json.model;

import java.util.List;

import com.predic8.schema.SimpleType;
import com.predic8.schema.TypeDefinition;
import com.predic8.schema.restriction.BaseRestriction;

public abstract class TypeSchema {
	
	protected String name;
	protected String qName;
	protected DefinitionDictionary dictionary;

	protected TypeSchema(TypeDefinition t,DefinitionDictionary dictionary) {
		this.dictionary = dictionary;
		this.name = t.getName() + getSuffix();
		if (t.getQname() !=null ) // complex type
			this.qName = t.getQname().toString();
		
	}
	
	protected abstract String getSuffix();
	
	public String getQName() {
		return qName;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isComplex(){
		return ((this instanceof ComplexTypeSchema) ? true: false);
	}
}
