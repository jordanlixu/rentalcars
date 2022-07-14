package com.example.rentalcars.model.dto;

import com.example.rentalcars.model.RentalCars;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;


@Data
public class RentDto {


    @NotBlank(message = "The phoneNum is required.")
    @ApiModelProperty("客户手机号码")
    private String phoneNum;

    @NotBlank(message = "The carId is required.")
    @ApiModelProperty(value = "车牌号码")
    private String carId;

    @NotNull(message = "The startDay is required.")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @ApiModelProperty("取车时间")
    private Date startDay;

    @NotNull(message = "The endDay is required.")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @ApiModelProperty("还车时间")
    private Date endDay;


    public RentalCars toRentalCars(BigDecimal rent){
        return  new RentalCars().setCarId(carId)
                                .setPhoneNum(phoneNum)
                                .setStartDay(startDay)
                                .setEndDay(endDay)
                                .setReturnFlag("N")
                                .setCreateTime(new Timestamp(System.currentTimeMillis()))
                                .setUpdateTime(new Timestamp(System.currentTimeMillis()))
                                .setRent(rent);

    }
}
