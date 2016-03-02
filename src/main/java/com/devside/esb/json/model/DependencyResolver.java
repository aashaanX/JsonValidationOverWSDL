package com.devside.esb.json.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class DependencyResolver<P,I> {
	
	protected class DependencyNode<P> {
		DependencyNode(String qName) {
			this.qName = qName;
		}
		String qName;
		Set<DependencyNode<P>> dependencySet = new HashSet<DependencyNode<P>>();
		
		void addDependency(DependencyNode<P> d){
			dependencySet.add(d);
		}
	}
	
	public List<String> resolve(I instance){
		Map<String,DependencyNode<P>> dbcTypes = new HashMap<String,DependencyNode<P>>();
		
		initDependency(instance, dbcTypes);
		
		List<String> qNameList = new ArrayList<String>();
		Iterator<Entry<String, DependencyNode<P>>> iterator = dbcTypes.entrySet().iterator();
		while (iterator.hasNext()) {
			DependencyNode<P> dbct = (DependencyNode<P>) iterator
					.next().getValue();
			dive(dbct, qNameList);
		}
		
		return qNameList;
	}

	protected abstract void initDependency(I instance,Map<String, DependencyNode<P>> dbcTypes) ;
	
	private void dive(DependencyNode<P> dbct,List<String> qNameList){
		if (!dbct.dependencySet.isEmpty()){
			for (DependencyNode<P> u : dbct.dependencySet) {
				dive(u, qNameList);
			}
			controlAndAdd(dbct.qName, qNameList);
		}else {
			controlAndAdd(dbct.qName, qNameList);
		}
	}
	
	private void controlAndAdd(String qName,List<String> qNameList){
		for (String string : qNameList) {
			if (string.equals(qName))
				return;
		}
		qNameList.add(qName);
	}
	
}
