package com.example.rentalcars.controller;


import cn.hutool.core.date.DateUtil;
import com.example.rentalcars.common.Constants;
import com.example.rentalcars.common.ResultEntity;
import com.example.rentalcars.model.RentalCars;
import com.example.rentalcars.service.RentalCarsService;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rentalCars")
public class RentalCarsController {


    private final RentalCarsService rentalCarsService;

    public RentalCarsController(RentalCarsService rentalCarsService) {
        this.rentalCarsService = rentalCarsService;
    }

    @ApiOperation(value = "rent a car", notes="租车")
    @PostMapping("")
    @ResponseBody
    public ResultEntity rentCar(@RequestBody RentalCars rentalInfo){
        if(rentalCarsService.isTheCarRentedInSpecificTime(rentalInfo.getCarId(),rentalInfo.getStartDay(),rentalInfo.getEndDay())){
            return new ResultEntity(Constants.FAIL_CODE,"这个车在您选择的时间段已被其他客户租用");
        }
        rentalInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        rentalInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        BigDecimal rent =  BigDecimal.ZERO;
        if(rentalInfo.getEndDay().before(rentalInfo.getStartDay())){
            return new ResultEntity(Constants.FAIL_CODE,"取车日期滞后于还车日期");
        }else {
            rent = rentalCarsService.calcRent(rentalInfo.getStartDay(),rentalInfo.getEndDay(),rentalInfo.getCarId());
        }
        rentalInfo.setRent(rent);
        rentalInfo.setReturnFlag("N");
        rentalCarsService.save(rentalInfo);
        return new ResultEntity(rentalInfo.getCarId()+ " rent total:"+ rentalInfo.getRent());
    }

    @ApiOperation(value = "return the car", notes="还车")
    @PostMapping("returnCar")
    @ResponseBody
    public ResultEntity returnCar(@RequestBody RentalCars returnCar){
          Optional<RentalCars> opRental = rentalCarsService.queryCustomerRentalInfo(returnCar.getCarId(),returnCar.getPhoneNum());
          if(!opRental.isPresent()){
              return  ResultEntity.getErrorEntity("There is no car to return.");
          }
          RentalCars rental = opRental.get();
          if (!nowDayEquals(rental.getEndDay())){
              rental.setEndDay(DateUtil.endOfDay(new java.util.Date()).toSqlDate());
              rental.setRent(rentalCarsService.calcRent(rental.getStartDay(),rental.getEndDay(),rental.getCarId()));
          }
          rental.setReturnFlag("Y");
          rental.setUpdateTime(new Timestamp(System.currentTimeMillis()));
          rentalCarsService.save(rental);
          return new ResultEntity(rental.getCarId() + " rent total:"+ rental.getRent());
    }


    @ApiOperation(value = "clear rental data", notes="清除数据")
    @PostMapping("clear")
    @ResponseBody
    public ResultEntity clear(@RequestBody RentalCars returnCar){
        rentalCarsService.clear();
        return new ResultEntity("cleared");
    }

    private boolean nowDayEquals(Date endDay){
        return DateUtil.today().equals(DateUtil.format(endDay, "yyyy-MM-dd"));
    }

}
