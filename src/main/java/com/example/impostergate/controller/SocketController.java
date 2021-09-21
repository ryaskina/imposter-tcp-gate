package com.example.impostergate.controller;

import com.example.impostergate.constant.SocketProperties;
import com.example.impostergate.model.ImposterGateResponse;
import com.example.impostergate.model.StartingSocket;
import com.example.impostergate.model.StoppingSocket;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class SocketController {

    @Autowired
    private SocketService socketService;

    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    @PostMapping(path = "/startsocket")
    @ResponseBody
    private ResponseEntity<Object> startSocket(@RequestBody StartingSocket startingSocket) {
        String protocol = startingSocket.getProtocol();

        if (startingSocket.getStartSocket()) {
            executorService.submit(() -> socketService.startMock(protocol));
        }

        return ResponseEntity.accepted().body(
                new ImposterGateResponse(ImposterGateResponse.StatusType.SUCCESS, "Request accept."));

    }

    @PostMapping(path = "/stopsocket")
    @ResponseBody
    private ResponseEntity<Object> stopSocket(@RequestBody StoppingSocket stoppingSocket) {
        if (stoppingSocket.getStopAllSockets()) {
            executorService.shutdownNow();
        }
        System.out.println("All mocks are stopped.");
        return ResponseEntity.accepted().body(
                new ImposterGateResponse(
                        ImposterGateResponse.StatusType.SUCCESS,"Request accept."));
    }

}
