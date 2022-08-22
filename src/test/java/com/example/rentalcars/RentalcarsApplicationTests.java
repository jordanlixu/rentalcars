package com.example.rentalcars;


import com.alibaba.fastjson.JSONArray;
import com.example.rentalcars.common.Constants;
import com.example.rentalcars.controller.RentalCarsController;
import com.alibaba.fastjson.JSONObject;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RentalcarsApplicationTests extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private RentalCarsController controller;

    private MockMvc mockMvc;


    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void Test_01_RentCarSuccess() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("phoneNum", "13760403346");
        body.put("carId", "KY880");
        body.put("startDay", "2022-07-06");
        body.put("endDay", "2022-07-08");
        String requestJson = JSONObject.toJSONString(body);
        Map<String, String> expectResponseBody = new HashMap<>();
        expectResponseBody.put("code", Constants.SUCCESS_CODE);
        expectResponseBody.put("msg", "KY880 rent total:270.00");
        expectResponseBody.put("data", new BigDecimal("270.00").toString());
        String expectJson = JSONObject.toJSONString(expectResponseBody);
        // String requestJson = "{\"phoneNum\":\"13760403346\",\"carId\":\"KY880\",\"startDay\":\"2022-07-06\",\"endDay\":\"2022-07-08\"}";
        mockMvc.perform(post("/rentalCars/rent").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(expectJson))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    @Test
    public void Test_02_RentTheSameCarAtAnotherTimeSuccess() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("phoneNum", "13760403346");
        body.put("carId", "KY880");
        body.put("startDay", "2022-07-06");
        body.put("endDay", "2022-07-08");
        String requestJson = JSONObject.toJSONString(body);

        mockMvc.perform(post("/rentalCars/rent").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andReturn();
        body.put("startDay", "2022-07-09");
        body.put("endDay", "2022-07-10");
        requestJson = JSONObject.toJSONString(body);

        // String requestJson = "{\"phoneNum\":\"13760403346\",\"carId\":\"KY880\",\"startDay\":\"2022-07-06\",\"endDay\":\"2022-07-08\"}";
        mockMvc.perform(post("/rentalCars/rent").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("000000")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }


    @Test
    public void Test_03_RentTheSameCarAtOverlappedTimeFail() throws Exception {

        Map<String, String> body = new HashMap<>();
        body.put("phoneNum", "13760403346");
        body.put("carId", "KY880");
        body.put("startDay", "2022-07-06");
        body.put("endDay", "2022-07-08");
        String requestJson = JSONObject.toJSONString(body);

        mockMvc.perform(post("/rentalCars/rent").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andReturn();
        body.put("startDay", "2022-07-07");
        body.put("endDay", "2022-07-10");
        requestJson = JSONObject.toJSONString(body);

        // String requestJson = "{\"phoneNum\":\"13760403346\",\"carId\":\"KY880\",\"startDay\":\"2022-07-06\",\"endDay\":\"2022-07-08\"}";
        mockMvc.perform(post("/rentalCars/rent").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk())
                //.andExpect(content().string(Matchers.containsString("000000")))
                .andExpect(content().json("{\"code\":\"111111\",\"msg\":\"在选择的时间段已被租用\",\"data\":null}"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    @Test
    public void Test_04_ReturnTheCar() throws Exception {


        Map<String, Object> body = new HashMap<>();
        body.put("phoneNum", "13760403346");
        body.put("carId", "KY880");
        body.put("startDay", "2022-07-06");
        body.put("endDay", "2022-07-08");
        String requestJson = JSONObject.toJSONString(body);
        mockMvc.perform(post("/rentalCars/rent").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andReturn();


        MvcResult mvcResult = mockMvc.perform(get("/rentalCars/query?phoneNum=13760403346")).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Integer id = getId(content);
        body.clear();
        body.put("id", id);
        body.put("phoneNum", "13760403346");
        body.put("endDay", "2022-07-10");
        requestJson = JSONObject.toJSONString(body);

        // String requestJson = "{\"phoneNum\":\"13760403346\",\"carId\":\"KY880\",\"startDay\":\"2022-07-06\",\"endDay\":\"2022-07-08\"}";
        mockMvc.perform(post("/rentalCars/return").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("000000")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    private Integer getId(String content) {
        JSONArray object = (JSONArray) JSONObject.parseObject(content).get("data");
        JSONObject object1 = (JSONObject) object.get(0);
        return (Integer) object1.get("id");
    }

    @Test
    public void Test_05_ShouldFail() throws Exception {
        Assertions.assertEquals(4, 1 + 6);
    }
}