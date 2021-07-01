package com.gasstation.managementsystem.service.impl;

import com.gasstation.managementsystem.entity.Fuel;
import com.gasstation.managementsystem.entity.Station;
import com.gasstation.managementsystem.entity.Tank;
import com.gasstation.managementsystem.exception.custom.CustomNotFoundException;
import com.gasstation.managementsystem.model.CustomError;
import com.gasstation.managementsystem.model.dto.tank.TankDTO;
import com.gasstation.managementsystem.model.dto.tank.TankDTOCreate;
import com.gasstation.managementsystem.model.dto.tank.TankDTOUpdate;
import com.gasstation.managementsystem.model.mapper.TankMapper;
import com.gasstation.managementsystem.repository.TankRepository;
import com.gasstation.managementsystem.service.TankService;
import com.gasstation.managementsystem.utils.OptionalValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TankServiceImpl implements TankService {
    private final TankRepository tankRepository;
    private final OptionalValidate optionalValidate;

    private HashMap<String, Object> listTankToMap(List<Tank> tanks) {
        List<TankDTO> tankDTOS = tanks.stream().map(TankMapper::toTankDTO).collect(Collectors.toList());
        HashMap<String, Object> map = new HashMap<>();
        map.put("data", tankDTOS);
        return map;
    }

    @Override
    public HashMap<String, Object> findAll(Pageable pageable) {
        Page<Tank> tanks = tankRepository.findAll(pageable);
        HashMap<String, Object> map = listTankToMap(tanks.getContent());
        map.put("totalElement", tanks.getTotalElements());
        map.put("totalPage", tanks.getTotalPages());
        return map;
    }

    @Override
    public HashMap<String, Object> findAll(Sort sort) {
        return listTankToMap(tankRepository.findAll(sort));
    }

    @Override
    public TankDTO findById(int id) throws CustomNotFoundException {
        return TankMapper.toTankDTO(optionalValidate.getTankById(id));
    }

    @Override
    public TankDTO create(TankDTOCreate tankDTOCreate) throws CustomNotFoundException {
        checkDuplicate(tankDTOCreate.getName(), tankDTOCreate.getStationId());
        Station station = optionalValidate.getStationById(tankDTOCreate.getStationId());
        Fuel fuel = optionalValidate.getFuelById(tankDTOCreate.getFuelId());
        Tank tank = TankMapper.toTank(tankDTOCreate);
        tank.setStation(station);
        tank.setFuel(fuel);
        tankRepository.save(tank);
        return TankMapper.toTankDTO(tank);
    }

    private void checkDuplicate(String name, Integer stationId) throws CustomNotFoundException {
        if (name != null && stationId != null) {
            Optional<Tank> tankOptional = tankRepository.findByNameAndStationId(name, stationId);
            if (tankOptional.isPresent()) {
                throw new CustomNotFoundException(CustomError.builder()
                        .code("not.found").field("(name,stationId)").message("Name and stationId is not exist").table("api_table").build());
            }
        }
    }

    @Override
    public TankDTO update(int id, TankDTOUpdate tankDTOUpdate) throws CustomNotFoundException {
        Tank oldTank = optionalValidate.getTankById(id);
        String name = tankDTOUpdate.getName();
        Integer stationId = tankDTOUpdate.getStationId();
        if (name != null && name.equals(oldTank.getName())) {
            name = null;
        }
        if (stationId != null && oldTank.getStation() != null
                && stationId != oldTank.getStation().getId()) {
            stationId = null;
        }
        checkDuplicate(name, stationId);
        TankMapper.copyNonNullToTank(oldTank, tankDTOUpdate);
        if (tankDTOUpdate.getStationId() != null) {
            Station station = optionalValidate.getStationById(tankDTOUpdate.getStationId());
            oldTank.setStation(station);
        }
        if (tankDTOUpdate.getFuelId() != null) {
            Fuel fuel = optionalValidate.getFuelById(tankDTOUpdate.getFuelId());
            oldTank.setFuel(fuel);
        }
        oldTank = tankRepository.save(oldTank);
        return TankMapper.toTankDTO(oldTank);
    }


    @Override
    public TankDTO delete(int id) throws CustomNotFoundException {
        Tank tank = optionalValidate.getTankById(id);
        tankRepository.delete(tank);
        return TankMapper.toTankDTO(tank);
    }
}
