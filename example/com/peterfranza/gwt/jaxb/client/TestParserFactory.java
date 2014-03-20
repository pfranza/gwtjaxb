package com.peterfranza.gwt.jaxb.client;

import com.peterfranza.gwt.jaxb.client.objs.TestAbstObject;
import com.peterfranza.gwt.jaxb.client.objs.TestBean;
import com.peterfranza.gwt.jaxb.client.objs.TestImplObject;
import com.peterfranza.gwt.jaxb.client.objs.TestStringValue;
import com.peterfranza.gwt.jaxb.client.objs.TestSubNamedObject;
import com.peterfranza.gwt.jaxb.client.objs.TestSubObject;
import com.peterfranza.gwt.jaxb.client.objs.TestSubObjectChild;
import com.peterfranza.gwt.jaxb.client.parser.JAXBBindings;
import com.peterfranza.gwt.jaxb.client.parser.JAXBParserFactory;

@JAXBBindings(value=TestBean.class, objects={TestSubObject.class, TestSubObjectChild.class, TestStringValue.class, TestSubNamedObject.class,
	TestAbstObject.class, TestImplObject.class})
public interface TestParserFactory extends JAXBParserFactory<TestBean>{}
