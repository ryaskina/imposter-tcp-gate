package com.example.impostergate.model;

import java.util.ArrayList;
import java.util.List;

public class MockDataList {

    private ArrayList<MockData> mockDataList;

    public ArrayList<MockData> getMockDataList() {
        if (mockDataList == null) {
            mockDataList = new ArrayList<>();
        }
        return mockDataList;
    }

    public void setMockDataList(ArrayList<MockData> mockDataList) {
        this.mockDataList = mockDataList;
    }
}
