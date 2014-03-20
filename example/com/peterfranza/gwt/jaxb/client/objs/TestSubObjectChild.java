package com.peterfranza.gwt.jaxb.client.objs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="TestSubObjectChild")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestSubObjectChild extends TestSubObject {

	@XmlAttribute
	public String attrtwo;

}
