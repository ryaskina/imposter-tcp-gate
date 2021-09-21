package com.example.impostergate.controller;

import com.example.impostergate.TcpMockApplication;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SocketService {

    private final TcpMockApplication tcpMockApplication;

    public SocketService(TcpMockApplication tcpMockApplication) {
        this.tcpMockApplication = tcpMockApplication;
    }

    public void startMock(String protocol) {
        Thread.currentThread().setName(protocol);
        System.out.println("Start mock in Thread " + Thread.currentThread().getName());
        try {
            tcpMockApplication.startTcpMockApplication(protocol);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
