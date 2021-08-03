package com.gasstation.managementsystem.model.mapper;

import com.gasstation.managementsystem.entity.*;
import com.gasstation.managementsystem.model.dto.card.CardDTO;
import com.gasstation.managementsystem.model.dto.debt.DebtDTO;
import com.gasstation.managementsystem.model.dto.fuel.FuelDTO;
import com.gasstation.managementsystem.model.dto.handOverShift.HandOverShiftDTO;
import com.gasstation.managementsystem.model.dto.pump.PumpDTO;
import com.gasstation.managementsystem.model.dto.station.StationDTO;
import com.gasstation.managementsystem.model.dto.tank.TankDTO;
import com.gasstation.managementsystem.model.dto.transaction.TransactionDTO;
import com.gasstation.managementsystem.model.dto.user.UserDTO;

public class DebtMapper {
    public static DebtDTO toDebtDTO(Debt debt) {
        Card card = debt.getTransaction().getCard();
        User customer = card != null ? card.getCustomer() : null;
        UserDTO customerDTO = customer != null ? UserDTO.builder().id(customer.getId()).name(customer.getName()).phone(customer.getPhone()).build() : null;
        CardDTO cardDTO = card != null ? CardDTO.builder().id(card.getId()).customer(customerDTO).build() : null;
        Station station = debt.getTransaction().getHandOverShift().getShift().getStation();
        StationDTO stationDTO = station != null ? StationDTO.builder()
                .id(station.getId())
                .name(station.getName())
                .address(station.getAddress())
                .build() : null;
        Transaction transaction = debt.getTransaction();
        HandOverShift handOverShift = transaction != null ? transaction.getHandOverShift() : null;
        Pump pump = handOverShift != null ? handOverShift.getPump() : null;
        Tank tank = pump != null ? pump.getTank() : null;
        Fuel fuel = tank != null ? tank.getFuel() : null;
        FuelDTO fuelDTO = fuel != null ? FuelDTO.builder()
                .id(fuel.getId())
                .name(fuel.getName()).build() : null;
        TankDTO tankDTO = tank != null ? TankDTO.builder()
                .id(tank.getId())
                .fuel(fuelDTO).build() : null;
        PumpDTO pumpDTO = pump != null ? PumpDTO.builder()
                .id(pump.getId())
                .tank(tankDTO).build() : null;
        HandOverShiftDTO handOverShiftDTO = handOverShift != null ? HandOverShiftDTO.builder()
                .id(handOverShift.getId())
                .pump(pumpDTO).build() : null;
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .id(transaction.getId())
                .time(transaction.getTime())
                .volume(transaction.getVolume())
                .unitPrice(transaction.getUnitPrice())
                .handOverShift(handOverShiftDTO).build();
        return DebtDTO.builder()
                .id(debt.getId())
                .card(cardDTO)
                .station(stationDTO)
                .transaction(transactionDTO).build();
    }
}
