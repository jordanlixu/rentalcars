package com.example.rentalcars.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table
@ApiModel("出租的轿车类")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @ApiModelProperty("主键id")
    private long id;

    @Column(name = "car_id")
    @ApiModelProperty("车牌号码")
    private String carId;


    @ApiModelProperty("车型")
    private String model;


    @Column(name = "rent_per_day")
    @ApiModelProperty("每天的租金")
    private BigDecimal rentPerDay;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getRentPerDay() {
        return rentPerDay;
    }

    public void setRentPerDay(BigDecimal rentPerDay) {
        this.rentPerDay = rentPerDay;
    }



}
