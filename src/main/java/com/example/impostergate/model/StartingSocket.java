package com.example.impostergate.model;

public class StartingSocket {

    private String protocol;
    private Boolean startSocket;

    public StartingSocket() {}

    public StartingSocket(String protocol, Boolean startSocket) {
        this.protocol = protocol;
        this.startSocket = startSocket;
    }

    @Override
    public String toString() {
        return "Request [protocol=" + protocol + ", startSocket" + startSocket.toString() + "]";
    }

    public String getProtocol() {return protocol;}

    public Boolean getStartSocket() {return startSocket;}

}
