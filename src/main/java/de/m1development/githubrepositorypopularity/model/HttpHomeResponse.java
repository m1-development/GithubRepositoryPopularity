package de.m1development.githubrepositorypopularity.model;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class HttpHomeResponse {
    private String description;
    private Map<String, String> endpoints = new LinkedHashMap<>();
}
