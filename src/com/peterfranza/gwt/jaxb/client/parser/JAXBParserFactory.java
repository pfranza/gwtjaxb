package com.peterfranza.gwt.jaxb.client.parser;

public interface JAXBParserFactory<T> {

	JAXBParser<T> create();
	
}
