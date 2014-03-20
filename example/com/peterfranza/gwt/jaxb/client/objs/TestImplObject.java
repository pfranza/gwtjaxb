package com.peterfranza.gwt.jaxb.client.objs;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class TestImplObject extends TestAbstObject {

	@XmlAttribute
	public boolean val;
	
}
