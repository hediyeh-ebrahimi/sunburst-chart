package com.example.chart.model;

import lombok.Data;

@Data
public class Children {
    private String name;
    private Double value;

    public Children(String name, Double value) {
        this.name = name;
        this.value = value;
    }
}
