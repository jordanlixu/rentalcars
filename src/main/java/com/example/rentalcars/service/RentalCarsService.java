package com.example.rentalcars.service;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.example.rentalcars.model.Car;
import com.example.rentalcars.model.RentalCars;
import com.example.rentalcars.repository.CarsRepository;
import com.example.rentalcars.repository.RentalCarsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RentalCarsService {


    private final RentalCarsRepository rentalCarsRepository;

    private final CarsRepository carsRepository;

    public RentalCarsService(RentalCarsRepository rentalCarsRepository, CarsRepository carsRepository) {
        this.rentalCarsRepository = rentalCarsRepository;
        this.carsRepository = carsRepository;
    }

    public void save(RentalCars rentalInfo){
        rentalCarsRepository.save(rentalInfo);
    }

    @Transactional
    public boolean isTheCarRentedInSpecificTime(String carId, Date startDay, Date endDay) {
        Car car = carsRepository.findCarByCarId(carId);
        if (car==null){
            throw new IllegalArgumentException("车牌号不存在");
        }
        List<RentalCars> list  = rentalCarsRepository.findRentalCarsByCarIdAndReturnFlag(carId,"N");
        boolean isTheCarNotRented = list.stream().allMatch(i -> i.getStartDay().after(endDay)||i.getEndDay().before(startDay));
        return !isTheCarNotRented;
    }


    public BigDecimal calcRent(Date startDay, Date endDay, String carId){
        if(endDay.before(startDay)){
            return new BigDecimal(0);
        }
        long betweenDay = DateUtil.between(startDay, endDay, DateUnit.DAY)+1;


        Car car = carsRepository.findCarByCarId(carId);
        if (car!=null){
            return car.getRentPerDay().multiply(new BigDecimal(betweenDay));
        }else {
            throw new IllegalArgumentException("车牌号不存在");
        }
    }

    public  Optional<RentalCars>  queryCustomerRentalInfo(long id, String phoneNum) {
        return  rentalCarsRepository.findRentalCarsByIdAndPhoneNumAndReturnFlag(id,phoneNum,"N");
    }

    public void clear(){
        rentalCarsRepository.deleteAll();
    }

    public List<RentalCars> queryRentedCars(String phoneNum,String returnFlag) {
        return rentalCarsRepository.findRentalCarsByPhoneNumAndReturnFlag(phoneNum,returnFlag);
    }
}
