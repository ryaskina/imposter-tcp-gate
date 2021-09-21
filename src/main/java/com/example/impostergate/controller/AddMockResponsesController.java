package com.example.impostergate.controller;

import com.example.impostergate.model.ImposterGateResponse;
import com.example.impostergate.model.MockData;
import com.example.impostergate.model.MockDataList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.example.impostergate.constant.Constants.TEMPLATES_DIR;

@RestController
public class AddMockResponsesController {

    @PostMapping(path = "/addmockresponses", consumes = "application/json", produces = "application/json")
    @ResponseBody
    private ResponseEntity<Object> addMockResponse(@RequestBody MockDataList mockDataList) {
        try {
            createDirNotExist(TEMPLATES_DIR);

            for (MockData mockData : mockDataList.getMockDataList()) {
                try (FileWriter fw = new FileWriter(createResponseFile(mockData.getResponseName(), mockData.getAdditionalName()))) {
                    for (String responseBody : mockData.getResponseBody()) {
                        fw.write(responseBody);
                    }
                }
            }
            return ResponseEntity.accepted().body(new ImposterGateResponse(ImposterGateResponse.StatusType.SUCCESS, "Request accept."));
        } catch (Exception e) {
            System.out.print(e);
            return ResponseEntity.accepted().body(new ImposterGateResponse(ImposterGateResponse.StatusType.ERROR, "Smth go wrong."));
        }
    }

    private File createResponseFile(String responseName, String additionalName) {
        String filePrefix = TEMPLATES_DIR + responseName;
        String fileType = ".txt";
        String filename = additionalName != null
                ? String.format("%s_%s%s", filePrefix, additionalName, fileType)
                : filePrefix + fileType;
        return new File(filename);
    }

    private void createDirNotExist(String dir) throws IOException {
        Path checkedPath = Paths.get(dir);
        if (Files.notExists(checkedPath)) {
            Files.createDirectories(checkedPath);
        }
    }
}