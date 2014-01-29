/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA
 */
package com.github.nicosensei.commons.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author ngiraud
 *
 */
public class XmlDomUtils {

    private final static Logger LOGGER = Logger.getLogger(XmlDomUtils.class);

    public static Element loadXml(String filePath) throws IOException {
        return loadXml(new FileInputStream(filePath));
    }

    public static Element loadXmlFile(File file) throws IOException {
    	return loadXml(new FileInputStream(file));
    }
    
    public static Element loadXml(final InputStream inStream) throws IOException {
        DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        factory.setValidating(false);


        Document doc;
        try {
            doc = factory.newDocumentBuilder().parse(inStream);
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
            LOGGER.error(XmlDomUtils.class, e);
            return e.getMessage();
        } catch (final TransformerException e) {
            LOGGER.error(XmlDomUtils.class, e);
            return e.getMessage();
        } catch (final IOException e) {
            LOGGER.error(XmlDomUtils.class, e);
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
