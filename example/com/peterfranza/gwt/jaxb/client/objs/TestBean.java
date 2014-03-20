package com.peterfranza.gwt.jaxb.client.objs;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.peterfranza.gwt.jaxb.client.JAXBExamples.TESTENUM;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TestBean {

	
	
	@XmlAttribute
	public String attrone;
	
	@XmlAttribute(name="attrtwo")
	protected String attr2;
	
	@XmlAttribute
	private String attrthree;
	
	@XmlAttribute 
	public int attrint;
	
	@XmlAttribute 
	public Integer attrinteger;
	
	@XmlAttribute 
	public long attrlong;
	
	@XmlAttribute 
	public Long attrLong;
	
	@XmlAttribute
	public short attrshort;
	
	@XmlAttribute 
	public Short attrShort;
	
	@XmlAttribute 
	public float attrfloat;
	
	@XmlAttribute 
	public Float attrFloat;
	
	@XmlAttribute 
	public double attrdouble;
	
	@XmlAttribute 
	public Double attrDouble;
	
	@XmlAttribute 
	public boolean attrboolean;
	
	@XmlAttribute 
	public Boolean attrBoolean;
	
	@XmlElement
	public TestSubObject publicSubObject;
	
	@XmlElement
	public ArrayList<TestSubObject> objectList;
	
	@XmlElement
	public ArrayList<TestSubObject> namedList;
	
	@XmlElement
	public ArrayList<String> stringsList;
	
	@XmlTransient
	public String transfield;
	
	@XmlElement
	public TestStringValue value;
	
	@XmlAttribute
	public TESTENUM attrEnum;
	
	@XmlElement
	public TESTENUM elemEnum;
	
	@XmlElement
	public String elemString;
	
	@XmlElement 
	public TestAbstObject abstractObj;
	
	public String getAttr2() {
		return attr2;
	}
	
	public String getAttrthree() {
		return attrthree;
	}
	
	public void setAttrthree(String attrthree) {
		this.attrthree = attrthree;
	}
	
}
