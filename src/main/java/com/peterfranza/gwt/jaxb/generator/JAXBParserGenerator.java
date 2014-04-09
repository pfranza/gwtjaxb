package com.peterfranza.gwt.jaxb.generator;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.peterfranza.gwt.jaxb.client.parser.JAXBBindings;

public class JAXBParserGenerator extends Generator {

	public static boolean ENABLE_VERBOSE = false;
	
	@Override
	public String generate(TreeLogger logger, GeneratorContext context,	String typeName) throws UnableToCompleteException {
			
		try {
			JClassType classType = context.getTypeOracle().getType(typeName);
			
			JAXBBindings bindings = classType.getAnnotation(JAXBBindings.class);
			if(bindings == null) {
			   throw new RuntimeException("No JAXBBindings defined for " + classType.getName());
			}
			
			createFactoryForClass(logger, context, bindings.value(), bindings);
			for(Class<?> cls: bindings.objects()) {
				if(!Modifier.isAbstract(cls.getModifiers())) {
					createFactoryForClass(logger, context, cls, bindings);
				}
			}
			
			
			String rootType = "?";
			rootType = bindings.value().getName();
			
			// Here you would retrieve the metadata based on typeName for this Screen
			SourceWriter src = getSourceWriter(classType, context, logger);
			if(src != null) {
				System.out.println("Generating for: " + typeName);
				System.out.println("  -- " + bindings.value().getName());

				src.println("public JAXBParser<"+rootType+"> create() {");			
				src.println("System.out.println(\"Creating Parser For: "+rootType+"\");");

				
				String clsName = bindings.value().getSimpleName();
				clsName = ("" + clsName.charAt(0)).toLowerCase() + clsName.substring(1);
				
				XmlRootElement rootElement = bindings.value().getAnnotation(XmlRootElement.class);
				if(rootElement != null) {
					if(!rootElement.name().equals("##default")) {
						clsName = rootElement.name();
					}
				}

				src.println("JAXBParser<"+rootType+"> pp = new JAXBParser<"+rootType+">() {");
				src.println("	@Override");
				src.println("	public "+rootType+" parse(String xml) {");
				src.println("		Document document = XMLParser.parse(xml);");
				src.println("		Element element = document.getDocumentElement();");
				
				src.println("		if(!element.getNodeName().equals(\""+clsName+"\"))");
				src.println("			throw new RuntimeException(\"Bad Element Found: \" + element.getNodeName() );");
				
				src.println("		return "+bindings.value().getName()+"FactoryGenerated.create(element);");
				src.println("	}");
				src.println("};");

				src.println("return pp;");
				src.println("}");
				src.commit(logger);
			}
			
			return typeName + "Generated";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void createFactoryForClass(TreeLogger logger,
			GeneratorContext context, Class<?> cls, final JAXBBindings bindings) throws Exception {		
		
		SourceWriter factorySrc = getFactorySourceWriter(cls, context, logger, getImports(cls));
		if(factorySrc != null) {
			factorySrc.println("public static "+cls.getName()+" create(Element element) {");
			
			factorySrc.println("		if(element == null) return null;");
			factorySrc.println("");
			factorySrc.println(cls.getName() + " _instance = new " + cls.getName() + "();");
			
			for(Field f: getDeclaredFields(cls)) {
				
				boolean isTransient = f.getAnnotation(XmlTransient.class) != null;				
				if(!isTransient) {
					
					XmlAttribute attr = f.getAnnotation(XmlAttribute.class);
					XmlElement   elem = f.getAnnotation(XmlElement.class);
					XmlValue     value = f.getAnnotation(XmlValue.class);

					if(attr != null) {				
						String attrName = f.getName();
						if(!attr.name().equals("##default")) {
							attrName = attr.name();
						}
						
						debug("marshalling " + f.getName() + " as attribute " + attrName);

						if(!Modifier.isPrivate(f.getModifiers())) {
							factorySrc.println("	_instance."+f.getName() + " = " + getAttributeAccessor(f, attrName)+";");
						} else {
							Method m = cls.getMethod(getSetterName(cls, f, attrName, f.getType()), f.getType());
							if(m != null) {						
								factorySrc.println("	_instance."+m.getName()+"("+getAttributeAccessor(f, attrName)+");");
							} else {
								System.out.println("Inaccessable type " + f.getName() + " " + f.getType() + " " + f.isAccessible());
							}
						}
					} else if(elem != null) {
						String elemName = f.getName();
						if(!elem.name().equals("##default")) {
							elemName = elem.name();
						}
						
						debug("marshalling " + f.getName() + " as element " + elemName);

						if(!Modifier.isPrivate(f.getModifiers())) {
							factorySrc.println("	_instance."+f.getName() + " = " + getElementAccessor(f, elemName, bindings)+";");
						} else {
							Method m = cls.getMethod(getSetterName(cls, f, elemName, f.getType()), f.getType());
							if(m != null) {						
								factorySrc.println("	_instance."+m.getName()+"(" + getElementAccessor(f, elemName, bindings)+");");
							} else {
								System.out.println("Inaccessable type " + f.getName() + " " + f.getType() + " " + f.isAccessible());
							}
						}

					} else if(value != null) {
						debug("marshalling " + f.getName() + " as value");

						if(!Modifier.isPrivate(f.getModifiers())) {
							factorySrc.println("	_instance."+f.getName() + " = " + getValueAccessor(f)+";");
						} else {
							Method m = cls.getMethod(getSetterName(cls, f, f.getName(), f.getType()), f.getType());
							if(m != null) {						
								factorySrc.println("	_instance."+m.getName()+"("+getValueAccessor(f)+");");
							} else {
								System.out.println("Inaccessable type " + f.getName() + " " + f.getType() + " " + f.isAccessible());
							}
						}
					}
				} else {
					debug(f.getName() + " as transient");
				}
			}
			
			factorySrc.println("return _instance;");
			factorySrc.println("}");
			factorySrc.commit(logger);
		}
	}

	
	private Collection<String> getImports(Class<?> cls) {
		HashSet<String> imports = new HashSet<String>();
		for(Field f: getDeclaredFields(cls)) {				
			if(!(f.getAnnotation(XmlTransient.class) != null)) {			
				XmlTransient trans  = f.getAnnotation(XmlTransient.class);
				
				if(trans == null) {					
					if(!f.getType().isPrimitive() && !f.getType().getName().startsWith("java.lang")) {
						if(f.getType().getEnclosingClass() == null) {
						imports.add(f.getType().getPackage().getName() + ".*");
						} else {
							imports.add(f.getType().getEnclosingClass().getName() + ".*");
						}
					} 
					
					if(Collection.class.isAssignableFrom(f.getType())) {
						debug("Import Collection: " + f.getName());
						if (f.getGenericType() instanceof ParameterizedType) {  
				            ParameterizedType pt = (ParameterizedType) f.getGenericType();   
				            Type listType = pt.getActualTypeArguments()[0];
				            if(listType instanceof Class) {
				            	Class<?> typeClass = (Class<?>) listType;
				            	if(!typeClass.isPrimitive() && !typeClass.getName().startsWith("java.lang")) {
				            		imports.add(typeClass.getPackage().getName() + ".*");
				            	}
				            }
						}
					}	
				}
			}
		}
		return imports;
	}
	

	private Collection<Field> getDeclaredFields(Class<?> cls) {
		Collection<Field> list = new ArrayList<Field>();
		list.addAll(Arrays.asList(cls.getDeclaredFields()));
		Class<?> spr = cls.getSuperclass();
		do {
			list.addAll(Arrays.asList(spr.getDeclaredFields()));
		} while((spr = spr.getSuperclass()) != null); 
		
		return list;
	}

	private static String getElementAccessor(Field f, String elemName, final JAXBBindings bindings) {
		

		
		if(f.getType().equals(String.class)) {
			return "XMLParsingUtils.getNodeText(XMLParsingUtils.getNamedChild(element, \""+elemName+"\"))";
		} else if(f.getType().equals(Integer.class) || f.getType().equals(int.class)) {
			return "XMLParsingUtils.hasNamedChild(element, \""+elemName+"\") ? Integer.valueOf(XMLParsingUtils.getNodeText(XMLParsingUtils.getNamedChild(element, \""+elemName+"\"))) : 0";
		} else if(f.getType().equals(Long.class) || f.getType().equals(long.class)) {
			return "XMLParsingUtils.hasNamedChild(element, \""+elemName+"\") ? Long.valueOf(XMLParsingUtils.getNodeText(XMLParsingUtils.getNamedChild(element, \""+elemName+"\"))) : 0";
		} else if(f.getType().equals(Short.class) || f.getType().equals(short.class)) {
			return "XMLParsingUtils.hasNamedChild(element, \""+elemName+"\") ? Short.valueOf(XMLParsingUtils.getNodeText(XMLParsingUtils.getNamedChild(element, \""+elemName+"\"))) : 0";
		} else if(f.getType().equals(Float.class) || f.getType().equals(float.class)) {
			return "XMLParsingUtils.hasNamedChild(element, \""+elemName+"\") ? Float.valueOf(XMLParsingUtils.getNodeText(XMLParsingUtils.getNamedChild(element, \""+elemName+"\"))) : 0";
		} else if(f.getType().equals(Double.class) || f.getType().equals(double.class)) {
			return "XMLParsingUtils.hasNamedChild(element, \""+elemName+"\") ? Double.valueOf(XMLParsingUtils.getNodeText(XMLParsingUtils.getNamedChild(element, \""+elemName+"\"))) : 0";
		} else if(f.getType().equals(Boolean.class) || f.getType().equals(boolean.class)) {
			return "XMLParsingUtils.hasNamedChild(element, \""+elemName+"\") ? Boolean.valueOf(XMLParsingUtils.getNodeText(XMLParsingUtils.getNamedChild(element, \""+elemName+"\"))) : false";
		} else if(f.getType().equals(Enum.class) || ((f.getType() instanceof Class<?>) && ((Class<?>)f.getType()).isEnum())) {
			Class<?> enumClass = ((Class<?>)f.getType());
			return "XMLParsingUtils.hasNamedChild(element, \""+elemName+"\") ? "+enumClass.getName().replace("$", ".")+".valueOf(XMLParsingUtils.getNodeText(XMLParsingUtils.getNamedChild(element, \""+elemName+"\"))) : null";
		}
		
		if(Collection.class.isAssignableFrom(f.getType())) {
			StringBuffer buf = new StringBuffer();
			debug("Found Collection: " + f.getGenericType());
			if (f.getGenericType() instanceof ParameterizedType) {  
	            ParameterizedType pt = (ParameterizedType) f.getGenericType();   
	            Type listType = pt.getActualTypeArguments()[0];
	            if(listType instanceof Class) {
	            	Class<?> typeClass = (Class<?>) listType;
	            	

	            	buf.append("XMLParsingUtils.buildNamedElements(element, \""+elemName+"\", new XMLNodeFactory<"+typeClass.getName()+">(){\n")
	            	.append("\t\t").append("public "+typeClass.getName()+" build(Element e){\n");

	            	for(Class<?> cls: bindings.objects()) {
	      		
	            		if(listType instanceof Class && ((Class<?>)listType).isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
	            			String typeName = cls.getSimpleName();
	            			
	            			XmlType xmlType = cls.getAnnotation(XmlType.class);  			
	    					if(xmlType != null && !xmlType.name().equals("##default")) {
	    						typeName = xmlType.name();
	    					}

	    					buf.append("\t\t\t").append("if(e.hasAttribute(\"xsi:type\") && e.getAttribute(\"xsi:type\").equals(\""+typeName+"\")) {\n")
    							.append("\t\t\t\t").append("return " + cls.getSimpleName() + "FactoryGenerated.create(e);\n")
    							.append("\t\t\t").append("}\n");
	            		}
	            	}
	            	
	            	if(typeClass.equals(String.class)) {
	            		buf.append("\t\t\t").append("return XMLParsingUtils.getNodeText(e);\n");
	            	} else if(!Modifier.isAbstract(typeClass.getModifiers())) {
	            		buf.append("\t\t\t").append("return " + typeClass.getSimpleName() + "FactoryGenerated.create(e);\n");
	            	} else {
	            		buf.append("\t\t\t").append("return null;\n");
	            	}
	            	
	            	buf.append("\t\t").append("}\n").append("\t").append("})");
	            			
	            }
	        }  
			return buf.toString();
		}
		
		if(bindings != null) {
			for(Class<?> b: bindings.objects()) {
				if(!Modifier.isAbstract(b.getModifiers()) && f.getType().equals(b)) {
					return b.getSimpleName() + "FactoryGenerated.create(XMLParsingUtils.getNamedChild(element, \""+elemName+"\"))";
				}
			}
			
			StringBuffer buf = new StringBuffer();
        	buf.append("XMLParsingUtils.buildNamedElement(element, \""+elemName+"\", new XMLNodeFactory<"+f.getType().getName()+">(){\n")
        	.append("\t\t").append("public "+f.getType().getName()+" build(Element e){\n");

        	for(Class<?> cls: bindings.objects()) {
        		if(f.getType().isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
        			
        			String typeName = cls.getSimpleName();
        			typeName = ("" + typeName.charAt(0)).toLowerCase() + typeName.substring(1);
        			
        			XmlType xmlType = cls.getAnnotation(XmlType.class);  			
					if(xmlType != null && !xmlType.name().equals("##default")) {
						typeName = xmlType.name();
					}
        			
        			buf.append("\t\t\t").append("if(e.hasAttribute(\"xsi:type\") && e.getAttribute(\"xsi:type\").equals(\""+typeName+"\")) {\n")
        				.append("\t\t\t\t").append("return " + cls.getSimpleName() + "FactoryGenerated.create(e);\n")
        				.append("\t\t\t").append("}\n");
        		}
        	}
        	
        	if(!Modifier.isAbstract(f.getType().getModifiers())) {
        		buf.append("\t\t\t").append("return " + f.getType().getSimpleName() + "FactoryGenerated.create(e);\n");
        	} else {
        		buf.append("\t\t\t").append("return null;\n");
        	}
        	
        	buf.append("\t\t").append("}\n").append("\t").append("})");
			return buf.toString();
		} 
			
		System.out.println("Unable to bind element type: " + f.getType() + " enum=" + f.getType().isEnum());
		throw new RuntimeException("No Accessor Found for " + f.getName());
	}
	
	private static String getAttributeAccessor(Field f, String attrName) {
		if(f.getType().equals(String.class)) {
			return "element.getAttribute(\""+attrName+"\")";
		} else if(f.getType().equals(Integer.class) || f.getType().equals(int.class)) {
			return "element.hasAttribute(\""+attrName+"\") ? Integer.valueOf(element.getAttribute(\""+attrName+"\")) : 0";
		} else if(f.getType().equals(Long.class) || f.getType().equals(long.class)) {
			return "element.hasAttribute(\""+attrName+"\") ? Long.valueOf(element.getAttribute(\""+attrName+"\")) : 0";
		} else if(f.getType().equals(Short.class) || f.getType().equals(short.class)) {
			return "element.hasAttribute(\""+attrName+"\") ? Short.valueOf(element.getAttribute(\""+attrName+"\")) : 0";
		} else if(f.getType().equals(Float.class) || f.getType().equals(float.class)) {
			return "element.hasAttribute(\""+attrName+"\") ? Float.valueOf(element.getAttribute(\""+attrName+"\")) : 0";
		} else if(f.getType().equals(Double.class) || f.getType().equals(double.class)) {
			return "element.hasAttribute(\""+attrName+"\") ? Double.valueOf(element.getAttribute(\""+attrName+"\")) : 0";
		} else if(f.getType().equals(Boolean.class) || f.getType().equals(boolean.class)) {
			return "element.hasAttribute(\""+attrName+"\") ? Boolean.valueOf(element.getAttribute(\""+attrName+"\")) : false";
		} else if(f.getType().equals(Enum.class) || ((f.getType() instanceof Class<?>) && ((Class<?>)f.getType()).isEnum())) {
			Class<?> enumClass = ((Class<?>)f.getType());
			return "element.hasAttribute(\""+attrName+"\") ? "+enumClass.getName().replace("$", ".") +".valueOf(element.getAttribute(\""+attrName+"\")) : null";
		}
		
		System.out.println("Unable to bind attribute type: " + f.getType());
		throw new RuntimeException("No Accessor Found for " + f.getName());
	}
	
	private static String getValueAccessor(Field f) {
		if(f.getType().equals(String.class)) {
			return "XMLParsingUtils.getNodeText(element)";
		} else if(f.getType().equals(Integer.class) || f.getType().equals(int.class)) {
			return "Integer.valueOf(XMLParsingUtils.getNodeText(element))";
		} else if(f.getType().equals(Long.class) || f.getType().equals(long.class)) {
			return "Long.valueOf(XMLParsingUtils.getNodeText(element))";
		} else if(f.getType().equals(Short.class) || f.getType().equals(short.class)) {
			return "Short.valueOf(XMLParsingUtils.getNodeText(element))";
		} else if(f.getType().equals(Float.class) || f.getType().equals(float.class)) {
			return "Float.valueOf(XMLParsingUtils.getNodeText(element))";
		} else if(f.getType().equals(Double.class) || f.getType().equals(double.class)) {
			return "Double.valueOf(XMLParsingUtils.getNodeText(element))";
		} else if(f.getType().equals(Boolean.class) || f.getType().equals(boolean.class)) {
			return "Boolean.valueOf(XMLParsingUtils.getNodeText(element))";
		} else if(f.getType().equals(Enum.class) || ((f.getType() instanceof Class<?>) && ((Class<?>)f.getType()).isEnum())) {
			Class<?> enumClass = ((Class<?>)f.getType());
			return "!XMLParsingUtils.getNodeText(element).equals(\"\") ? " + enumClass.getName().replace("$", ".")+".valueOf(XMLParsingUtils.getNodeText(element)) : null";
		}
		
		System.out.println("Unable to bind attribute type: " + f.getType());
		throw new RuntimeException("No Accessor Found for " + f.getName());
	}
	
	private static String getSetterName(Class<?> cls, Field f, String attribute, Class<?> type) {
		Method m;
		
		String altName = f.getName().toLowerCase().startsWith("is") && (type.equals(boolean.class) || type.equals(Boolean.class))
				? f.getName().substring(2) : f.getName();
		
		if((m = isMethodExists(cls, "set" + ("" + attribute.charAt(0)).toUpperCase() + attribute.substring(1), type)) != null) {
			return m.getName();
		} else if((m = isMethodExists(cls, "set" + ("" + f.getName().charAt(0)).toUpperCase() + f.getName().substring(1), type)) != null) {
			return m.getName();	
		} else if((m = isMethodExists(cls, "set" + ("" + altName.charAt(0)).toUpperCase() + altName.substring(1), type)) != null) {
			return m.getName();
		} 
		
		throw new RuntimeException("No Setter Found for " + f.getName());
	}
	
	private static Method isMethodExists(Class<?> cls, String name, Class<?> type) {
		try {
			return cls.getMethod(name, type);
		} catch (Exception e) {
			debug(e.getMessage());
		}
		return null;
	}
	
	public SourceWriter getSourceWriter(JClassType classType,
			GeneratorContext context, TreeLogger logger) {
		String packageName = classType.getPackage().getName();
		String simpleName = classType.getSimpleSourceName() + "Generated";
		
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, simpleName);
		composer.addImplementedInterface(classType.getName());

		// Need to add whatever imports your generated class needs.
		composer.addImport("com.google.gwt.xml.client.*");
		composer.addImport("com.peterfranza.gwt.jaxb.client.parser.*");
	
		
		PrintWriter printWriter = context.tryCreate(logger, packageName, simpleName);
		if (printWriter == null) {
			return null;
		} else {
			SourceWriter sw = composer.createSourceWriter(context, printWriter);
			return sw;
		}
	}
	
	public SourceWriter getFactorySourceWriter(Class<?> classType,
			GeneratorContext context, TreeLogger logger, Collection<String> imports) {
		String packageName = classType.getPackage().getName();
		String simpleName = classType.getSimpleName() + "FactoryGenerated";
		
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, simpleName);

		// Need to add whatever imports your generated class needs.
		composer.addImport("com.google.gwt.xml.client.*");
		composer.addImport("com.peterfranza.gwt.jaxb.client.parser.*");
		composer.addImport("com.peterfranza.gwt.jaxb.client.parser.utils.*");
		composer.addImport("java.util.*");
		
		for(String s: imports) {
			composer.addImport(s);
			debug("Augmenting Imports: +" + s);
		}
				
		PrintWriter printWriter = context.tryCreate(logger, packageName, simpleName);
		if (printWriter == null) {
			return null;
		} else {
			SourceWriter sw = composer.createSourceWriter(context, printWriter);
			return sw;
		}
	}
	
	private static void debug(String s) {
		if(ENABLE_VERBOSE) {
			System.out.println(s);
		}
	}

}
