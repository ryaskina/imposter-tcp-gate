package com.example.impostergate.model;

public class StoppingSocket {

    private Boolean stopAllSockets;

    public StoppingSocket() {}

    public StoppingSocket(Boolean stopAllSockets) {
        super();
        this.stopAllSockets = stopAllSockets;
    }

    @Override
    public String toString() {
        return "Request [ stopAllSockets" + stopAllSockets.toString() + "]";
    }

    public Boolean getStopAllSockets() {return stopAllSockets;}

}
