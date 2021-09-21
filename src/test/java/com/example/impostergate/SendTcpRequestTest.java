package com.example.impostergate;

import com.example.impostergate.constant.SocketProperties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.impostergate.constant.Constants.SOCKET_HOSTNAME;

public class SendTcpRequestTest {

    /**
     * Test send request to SIS tcp mock and print response by mock.
     */
    @Test
    @Tag("mockTest")
    public void sendSisRequestTest() {
        String requestBody = "<Document><SIS><InfoService><ReqAuthentication><a>Authenticate me, please!</a></ReqAuthentication></InfoService></SIS></Document>";
        String responseBody = getResponseByTcpServer(SOCKET_HOSTNAME, SocketProperties.SIS.getPort(), requestBody);
        System.out.println(responseBody);

        checkResponseBody(requestBody, responseBody);
    }

    /**
     * Test send request to CIA tcp mock and print response by mock.
     */
    @Test
    @Tag("mockTest")
    public void sendCiaRequestTest() {
        String requestBody = "<Document><CIA><InfoService><ReqMyInfo><a>Good morning dear friend!</a></ReqMyInfo></InfoService></CIA></Document>";
        String responseBody = getResponseByTcpServer(SOCKET_HOSTNAME, SocketProperties.CIA.getPort(), requestBody);
        System.out.println(responseBody);

        checkResponseBody(requestBody, responseBody);
    }

    public static void checkResponseBody(String requestBody, String responseBody) {
        Pattern pattern = Pattern.compile("Req[\\w]*");
        Matcher matcher = pattern.matcher(requestBody);
        String requestName = "";

        while (matcher.find()) {
            int start=matcher.start();
            int end=matcher.end();
            requestName = requestBody.substring(start,end);
        }

        Assertions
                .assertThat(responseBody)
                .contains(requestName.replace("Req", "Ans"));

    }

    public static String getResponseByTcpServer(String hostname, int port, String body) {
        String line;
        String responseBody = "";

        try (Socket socket = new Socket(hostname, port)) {

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(body);

            InputStream input = socket.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            while ((line = reader.readLine()) != null) {
                responseBody = responseBody.concat(line);
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }

        return responseBody;
    }
}
