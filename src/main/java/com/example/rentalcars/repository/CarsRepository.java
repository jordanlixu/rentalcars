package com.example.rentalcars.repository;

import com.example.rentalcars.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CarsRepository extends JpaRepository<Car,Long> {

     public Car findCarByCarId(String carId);


}
