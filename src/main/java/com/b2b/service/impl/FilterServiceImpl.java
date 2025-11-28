package com.b2b.service.impl;

import com.b2b.service.FilterService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilterServiceImpl implements FilterService {

    private final Map<Integer, List<String>> filters = new HashMap<>();

    public FilterServiceImpl() {

        // 1) Heavy Machinery
        filters.put(1, List.of(
                "Brand: Caterpillar",
                "Brand: Volvo",
                "Brand: Komatsu",
                "Power: Diesel",
                "Power: Electric"
        ));

        // 2) Construction Materials
        filters.put(2, List.of(
                "Type: Cement",
                "Type: Sand",
                "Type: Gravel",
                "Grade: Standard",
                "Grade: Premium"
        ));

        // 3) Electrical Equipment
        filters.put(3, List.of(
                "Voltage: 220V",
                "Voltage: 380V",
                "Material: Copper",
                "Material: Aluminium",
                "Phase: Single",
                "Phase: Three"
        ));

        // 4) Industrial Safety
        filters.put(4, List.of(
                "Type: Helmet",
                "Type: Gloves",
                "Type: Boots",
                "Certification: EN-20345",
                "Certification: ANSI"
        ));

        // 5) Tools & Hardware
        filters.put(5, List.of(
                "Type: Hand Tool",
                "Type: Power Tool",
                "Brand: Bosch",
                "Brand: Makita",
                "Material: Steel"
        ));

        // 6) Plumbing & HVAC
        filters.put(6, List.of(
                "Type: Pipe",
                "Type: Valve",
                "Diameter: 20mm",
                "Diameter: 40mm",
                "System: HVAC",
                "System: Water"
        ));
    }

    @Override
    public List<String> getFiltersForCategory(int categoryId) {
        return filters.getOrDefault(categoryId, List.of());
    }
}
