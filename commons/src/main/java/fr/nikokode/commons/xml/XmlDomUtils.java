/**
 *
 */
package fr.nikokode.commons.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.nikokode.commons.log.Log4jLogger;

/**
 * @author ngiraud
 *
 */
public class XmlDomUtils {

    public static Element loadXmlFile(String filePath) throws IOException {
        return loadXmlFile(new File(filePath));
    }

    public static Element loadXmlFile(File filePath) throws IOException {
        DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        factory.setValidating(false);


        Document doc;
        try {
            doc = factory.newDocumentBuilder().parse(filePath);
        } catch (SAXException e) {
            throw new IOException(e.getLocalizedMessage(), e);
        } catch (ParserConfigurationException e) {
            throw new IOException(e.getLocalizedMessage(), e);
        }

        return doc.getDocumentElement();
    }

    public static String elementToString(Element element) {
        try {
            Source source = new DOMSource(element);
            StringWriter sw = new StringWriter();
            Result result = new StreamResult(sw);

            Transformer xformer =
                TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);

            String eltStr = sw.toString();
            sw.close();

            return eltStr;
        } catch (final TransformerConfigurationException e) {
            Log4jLogger.error(XmlDomUtils.class, e);
            return e.getMessage();
        } catch (final TransformerException e) {
            Log4jLogger.error(XmlDomUtils.class, e);
            return e.getMessage();
        } catch (final IOException e) {
            Log4jLogger.error(XmlDomUtils.class, e);
            return e.getMessage();
        }
    }

    public static Element getFirstChildElementByName(Element root, String tagName) {
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node child = nl.item(i);
            if (child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (tagName.equals(child.getNodeName())) {
                return (Element) child;
            }
        }
        return null;
    }

    public static List<Element> getChildElementsByName(Element root, String tagName) {
        List<Element> tags = new LinkedList<Element>();
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node child = nl.item(i);
            if (child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (tagName.equals(child.getNodeName())) {
                tags.add((Element) child);
            }
        }
        return tags;
    }

}
