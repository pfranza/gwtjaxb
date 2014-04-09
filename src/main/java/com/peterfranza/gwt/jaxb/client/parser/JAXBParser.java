package com.peterfranza.gwt.jaxb.client.parser;

public interface JAXBParser<T> {

	T parse(String xml);

}
