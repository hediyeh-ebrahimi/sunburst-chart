package com.example.chart.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class Parent {
    private String name;
    @JsonProperty("children")
    private List<Children> childrenList;

    public Parent(String name, List<Children> childrenList) {
        this.name = name;
        this.childrenList = childrenList;
    }

}
