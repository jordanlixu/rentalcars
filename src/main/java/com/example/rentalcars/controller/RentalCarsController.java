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
import java.util.Optional;

import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rentalCars")
public class RentalCarsController {


    private final RentalCarsService rentalCarsService;

    public RentalCarsController(RentalCarsService rentalCarsService) {
        this.rentalCarsService = rentalCarsService;
    }

    @ApiOperation(value = "rent a car", notes="租车接口,按日计费，时间格式为yyyy-MM-dd")
    @PostMapping("")
    @ResponseBody
    public ResultEntity rentCar(@RequestBody RentalCars rentalInfo){
        if(rentalInfo.getCarId() == null || rentalInfo.getCarId().isEmpty()){
            return new ResultEntity(Constants.FAIL_CODE,"车牌号不能为空");
        }
        if(rentalInfo.getPhoneNum() == null || rentalInfo.getPhoneNum().isEmpty()){
            return new ResultEntity(Constants.FAIL_CODE,"手机号不能为空");
        }
        if(rentalInfo.getStartDay() == null || rentalInfo.getEndDay() == null){
            return new ResultEntity(Constants.FAIL_CODE,"租车或还车时间不能为空");
        }
        if(rentalCarsService.isTheCarRentedInSpecificTime(rentalInfo.getCarId(),rentalInfo.getStartDay(),rentalInfo.getEndDay())){
            return new ResultEntity(Constants.FAIL_CODE,"这个车在您选择的时间段已被其他客户租用");
        }
        if(rentalInfo.getEndDay().before(rentalInfo.getStartDay())){
            return new ResultEntity(Constants.FAIL_CODE,"取车日期滞后于还车日期");
        }
        BigDecimal rent = rentalCarsService.calcRent(rentalInfo.getStartDay(),rentalInfo.getEndDay(),rentalInfo.getCarId());
        rentalInfo.setRent(rent);
        rentalInfo.setReturnFlag("N");
        rentalInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        rentalInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        rentalCarsService.save(rentalInfo);
        return new ResultEntity(rentalInfo.getCarId()+ " rent total:"+ rentalInfo.getRent());
    }

    @ApiOperation(value = "return the car", notes="还车")
    @PostMapping("returnCar")
    @ResponseBody
    public ResultEntity returnCar(@ApiParam(value = "还车接口，车牌和手机号必填，startDay、endDay非必填",required = true) @RequestBody RentalCars returnCar){
          Optional<RentalCars> opRental = rentalCarsService.queryCustomerRentalInfo(returnCar.getCarId(),returnCar.getPhoneNum());
          if(!opRental.isPresent()){
              return  ResultEntity.getErrorEntity("There is no car to return.");
          }
          RentalCars rental = opRental.get();
          if (returnCar.getStartDay()!= null && dayNotEquals(rental.getStartDay(), returnCar.getStartDay())){
            return  ResultEntity.getErrorEntity("租车开始时间不正确");
          }
          if(returnCar.getEndDay() == null){
              returnCar.setEndDay(DateUtil.endOfDay(new java.util.Date()).toSqlDate());
          }
          if (dayNotEquals(returnCar.getEndDay(), rental.getEndDay())){
              rental.setEndDay(returnCar.getEndDay());
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
    public ResultEntity clear(){
        rentalCarsService.clear();
        return new ResultEntity("cleared");
    }

    private boolean dayNotEquals(Date aDay, Date bDay){
        return !(DateUtil.format(aDay, "yyyy-MM-dd")).equals(DateUtil.format(bDay, "yyyy-MM-dd"));
    }

}
