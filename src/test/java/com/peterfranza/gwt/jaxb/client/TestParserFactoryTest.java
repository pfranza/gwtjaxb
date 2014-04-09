package com.peterfranza.gwt.jaxb.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

public class TestParserFactoryTest extends GWTTestCase {

	private TestParserFactory p;

	@Override
	public String getModuleName() {
		return "com.peterfranza.gwt.jaxb.JAXBTest";
	}
	
	@Override
	protected void gwtSetUp() throws Exception {
		super.gwtSetUp();
		p = GWT.create(TestParserFactory.class);
	}
	
	public void testParseRoot() {	 
		MututalTests.testParseRoot(p);
	}
	
	public void testParseBadRoot() {
		MututalTests.testParseBadRoot(p);
	}
	
	public void testParseMisnamedRoot() {
		MututalTests.testParseMisnamedRoot(p);
	}
	
	public void testParsePublicAttribute() {
		MututalTests.testParsePublicAttribute(p);
	}
	
	public void testParseRenamedProtectedAttribute() {
		MututalTests.testParseRenamedProtectedAttribute(p);
	}
	
	public void testParsePrivateAttribute() {
		MututalTests.testParsePrivateAttribute(p);
	}
	
	public void testParseTypedAttribute() {
		MututalTests.testParseTypedAttribute(p);
	}
	
	public void testParseSubObjectAttribute() {
		MututalTests.testParseSubObjectAttribute(p);
	}
	
	public void testParseSubObjectList() {
		MututalTests.testParseSubObjectList(p);
	}
	
	public void testParseSubObjectPolymorphicList() {
		MututalTests.testParseSubObjectPolymorphicList(p);
	}
	
	public void testParseTransientAttribute() {
		MututalTests.testParseTransientAttribute(p);
	}
	
	public void testParseValueAttribute() {
		MututalTests.testParseValueAttribute(p);
	}
	
	public void testParseNamedSubObjectPolymorphicList() {
		MututalTests.testParseNamedSubObjectPolymorphicList(p);
	}
	
	public void testParseStringElement() {
		MututalTests.testParseStringElement(p);
	}
	
	public void testParseElementEnum() {
		MututalTests.testParseElementEnum(p);
	}
	
	public void testParseAttributeEnum() {
		MututalTests.testParseAttributeEnum(p);
	}

	public void testPolymprohicElement() {
		MututalTests.testPolymprohicElement(p);
	}
	
	public void testListStringElement() {
		MututalTests.testListStringElement(p);
	}
	
}
