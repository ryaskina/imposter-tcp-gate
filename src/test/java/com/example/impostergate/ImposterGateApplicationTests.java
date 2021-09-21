package com.example.impostergate;

import com.example.impostergate.constant.SocketProperties;
import com.example.impostergate.model.MockData;
import com.example.impostergate.model.MockDataList;
import com.example.impostergate.model.StartingSocket;
import com.example.impostergate.model.StoppingSocket;
import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static com.example.impostergate.constant.Constants.*;

@SpringBootTest
class ImposterGateApplicationTests {

    private static final Gson gson = new Gson();

    /**
     * Customizing stub responses for some requests for SIS and CIA services.
     */
    @Test
    @Tag("mocksSetting")
    public void contextLoads() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(ADD_MOCK_RESPONSES_URL);

        MockData mockDataOne = new MockData(
                "ReqAgentParameter",
                "AnsAgentParameter",
                null,
                new String[] {"<Document><CIA><InfoService><AnsAgentParameter></AnsAgentParameter></InfoService></CIA></Document>"});
        MockData mockDataTwo = new MockData(
                "ReqMyInfo",
                "AnsMyInfo",
                null,
                new String[]{"<Document><CIA><InfoService><AnsMyInfo><FullName>JAMES BOND</FullName></AnsMyInfo></InfoService></CIA></Document>"});
        MockData mockDataThree = new MockData(
                "ReqAuthentication",
                "AnsAuthentication",
                null,
                new String[] {"<Document><SIS><Security><AnsAuthentication><Status><Code>007</Code></Status><PrivilegeList><PrivilegeId>7</PrivilegeId><PrivilegeName>License to kill</PrivilegeName></PrivilegeList></AnsAuthentication></Security></SIS></Document>"});
        MockData mockDataFour = new MockData(
                "ReqSupportInformation",
                "AnsSupportInformation",
                "MemoFileNameAns",
                new String[] {"<Document><SIS><InfoService><AnsSupportInformation><MemoFileNameAns>some_information.pdf</MemoFileNameAns></AnsSupportInformation></InfoService></SIS></Document>"});
        MockData mockDataFive = new MockData(
                "ReqSupportInformation",
                "AnsSupportInformation",
                "ScanningStatusAns",
                new String[] {"<Document><SIS><InfoService><AnsSupportInformation><ScanningStatusAns><Status><Code>0</Code></Status></ScanningStatusAns></AnsSupportInformation></InfoService></SIS></Document>"});
        ArrayList<MockData> list = new ArrayList<MockData>();
        list.add(mockDataOne);
        list.add(mockDataTwo);
        list.add(mockDataThree);
        list.add(mockDataFour);
        list.add(mockDataFive);

        MockDataList mockDataList = new MockDataList();
        mockDataList.setMockDataList(list);
        String json = gson.toJson(mockDataList);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(json, headers);
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

        outResult(result);
    }

    @Test
    public void startSisTcpTest() throws URISyntaxException {
        int port = SocketProperties.SIS.getPort();
        Assertions.assertThat(available(port))
                .as("---Port " + port + " for SIS protocol is not available---")
                .isTrue();

        startSocket(SocketProperties.SIS.getProtocol());
    }

    @Test
    public void startCiaTcpTest() throws URISyntaxException {
        int port = SocketProperties.CIA.getPort();
        Assertions.assertThat(available(port))
                .as("---Port " + port + " for CIA protocol is not available---")
                .isTrue();

        startSocket(SocketProperties.CIA.getProtocol());
    }

    @Test
    @Tag("mocksRunning")
    public void startAllTcpTest() throws URISyntaxException {
        startCiaTcpTest();
        startSisTcpTest();
    }

    private static boolean available(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }

    @Test
    @Tag("mocksStopping")
    public void stopTcpTest() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(STOP_SOCKET_URL);

        StoppingSocket stoppingSocket = new StoppingSocket(true);
        String json = gson.toJson(stoppingSocket);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(json, headers);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

        outResult(result);
    }

    private void outResult(ResponseEntity<String> result) {
        System.out.println("STATUS_CODE=" + result.getStatusCode());
        System.out.println("RESPONSE=" + result);
    }

    private void startSocket(String protocol) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(START_SOCKET_URL);

        StartingSocket startingSocket = new StartingSocket(protocol, true);
        String json = gson.toJson(startingSocket);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(json, headers);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

        Assertions.assertThat(result.getStatusCode().toString())
                .contains("202 ACCEPTED");

        outResult(result);
    }
}