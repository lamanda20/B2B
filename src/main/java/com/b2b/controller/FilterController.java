package com.b2b.controller;

import com.b2b.service.FilterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filters")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class FilterController {

    private final FilterService filterService;

    public FilterController(FilterService filterService) {
        this.filterService = filterService;
    }

    @GetMapping("/{categoryId}")
    public List<String> getFilters(@PathVariable int categoryId) {
        return filterService.getFiltersForCategory(categoryId);
    }
}
