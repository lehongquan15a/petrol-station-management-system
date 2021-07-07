package com.gasstation.managementsystem.model.dto.fuelImport;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FuelImportDTOCreate {
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Date is mandatory")
    private Date date;

    @NotNull(message = "Volume is mandatory")
    private Double volume;

    @NotNull(message = "Unit price is mandatory")
    private Double unitPrice;

    @NotNull(message = "Paid is mandatory")
    private Double paid;

    @NotNull(message = "Liability is mandatory")
    private Double liability;

    @NotNull(message = "VAT percent is mandatory")
    private Double vatPercent;

    private String note;

    @NotNull(message = "Tank id is mandatory")
    private Integer tankId;

    @NotNull(message = "Supplier id is mandatory")
    private Integer supplierId;
}