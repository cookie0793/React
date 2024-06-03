package org.zerock.carddatebase.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.carddatebase.domain.Car;
import org.zerock.carddatebase.domain.Owner;
import org.zerock.carddatebase.repository.CarRepository;
import org.zerock.carddatebase.repository.OwnerRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OwnerCarController {

    private final OwnerRepository ownerRepository;
    private final CarRepository carRepository;

    @RequestMapping("/owners")
    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }

    @RequestMapping("/cars")
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
}
