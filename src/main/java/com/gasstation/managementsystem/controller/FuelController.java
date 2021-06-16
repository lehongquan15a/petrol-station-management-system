package com.gasstation.managementsystem.controller;

import com.gasstation.managementsystem.exception.custom.CustomNotFoundException;
import com.gasstation.managementsystem.model.dto.fuel.FuelDTO;
import com.gasstation.managementsystem.model.dto.fuel.FuelDTOCreate;
import com.gasstation.managementsystem.model.dto.fuel.FuelDTOUpdate;
import com.gasstation.managementsystem.service.FuelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
@Tag(name = "Fuel", description = "API for Fuel")
@RequiredArgsConstructor
public class FuelController {

    private final FuelService fuelService;

    @Operation(summary = "View All fuel category")
    @GetMapping("/fuels")
    public HashMap<String, Object> getAll(@RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                                          @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        if (pageSize != null) {
            return fuelService.findAll(PageRequest.of(pageIndex - 1, pageSize));
        } else {
            return fuelService.findAll();
        }

    }

    @Operation(summary = "Find fuel category by id")
    @GetMapping("/fuels/{id}")
    public FuelDTO getOne(@PathVariable(name = "id") Integer id) throws CustomNotFoundException {
        return fuelService.findById(id);
    }

    @Operation(summary = "Create new fuel category")
    @PostMapping("/fuels")
    public FuelDTO create(@Valid @RequestBody FuelDTOCreate fuelDTOCreate) {
        return fuelService.create(fuelDTOCreate);
    }

    @Operation(summary = "Update fuel category by id")
    @PutMapping("/fuels/{id}")
    public FuelDTO update(@PathVariable(name = "id") Integer id, @Valid @RequestBody FuelDTOUpdate fuelDTOUpdate) throws CustomNotFoundException {
        return fuelService.update(id, fuelDTOUpdate);
    }

    @Operation(summary = "Delete fuel category by id")
    @DeleteMapping("/fuels/{id}")
    public FuelDTO delete(@PathVariable(name = "id") Integer id) throws CustomNotFoundException {
        return fuelService.delete(id);
    }
}
