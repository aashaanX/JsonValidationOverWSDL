package com.devside.esb.json.model;

import java.util.LinkedList;
import java.util.List;

public class SequenceSchema {

	List<ElementSchema> participants = new LinkedList<ElementSchema>();
	
	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		for (ElementSchema eSchema : participants) {
			sBuilder.append(eSchema);
		}
		return sBuilder.toString().substring(0,sBuilder.length()-1);
	}
	
}
