/**
 *
 */
package fr.nikokode.textbatch.utils;

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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.nikokode.textbatch.Tool;


/**
 * @author ngiraud
 *
 */
public class XmlUtils {

    public static Element loadXmlFile(String filePath)
    throws SAXException, IOException, ParserConfigurationException {
        return loadXmlFile(new File(filePath));
    }

    public static Element loadXmlFile(File filePath)
    throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        factory.setValidating(false);


        Document doc =
            factory.newDocumentBuilder().parse(filePath);

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
        } catch (TransformerConfigurationException e) {
            Tool.getInstance().logError(e);
            return e.getMessage();
        } catch (TransformerException e) {
            Tool.getInstance().logError(e);
            return e.getMessage();
        } catch (IOException e) {
            Tool.getInstance().logError(e);
            return e.getMessage();
        }
    }

}
