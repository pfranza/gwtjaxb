package com.peterfranza.gwt.jaxb.client;

import junit.framework.Assert;

import com.peterfranza.gwt.jaxb.client.JAXBExamples.TESTENUM;
import com.peterfranza.gwt.jaxb.client.objs.TestBean;
import com.peterfranza.gwt.jaxb.client.objs.TestImplObject;
import com.peterfranza.gwt.jaxb.client.objs.TestSubNamedObject;
import com.peterfranza.gwt.jaxb.client.objs.TestSubObjectChild;

public class MututalTests extends Assert {

	public static void testParseRoot(TestParserFactory p) {
		TestBean bean = p.create().parse("<testBean />");
		assertNotNull(bean);
	}
	
	public static void testParseBadRoot(TestParserFactory p) {
		try {
			p.create().parse("<TestBeanBadRoot");
			fail();
		} catch(Exception e) {
			assertNotNull(e);
		}
	}
	
	public static void testParseMisnamedRoot(TestParserFactory p) {
		try {
			p.create().parse("<TestBeanBadName />");
			fail();
		} catch(Exception e) {
			assertNotNull(e);
		}
	}
	
	public static void testParsePublicAttribute(TestParserFactory p) {
		TestBean bean = p.create().parse("<testBean attrone=\"passed\"/>");
		assertNotNull(bean);
		assertEquals("passed", bean.attrone);
	}
	
	public static void testParseRenamedProtectedAttribute(TestParserFactory p) {
		TestBean bean = p.create().parse("<testBean attrtwo=\"passed\"/>");
		assertNotNull(bean);
		assertEquals("passed", bean.getAttr2());
	}
	
	public static void testParsePrivateAttribute(TestParserFactory p) {
		TestBean bean = p.create().parse("<testBean attrthree=\"passed\"/>");
		assertNotNull(bean);
		assertEquals("passed", bean.getAttrthree());
	}
	
	public static void testParseTypedAttribute(TestParserFactory p) {
		TestBean bean = p.create().parse("<testBean attrint=\"1\" attrinteger=\"2\"/>");
		assertNotNull(bean);
		assertEquals(1, bean.attrint);
		assertEquals(2, bean.attrinteger.intValue());
	}
	
	public static void testParseSubObjectAttribute(TestParserFactory p) {
		TestBean bean = p.create().parse("<testBean><publicSubObject attrone=\"passed\"/></testBean>");
		assertNotNull(bean);
		assertNotNull(bean.publicSubObject);
		assertEquals("passed", bean.publicSubObject.attrone);
	}
	
	public static void testParseSubObjectList(TestParserFactory p) {
		TestBean bean = p.create().parse("<testBean><objectList attrone=\"passed1\"/><objectList attrone=\"passed2\"/><objectList attrone=\"passed3\"/></testBean>");
		assertNotNull(bean);
		assertNotNull(bean.objectList);
		assertEquals(3, bean.objectList.size());
		assertEquals("passed1", bean.objectList.get(0).attrone);
		assertEquals("passed2", bean.objectList.get(1).attrone);
		assertEquals("passed3", bean.objectList.get(2).attrone);
	}
	
	public static void testParseSubObjectPolymorphicList(TestParserFactory p) {
		TestBean bean = p.create().parse("<testBean><objectList attrone=\"passed1\"/><objectList xsi:type=\"TestSubObjectChild\" attrtwo=\"passed3\" attrone=\"passed2\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/></testBean>");
		assertNotNull(bean);
		assertNotNull(bean.objectList);
		assertEquals(2, bean.objectList.size());
		assertEquals("passed1", bean.objectList.get(0).attrone);
		assertEquals("passed2", ((TestSubObjectChild)bean.objectList.get(1)).attrone);
		assertEquals("passed3", ((TestSubObjectChild)bean.objectList.get(1)).attrtwo);
	}
	
	public static void testParseTransientAttribute(TestParserFactory p) {
		TestBean bean = p.create().parse("<testBean transfield=\"false\"/>");
		assertNotNull(bean);
		assertNull(bean.transfield);
	}
	
	public static void testParseValueAttribute(TestParserFactory p) {
		TestBean bean = p.create().parse("<testBean><value encoded=\"true\">innervalue</value></testBean>");
		assertNotNull(bean);
		assertNotNull(bean.value);
		assertTrue(bean.value.encoded);
		assertEquals("innervalue", bean.value.value);
	}
	
	public static void testParseNamedSubObjectPolymorphicList(TestParserFactory p) {
		TestBean bean = p.create().parse("<testBean><namedList xsi:type=\"namedObject\" objattr=\"passed3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/></testBean>");
		assertNotNull(bean);
		assertNotNull(bean.namedList);
		assertEquals(1, bean.namedList.size());
		assertEquals("passed3", ((TestSubNamedObject)bean.namedList.get(0)).objattr);
	}
	
	public static void testParseStringElement(TestParserFactory p) {
		TestBean bean = p.create().parse("<testBean><elemString>testString</elemString></testBean>");
		assertNotNull(bean);
		assertNotNull(bean.elemString);
		assertEquals("testString", bean.elemString);
	}
	
	public static void testParseElementEnum(TestParserFactory p) {
		TestBean bean = p.create().parse("<testBean><elemEnum>VAL2</elemEnum></testBean>");
		assertNotNull(bean);
		assertNotNull(bean.elemEnum);
		assertEquals(TESTENUM.VAL2, bean.elemEnum);
	}
	
	public static void testParseAttributeEnum(TestParserFactory p) {
		TestBean bean = p.create().parse("<testBean attrEnum=\"VAL2\"></testBean>");
		assertNotNull(bean);
		assertNotNull(bean.attrEnum);
		assertEquals(TESTENUM.VAL2, bean.attrEnum);
	}
	
	public static void testPolymprohicElement(TestParserFactory p) {
		TestBean bean = p.create().parse("<testBean><abstractObj xsi:type=\"testImplObject\" val=\"true\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/></testBean>");
		assertNotNull(bean);
		assertNotNull(bean.abstractObj);
		assertTrue(bean.abstractObj instanceof TestImplObject);
		assertTrue(((TestImplObject)bean.abstractObj).val);
	}
	
	public static void testListStringElement(TestParserFactory p) {
		TestBean bean = p.create().parse("<testBean><abstractObj xsi:type=\"testImplObject\" val=\"true\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/><stringsList>test</stringsList><stringsList>test2</stringsList><stringsList>test3</stringsList></testBean>");
		assertNotNull(bean);
		assertNotNull(bean.stringsList);
		assertEquals(3, bean.stringsList.size());
	}
	
	
}
