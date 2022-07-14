package com.example.rentalcars.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table
@Data
@Accessors(chain = true)
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


}
