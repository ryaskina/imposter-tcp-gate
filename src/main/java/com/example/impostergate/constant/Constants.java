package com.example.impostergate.constant;

import static com.example.impostergate.constant.Utils.fetchAppProperties;

public final class Constants {
    public final static String TEMPLATES_DIR = System.getProperty("user.dir") + "/src/main/resources/templates/";
    
    public final static String SOCKET_HOSTNAME = "localhost";

    public final static String IMPOSTER_APPl_PORT = fetchAppProperties().getProperty("server.port");
    public final static String START_SOCKET_URL = "http://localhost:" + IMPOSTER_APPl_PORT  + "/startsocket";
    public final static String STOP_SOCKET_URL = "http://localhost:" + IMPOSTER_APPl_PORT  + "/stopsocket";
    public final static String ADD_MOCK_RESPONSES_URL = "http://localhost:9000/addmockresponses";
}
