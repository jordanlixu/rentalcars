package com.example.rentalcars.controller;


import cn.hutool.core.date.DateUtil;
import com.example.rentalcars.common.ResultEntity;
import com.example.rentalcars.model.RentalCars;
import com.example.rentalcars.model.dto.ReturnCarDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.rentalcars.model.dto.RentDto;
import com.example.rentalcars.service.RentalCarsService;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/rentalCars")
public class RentalCarsController {


    private final RentalCarsService rentalCarsService;

    public RentalCarsController(RentalCarsService rentalCarsService) {
        this.rentalCarsService = rentalCarsService;
    }

    @ApiOperation(value = "rent a car", notes="租车接口,按日计费，时间格式为yyyy-MM-dd")
    @PostMapping("rent")
    public ResponseEntity<ResultEntity> rentCar(@RequestBody @Valid RentDto rentDto){
        if(rentDto.getEndDay().before(rentDto.getStartDay())){
            return new ResponseEntity<>(ResultEntity.getErrorEntity("取车日期不能滞后于还车日期"),HttpStatus.OK);
        }
        if(rentalCarsService.isTheCarRentedInSpecificTime(rentDto.getCarId(),rentDto.getStartDay(),rentDto.getEndDay())){
            return new ResponseEntity<>(ResultEntity.getErrorEntity("在选择的时间段已被租用"),HttpStatus.OK);
        }
        BigDecimal rent = rentalCarsService.calcRent(rentDto.getStartDay(),rentDto.getEndDay(),rentDto.getCarId());
        RentalCars rental = rentDto.toRentalCars(rent);
        rentalCarsService.save(rental);
        ResultEntity result = new ResultEntity(rent.toString());
        result.setMsg(rentDto.getCarId()+ " rent total:"+ rent);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "return the car", notes="还车")
    @PostMapping("return")
    public ResponseEntity<ResultEntity> returnCar(@ApiParam(value = "还车日期格式yyyy-MM-dd",required = true) @RequestBody @Valid ReturnCarDto returnCarDto){
          Optional<RentalCars> opRental = rentalCarsService.queryCustomerRentalInfo(returnCarDto.getId(),returnCarDto.getPhoneNum());
          if(!opRental.isPresent()){
              return  new ResponseEntity<>(ResultEntity.getErrorEntity("There is no car to return."),HttpStatus.BAD_REQUEST);
          }
          RentalCars rental = opRental.get();
          Date returnDay = DateUtil.endOfDay(returnCarDto.getEndDay()).toSqlDate();
          if (dayNotEquals(returnDay, rental.getEndDay())){
                rental.setEndDay(returnDay);
                rental.setRent(rentalCarsService.calcRent(rental.getStartDay(),rental.getEndDay(),rental.getCarId()));
          }
          rental.setReturnFlag("Y");
          rental.setUpdateTime(new Timestamp(System.currentTimeMillis()));
          rentalCarsService.save(rental);
          ResultEntity result = new ResultEntity(rental);
          result.setMsg(rental.getCarId()+ " rent total:"+ rental.getRent());
          return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @ApiOperation(value = "query Rented Cars ", notes="查询租借的车列表")
    @GetMapping (value = "/query")
    public ResponseEntity<ResultEntity> queryRentedCars(@ApiParam(value = "客户手机号",required = true) @RequestParam String phoneNum){
        List<RentalCars> list = rentalCarsService.queryRentedCars(phoneNum,"N");
        ResultEntity result = new ResultEntity(list);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @ApiOperation(value = "clear rental data", notes="清除数据")
    @PostMapping("clear")
    public ResultEntity clear(){
        rentalCarsService.clear();
        return new ResultEntity("cleared");
    }

    private boolean dayNotEquals(Date aDay, Date bDay){
        return !(DateUtil.format(aDay, "yyyy-MM-dd")).equals(DateUtil.format(bDay, "yyyy-MM-dd"));
    }

}
