package com.devside.esb.json.model;

import java.util.List;
import java.util.Map;

import com.devside.esb.json.model.DependencyResolver.DependencyNode;
import com.predic8.schema.ComplexType;
import com.predic8.schema.Element;
import com.predic8.schema.Schema;
import com.predic8.schema.SchemaComponent;
import com.predic8.schema.Sequence;

public class ComplexTypeDependencyResolver extends DependencyResolver<ComplexTypeSchema, Schema> {
	
	protected void initDependency(Schema schema,Map<String, DependencyNode<ComplexTypeSchema>> dbcTypes) {
		List<ComplexType> complexTypes = schema.getComplexTypes();
		for (ComplexType complexType : complexTypes) {
			DependencyNode<ComplexTypeSchema> pdbcType = dbcTypes.get(complexType.getQname().toString());
			if (pdbcType == null)
				pdbcType = new DependencyNode<ComplexTypeSchema>(complexType.getQname().toString());
			dbcTypes.put(pdbcType.qName,pdbcType);
	 		SchemaComponent component = complexType.getModel();
			if (component instanceof Sequence){
				Sequence sequence = (Sequence) component;
				for (SchemaComponent sComp : sequence.getParticles()) {
					Element e = (Element)sComp;
					DependencyNode<ComplexTypeSchema> dbcType =  dbcTypes.get(e.getType().toString());
			 		if (dbcType == null)
			 			dbcType = new DependencyNode<ComplexTypeSchema>(e.getType().toString());
			 		dbcTypes.put(dbcType.qName,dbcType);
			 		pdbcType.addDependency(dbcType);
				}
			} 
		}
	}

	

}
