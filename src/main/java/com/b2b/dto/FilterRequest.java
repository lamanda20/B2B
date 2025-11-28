package com.b2b.dto;

import lombok.Data;

import java.util.Map;

@Data
public class FilterRequest {
    private int categoryId;
    private Map<String, String> filters;
}
