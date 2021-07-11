package com.gasstation.managementsystem.model.dto.transaction;

import com.gasstation.managementsystem.model.dto.card.CardDTO;
import com.gasstation.managementsystem.model.dto.handOverShift.HandOverShiftDTO;
import com.gasstation.managementsystem.model.dto.pump.PumpDTO;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TransactionDTO {
    private int id;
    private Long time;
    private Double volume;
    private Double unitPrice;
    private String uuid;
    private CardDTO card;
    private PumpDTO pump;
    private HandOverShiftDTO handOverShift;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDTO that = (TransactionDTO) o;
        return id == that.id && Objects.equals(time, that.time) && Objects.equals(volume, that.volume) && Objects.equals(unitPrice, that.unitPrice) && Objects.equals(card, that.card);
    }

}
