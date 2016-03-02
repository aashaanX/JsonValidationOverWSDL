package com.devside.esb.json.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.predic8.schema.ComplexType;
import com.predic8.schema.Element;
import com.predic8.schema.Schema;
import com.predic8.schema.SchemaComponent;
import com.predic8.schema.Sequence;
import com.predic8.wsdl.Definitions;

public class SchemaDependencyResolver extends DependencyResolver<DefinitionDictionary, Definitions> {
	
	protected void initDependency(Definitions def,Map<String, DependencyNode<DefinitionDictionary>> dbcTypes) {
		List<Schema> schemas = def.getSchemas();
		
		for (Schema schema : schemas){
			
			List<ComplexType> complexTypes = schema.getComplexTypes();
			DependencyNode<DefinitionDictionary> pdbcType = new DependencyNode<DefinitionDictionary>(schema.getTargetNamespace());
			dbcTypes.put(schema.getTargetNamespace(), pdbcType);
			for (ComplexType complexType : complexTypes) {
		 		SchemaComponent component = complexType.getModel();
				if (component instanceof Sequence){
					Sequence sequence = (Sequence) component;
					for (SchemaComponent sComp : sequence.getParticles()) {
						Element e = (Element)sComp;
						Iterator<Entry<String, String>> iterator = ((Map<String , String >)e.getNamespaceContext()).entrySet().iterator();
						while (iterator.hasNext()) {
							String ns = ((Map.Entry<String, String>) iterator.next()).getValue();
							if (ns.equals(schema.getTargetNamespace()))
								continue;
							DependencyNode<DefinitionDictionary> dbcType =  dbcTypes.get(ns);
					 		if (dbcType == null)
					 			dbcType = new DependencyNode<DefinitionDictionary>(ns);
					 		dbcTypes.put(dbcType.qName,dbcType);
					 		pdbcType.addDependency(dbcType);
						}
						
					}
				} 
			}
		}
		
	}

	

}
