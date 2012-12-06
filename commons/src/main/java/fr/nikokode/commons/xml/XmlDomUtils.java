/**
 *
 */
package fr.nikokode.commons.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

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

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author ngiraud
 *
 */
public class XmlDomUtils {

    private static Logger LOGGER = Logger.getLogger(XmlDomUtils.class);

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
            LOGGER.error(e.getLocalizedMessage(), e);
            return e.getMessage();
        } catch (final TransformerException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            return e.getMessage();
        } catch (final IOException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            return e.getMessage();
        }
    }

    public static Element getChildElementByName(Element root, String tagName) {
        NodeList nl = root.getElementsByTagName(tagName);
        return (nl.getLength() == 0 ? null : (Element) nl.item(0));
    }

}
