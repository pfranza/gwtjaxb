package com.peterfranza.gwt.jaxb.client.objs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(name="StringValue")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestStringValue {

	@XmlAttribute
	public boolean encoded;
	
	@XmlValue
	public String value;
	
}
