package com.example.rentalcars.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table
@ApiModel("租车明细类")
public class RentalCars {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @ApiModelProperty(value = "记录的唯一id",hidden = true)
    private long id;


    @Column(name = "phone_num")
    @ApiModelProperty("客户手机号码")
    private String phoneNum;

    @Column(name = "car_id")
    @ApiModelProperty(value = "车牌号码")
    private String carId;

    @Column(name = "start_day")
    @ApiModelProperty("取车时间")
    private Date startDay;

    @Column(name = "end_day")
    @ApiModelProperty("还车时间")
    private Date endDay;


    @Column(name = "return_flag")
    @ApiModelProperty(value = "是否归还(Y/N)",hidden = true)
    private String returnFlag;


    @Column(name = "rent")
    @ApiModelProperty(value = "租金",hidden = true)
    private BigDecimal rent;

    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间",hidden = true)
    private Timestamp createTime;

    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间",hidden = true)
    private Timestamp updateTime;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }

    public String getReturnFlag() {
        return returnFlag;
    }

    public void setReturnFlag(String returnFlag) {
        this.returnFlag = returnFlag;
    }

    public BigDecimal getRent() {
        return rent;
    }

    public void setRent(BigDecimal rent) {
        this.rent = rent;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }


}
