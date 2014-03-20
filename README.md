gwtjaxb
=======

A GWT library that supports basic JAXB annotations to bind xml to a bean hierarchy.

## Usage

Creating a jaxb parser is easy:
```
@JAXBBindings(value=RootBean.class, objects={NestedSubObjectOne.class, NestedSubObjectTwo.class})
public interface RootBeanParserFactory extends JAXBParserFactory<RootBean>{}
```

You create an interface that extends JAXBParserFactory<> with a generic that matches the @XMLRootEntity node.


Then you add a @JAXBBindings to the interface where value is the @XmlRootEntity and objects is an array of all other beans that the parser should use.

Using the jaxb parser is easy:
```
RootBeanParserFactory p = GWT.create(RootBeanParserFactory .class);
RootBean bean = p.create().parse("<RootBean />");
```

Create and instance of the parser using GWT.create() and pass an xml string in for parsing.

Supported Annotations
* @XmlAccessorType(XmlAccessType.FIELD): Only the attributes on fields are supported
* @XmlRootElement
* @XmlAttribute
* @XmlElement
* @XmlTransient
* @XmlValue
 Adding more support soon