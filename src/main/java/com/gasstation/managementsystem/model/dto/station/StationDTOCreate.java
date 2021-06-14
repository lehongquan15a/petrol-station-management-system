package com.gasstation.managementsystem.model.dto.station;

import com.gasstation.managementsystem.entity.Station;
import com.gasstation.managementsystem.model.dto.user.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class StationDTOCreate {

    @Schema(example = "station1")
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Address is mandatory")
    private String address;
    @Positive(message = "owner id is a positive number")
    private int ownerId;
}