package com.example.impostergate;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class xpathTest {

    /**
     * Check how work with xml parser in java.
     */
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        StringBuilder xmlStringBuilder = new StringBuilder();
        xmlStringBuilder.append("<?xml version = \"1.0\"?> <class>    <ReqStudent rollno = \"393\">\n" +
                "      <firstname>dinkar</firstname>\n" +
                "   </ReqStudent></class>");
        ByteArrayInputStream input =  new ByteArrayInputStream(
                xmlStringBuilder.toString().getBytes("UTF-8"));
        Document doc = builder.parse(input);

        XPath xPath =  XPathFactory.newInstance().newXPath();

        String expression = "//ReqStudent";
        NodeList nodeList = (NodeList) xPath.compile(expression)
                                            .evaluate(doc, XPathConstants.NODESET);

        String expression2 = "name(//class/*)";
        String nodeList2 = (String) xPath.compile(expression2)
                                         .evaluate(doc, XPathConstants.STRING);
        System.out.println("1>>>" + nodeList2);

        nodeList2 = nodeList2.replaceAll("(Req){1}","");
        System.out.println("2>>>" + nodeList2);

        /*for (int i = 0; i < nodeList.getLength(); i++) {
            Node nNode = nodeList.item(i);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                System.out.println("Student roll no :" + eElement.getAttribute("rollno"));
            }
        }*/
    }
}
