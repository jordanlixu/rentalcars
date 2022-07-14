package com.example.rentalcars.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.sql.Date;

@Data
public class ReturnCarDto {

    @NotNull(message = "ID is required")
    @ApiModelProperty(value = "记录的唯一id")
    private long id;

    @NotBlank(message = "The phoneNum is required.")
    @ApiModelProperty("客户手机号码")
    private String phoneNum;

    @NotNull(message = "The endDay is required.")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @ApiModelProperty("还车时间")
    private Date endDay;
}
