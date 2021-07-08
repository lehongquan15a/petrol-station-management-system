package com.gasstation.managementsystem.controller;


import com.gasstation.managementsystem.exception.custom.CustomNotFoundException;
import com.gasstation.managementsystem.model.dto.transaction.TransactionDTO;
import com.gasstation.managementsystem.model.dto.transaction.TransactionDTOCreate;
import com.gasstation.managementsystem.model.dto.transaction.TransactionDTOUpdate;
import com.gasstation.managementsystem.service.TransactionService;
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
@Tag(name = "Transaction", description = "API for Transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(summary = "View All Transaction")
    @GetMapping("/transactions")
    public HashMap<String, Object> getAll(@RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                                          @RequestParam(name = "pageSize", defaultValue = "2") Integer pageSize) {
        return transactionService.findAll(PageRequest.of(pageIndex - 1, pageSize));
    }

    @Operation(summary = "Find Transaction by id")
    @GetMapping("/transactions/{id}")
    public TransactionDTO getOne(@PathVariable(name = "id") Integer id) throws CustomNotFoundException {
        return transactionService.findById(id);
    }

    @Operation(summary = "Create new Transaction")
    @PostMapping("/transactions")
    public TransactionDTO create(@Valid @RequestBody TransactionDTOCreate transactionDTOCreate) throws CustomNotFoundException {
        return transactionService.create(transactionDTOCreate);
    }

    @Operation(summary = "Update Transaction by id")
    @PutMapping("/transactions/{id}")
    public TransactionDTO update(@PathVariable(name = "id") Integer id, @Valid @RequestBody TransactionDTOUpdate transactionDTOUpdate) throws CustomNotFoundException {
        return transactionService.update(id, transactionDTOUpdate);
    }

    @Operation(summary = "Delete Transaction by id")
    @DeleteMapping("/transactions/{id}")
    public TransactionDTO delete(@PathVariable(name = "id") Integer id) throws CustomNotFoundException {
        return transactionService.delete(id);
    }
}
