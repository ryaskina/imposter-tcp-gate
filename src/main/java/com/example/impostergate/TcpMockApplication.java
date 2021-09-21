package com.example.impostergate;

import com.example.impostergate.constant.SocketProperties;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static com.example.impostergate.constant.Constants.TEMPLATES_DIR;

@Component
public class TcpMockApplication {

    public void startTcpMockApplication(String protocol) throws IOException {
        int port = SocketProperties.findSocketPropsByProtocol(protocol).getPort();
        ServerSocket server = new ServerSocket(port);

        System.out.println(protocol + " mock started on port " + port + ".");

        while (true) {
            Socket socket = null;
            try {
                socket = server.accept();
                handleRequest(socket);
            } catch (Throwable e) {
                e.printStackTrace();
                System.out.println(e);
            } finally {
                if (socket != null) {
                    socket.close();
                }
            }
        }
    }

    protected void handleRequest(Socket socket) throws IOException {
        String xpathExpression = SocketProperties.findSocketPropsByPort(socket.getLocalPort()).getXpathExpression();
        String protocol = SocketProperties.findSocketPropsByPort(socket.getLocalPort()).getProtocol();
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {
            System.out.println("The client sent request.");

            JAXBContext jaxbContext;
            try {

                String text;
                String req = "";
                String ans = "";

                do {
                    text = in.readLine();
                    if (text != null) {
                        text = text.replaceAll("\\t", "");
                        text = text.replaceAll(".*(<" + protocol + ">[\\s\\S]+</" + protocol + ">).*", "$1");
                        req += text;
                    }

                } while (!(text.contains("</" + SocketProperties.SIS.getProtocol() + ">")
                        | text.contains("</" + SocketProperties.CIA.getProtocol() + ">")));

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                StringBuilder xmlStringBuilder = new StringBuilder();
                xmlStringBuilder.append(req);
                ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
                Document doc = builder.parse(input);
                XPath xPath = XPathFactory.newInstance().newXPath();
                String requestName = (String) xPath.compile(xpathExpression).evaluate(doc, XPathConstants.STRING);
                boolean isReqSupportInformation = (requestName == "ReqSupportInformation");
                System.out.println("Request name := " + requestName);
                System.out.println("Request body from client: " + req);
                requestName = requestName.replaceAll("(Req){1}", "");


                /*File newTextFile = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\textfile.txt");
                FileWriter fw = new FileWriter(newTextFile);
                fw.write(req);
                fw.close();

                jaxbContext = JAXBContext.newInstance(BROKER.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                Object broker = jaxbUnmarshaller.unmarshal(newTextFile);
                BROKER b = (BROKER) broker;
                System.out.println("1>>>" + b.getSecurity().getReqAuthentication().getOperator().getOperLogin());
                String json = gson.toJson(b);
                System.out.println(json);*/

                if (isReqSupportInformation) {
                    String getRequestTypeXpath = "local-name(//ReqSupportInformation/*[1])";
                    String additionalName = (String) xPath.compile(getRequestTypeXpath).evaluate(doc, XPathConstants.STRING);
                    additionalName = additionalName.replaceAll("(Req$){1}", "");
                    out.write(readLineByLineJava8(TEMPLATES_DIR + "Ans" + requestName + "_" + additionalName + "Ans.txt"));
                } else {
                    out.write(readLineByLineJava8(TEMPLATES_DIR + "Ans" + requestName + ".txt"));
                }
                out.flush();
                System.out.println("The client got a response.");
            } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
                e.printStackTrace();
            }
        }
    }

    private String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        String txt;
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        txt = contentBuilder.toString();
        txt = txt.replaceAll("\\t", "");
        txt = txt.replaceAll("\\n", "");
        return txt;
    }
}
