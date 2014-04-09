package com.peterfranza.gwt.jaxb.client.parser.utils;

import com.google.gwt.xml.client.Element;

public interface XMLNodeFactory<T> {

	T build(Element e);
	
}
