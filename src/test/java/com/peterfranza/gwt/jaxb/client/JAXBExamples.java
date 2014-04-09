package com.peterfranza.gwt.jaxb.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.peterfranza.gwt.jaxb.client.JAXBExamples.TESTENUM;
import com.peterfranza.gwt.jaxb.client.objs.TestBean;

public class JAXBExamples implements EntryPoint {

	public static enum TESTENUM {
		VAL1,
		VAL2,
		VAL3
	}
	
	@Override
	public void onModuleLoad() {		
		TestParserFactory p = GWT.create(TestParserFactory.class);
		TestBean bean = p.create().parse("<TestBean />");
		System.out.println(bean);
//		Element ele;
//		ele.hasAttribute(name)

	}

}
