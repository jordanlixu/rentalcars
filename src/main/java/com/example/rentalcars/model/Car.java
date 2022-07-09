package com.example.rentalcars.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table
@ApiModel("出租的轿车类")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "car_id")
    private String carId;

    private String model;


    @Column(name = "rent_per_day")
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
