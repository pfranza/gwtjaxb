package com.peterfranza.gwt.jaxb.client.parser.utils;

import java.util.ArrayList;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;


public class XMLParsingUtils {

	public static Element getNamedChild(Element e, String name) {
		NodeList l = e.getChildNodes();
		for(int i = 0; i < l.getLength(); i++) {
			if(l.item(i) instanceof Element) {
				Element n = (Element)l.item(i);
				if(n.getNodeName().equals(name)) {
					return n;
				}
			}
		}
		return null;
	}
	
	public static boolean hasNamedChild(Element e, String name) {
		NodeList l = e.getChildNodes();
		for(int i = 0; i < l.getLength(); i++) {
			if(l.item(i) instanceof Element) {
				Element n = (Element)l.item(i);
				if(n.getNodeName().equals(name)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static <E> ArrayList<E> buildNamedElements(Element e, String name, XMLNodeFactory<E> factory) {
		ArrayList<E> list = new ArrayList<E>();
		NodeList l = e.getChildNodes();
		for(int i = 0; i < l.getLength(); i++) {
			if(l.item(i) instanceof Element) {
				Element n = (Element)l.item(i);
				if(n.getNodeName().equals(name)) {
					E fn = factory.build(n);
					if(fn != null) {
						list.add(fn);
					}
				}
			}
		}
		return list;
	}
	
	public static <E> E buildNamedElement(Element e, String name, XMLNodeFactory<E> factory) {
		NodeList l = e.getChildNodes();
		for(int i = 0; i < l.getLength(); i++) {
			if(l.item(i) instanceof Element) {
				Element n = (Element)l.item(i);
				if(n.getNodeName().equals(name)) {
					E fn = factory.build(n);
					if(fn != null) {
						return fn;
					}
				}
			}
		}
		return null;
	}
	
	public static String getNodeText(Element e) {
        if(e == null)  return "";
        NodeList nodes = e.getChildNodes();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < nodes.getLength(); i++) {
                result.append(nodes.item(i).getNodeValue());
        }
        return result.toString();
	}
	
}
