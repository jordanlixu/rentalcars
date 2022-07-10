package com.example.rentalcars.service;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.example.rentalcars.model.Car;
import com.example.rentalcars.model.RentalCars;
import com.example.rentalcars.repository.CarsRepository;
import com.example.rentalcars.repository.RentalCarsRepository;
import org.springframework.stereotype.Service;

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

    public boolean isTheCarRentedInSpecificTime(String carId, Date startDay, Date endDay) {
       boolean isTheCarRented = false;
       List<RentalCars> list  = rentalCarsRepository.findRentalCarsByCarIdAndReturnFlag(carId,"N");
        for (RentalCars rentalInfo:list) {
            if(rentalInfo.getStartDay().after(endDay)||rentalInfo.getEndDay().before(startDay)){
                continue;
            }else {
                isTheCarRented = true;
                break;
            }
        }
       return isTheCarRented;
    }

    public BigDecimal calcRent(Date startDay, Date endDay, String carId){
//        long difference = (endDay.getTime()-startDay.getTime())/86400000;
//        long days = Math.abs(difference)+1;  //按天计算费用
        if(endDay.before(startDay)){
            new BigDecimal(0);
        }
        long betweenDay = DateUtil.between(startDay, endDay, DateUnit.DAY)+1;


        Car car = carsRepository.findCarByCarId(carId);
        if (car!=null){
            return car.getRentPerDay().multiply(new BigDecimal(betweenDay));
        }else {
            throw new IllegalArgumentException("车牌号不存在");
        }
    }

    public  Optional<RentalCars>  queryCustomerRentalInfo(String carId, String phoneNum) {
        List<RentalCars> rentalInfoList = rentalCarsRepository.findRentalCarsByCarIdAndPhoneNumOrderByStartDay(carId,phoneNum);
        Optional<RentalCars> rental = rentalInfoList.stream().filter(s->s.getReturnFlag().equals("N")).findFirst();
        return rental ;
    }

    public void clear(){
        rentalCarsRepository.deleteAll();
    }
}
