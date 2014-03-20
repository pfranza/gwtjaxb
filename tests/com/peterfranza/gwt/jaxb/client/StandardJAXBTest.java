package com.peterfranza.gwt.jaxb.client;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;

import com.peterfranza.gwt.jaxb.client.objs.TestBean;
import com.peterfranza.gwt.jaxb.client.parser.JAXBBindings;
import com.peterfranza.gwt.jaxb.client.parser.JAXBParser;

public class StandardJAXBTest {

	TestParserFactory p;
	
	@Before
	public void setup() {
		p = new TestParserFactory() {
			@Override
			public JAXBParser<TestBean> create() {
				return new JAXBParser<TestBean>() {
					@Override
					public TestBean parse(String xml) {
						try {
							JAXBBindings bindings = TestParserFactory.class.getAnnotation(JAXBBindings.class);
							ArrayList<Class<?>> binds = new ArrayList<Class<?>>();
							binds.add(bindings.value());
							
							for(Class<?> c: bindings.objects()) {
								binds.add(c);
							}
							
							final JAXBContext context = JAXBContext.newInstance(binds.toArray(new Class<?>[0]));
							return (TestBean) context.createUnmarshaller().unmarshal(new StringReader(xml));
						} catch (JAXBException e) {
							throw new RuntimeException(e);
						}
					}
				};
			}
		};
	}
	
	@Test
	public void testParseRoot() {
		MututalTests.testParseRoot(p);
	}
	
	@Test
	public void testParseBadRoot() {
		MututalTests.testParseBadRoot(p);
	}
	
	@Test
	public void testParseMisnamedRoot() {
		MututalTests.testParseMisnamedRoot(p);
	}
	
	@Test
	public void testParsePublicAttribute() {
		MututalTests.testParsePublicAttribute(p);
	}
	
	@Test
	public void testParseRenamedProtectedAttribute() {
		MututalTests.testParseRenamedProtectedAttribute(p);
	}
	
	@Test
	public void testParsePrivateAttribute() {
		MututalTests.testParsePrivateAttribute(p);
	}
	
	@Test
	public void testParseTypedAttribute() {
		MututalTests.testParseTypedAttribute(p);
	}
	
	@Test
	public void testParseSubObjectAttribute() {
		MututalTests.testParseSubObjectAttribute(p);
	}
	
	@Test
	public void testParseSubObjectList() {
		MututalTests.testParseSubObjectList(p);
	}
	
	@Test
	public void testParseSubObjectPolymorphicList() {
		MututalTests.testParseSubObjectPolymorphicList(p);
	}
	
	@Test
	public void testParseTransientAttribute() {
		MututalTests.testParseTransientAttribute(p);
	}
	
	@Test
	public void testParseValueAttribute() {
		MututalTests.testParseValueAttribute(p);
	}
	
	@Test
	public void testParseNamedSubObjectPolymorphicList() {
		MututalTests.testParseNamedSubObjectPolymorphicList(p);
	}
	
	@Test
	public void testParseStringElement() {
		MututalTests.testParseStringElement(p);
	}
	
	@Test
	public void testParseElementEnum() {
		MututalTests.testParseElementEnum(p);
	}
	
	@Test
	public void testParseAttributeEnum() {
		MututalTests.testParseAttributeEnum(p);
	}
	
	@Test
	public void testPolymprohicElement() {
		MututalTests.testPolymprohicElement(p);
	}
	
	@Test
	public void testListStringElement() {
		MututalTests.testListStringElement(p);
	}

	
	public static void main(String[] args) throws Exception {
		
//		TestBean bean = new TestBean();
//		bean.stringsList = new ArrayList<String>();
//		bean.stringsList.add("test");
//		bean.stringsList.add("test2");
//		bean.stringsList.add("test3");
//		
//		JAXBBindings bindings = TestParserFactory.class.getAnnotation(JAXBBindings.class);
//		ArrayList<Class<?>> binds = new ArrayList<Class<?>>();
//		binds.add(bindings.value());
//		
//		for(Class<?> c: bindings.objects()) {
//			binds.add(c);
//		}
//		
//		final JAXBContext context = JAXBContext.newInstance(binds.toArray(new Class<?>[0]));
//		context.createMarshaller().marshal(bean, new OutputStreamWriter(System.out));
		
		System.out.println(Collection.class.isAssignableFrom(List.class));
		
	}
	
}
