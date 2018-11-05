package org.rubik.util.serializer;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.rubik.bean.core.Constants;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

@SuppressWarnings("unchecked")
public class XmlSerializer implements Serializer {

	@Override
	public byte[] serial(Serializable model) {
		return beanToXml(model).getBytes(Constants.UTF_8);
	}

	@Override
	public <ENTITY extends Serializable> ENTITY deserial(byte[] data, Class<ENTITY> clazz) {
		return xmlTobean(new String(data, Constants.UTF_8), clazz);
	}
	
	public static final String beanToXml(Object bean) {
		try {
			JAXBContext context = JAXBContext.newInstance(bean.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, Constants.UTF_8);
			StringWriter writer = new StringWriter();
			marshaller.marshal(bean, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static final <ENTITY> ENTITY xmlTobean(String xml, Class<ENTITY> clazz) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(xml);
			SAXParserFactory sax = SAXParserFactory.newInstance();
			sax.setNamespaceAware(false);
			XMLReader xmlReader = sax.newSAXParser().getXMLReader();
			Source source = new SAXSource(xmlReader, new InputSource(reader));
			ENTITY instance = (ENTITY) unmarshaller.unmarshal(source);
			return instance;
		} catch (JAXBException | SAXException | ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
	}
}
