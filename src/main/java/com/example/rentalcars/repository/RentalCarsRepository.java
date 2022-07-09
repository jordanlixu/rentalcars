package com.example.rentalcars.repository;

import com.example.rentalcars.model.RentalCars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalCarsRepository extends JpaRepository<RentalCars,Long> {

    public List<RentalCars> findRentalCarsByCarIdAndReturnFlag(String carId, String returnFlag);

    public  List<RentalCars>  findRentalCarsByCarIdAndPhoneNumOrderByStartDay(String carId,String phoneNum);

}
