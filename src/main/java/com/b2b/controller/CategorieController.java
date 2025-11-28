package com.b2b.controller;

import com.b2b.dto.CategoryDto;
import com.b2b.mapper.DtoMapper;
import com.b2b.service.CategorieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
@RequiredArgsConstructor
public class CategorieController {

    private final CategorieService categorieService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAll() {
        List<CategoryDto> dtos = categorieService.findAll()
                .stream()
                .map(DtoMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> findById(@PathVariable Integer id) {
        return categorieService.findById(id)
                .map(DtoMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryDto> findByName(@PathVariable String name) {
        return categorieService.findByName(name)
                .map(DtoMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto dto) {
        var entity = categorieService.create(dto);
        return ResponseEntity.ok(DtoMapper.toDto(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Integer id, @RequestBody CategoryDto dto) {
        try {
            var updated = categorieService.update(id, dto);
            return ResponseEntity.ok(DtoMapper.toDto(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        categorieService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/filters")
    public ResponseEntity<?> getFilters(@PathVariable Integer id) {
        var filters = categorieService.getFiltersForCategory(id);
        return ResponseEntity.ok(filters);
    }

}
