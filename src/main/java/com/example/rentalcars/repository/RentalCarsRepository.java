package com.example.rentalcars.repository;

import com.example.rentalcars.model.RentalCars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalCarsRepository extends JpaRepository<RentalCars,Long> {

    public List<RentalCars> findRentalCarsByCarIdAndReturnFlag(String carId, String returnFlag);

    public Optional<RentalCars> findRentalCarsByIdAndPhoneNumAndReturnFlag(long id, String phoneNum,String returnFlag);

    public List<RentalCars>  findRentalCarsByPhoneNumAndReturnFlag(String phoneNum,String returnFlag);
}
