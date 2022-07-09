package com.example.rentalcars;


import com.example.rentalcars.controller.RentalCarsController;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RentalCarsControllerTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private RentalCarsController controller;

    private MockMvc mockMvc;


    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        //mockMvc.perform(post("/rentalCars/clear").contentType(MediaType.APPLICATION_JSON).content(""));
    }




    @Test
    public void Test_01_RentCarSuccess() throws Exception{
        String requestJson = "{\"phoneNum\":\"13760403346\",\"carId\":\"KY880\",\"startDay\":\"2022-07-06\",\"endDay\":\"2022-07-08\"}";
        mockMvc.perform(post("/rentalCars").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":\"000000\",\"msg\":\"succeed\",\"data\":\"KY880 rent total:270.00\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    @Test
    public void Test_02_RentTheSameCarAtAnotherTimeSuccess() throws Exception{
        String requestJson = "{\"phoneNum\":\"13760403346\",\"carId\":\"KY880\",\"startDay\":\"2022-07-06\",\"endDay\":\"2022-07-08\"}";
        mockMvc.perform(post("/rentalCars").contentType(MediaType.APPLICATION_JSON).content(requestJson));
        requestJson =  "{\"phoneNum\":\"13760403346\",\"carId\":\"KY880\",\"startDay\":\"2022-07-09\",\"endDay\":\"2022-07-10\"}";
        mockMvc.perform(post("/rentalCars").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("000000")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }


    @Test
    public void Test_03_RentTheSameCarAtOverlappedTimeFail() throws Exception{
        /*
            {
           "phoneNum":"13760403346",
           "carId":"AJ889",
           "startDay":"2022-07-06",
           "endDay":"2022-07-08"
            }
         */

        String requestJson = "{\"phoneNum\":\"13760403346\",\"carId\":\"KY880\",\"startDay\":\"2022-07-06\",\"endDay\":\"2022-07-08\"}";
        mockMvc.perform(post("/rentalCars").contentType(MediaType.APPLICATION_JSON).content(requestJson));
        requestJson =  "{\"phoneNum\":\"13760403346\",\"carId\":\"KY880\",\"startDay\":\"2022-07-07\",\"endDay\":\"2022-07-10\"}";
        mockMvc.perform(post("/rentalCars").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":\"111111\",\"msg\":\"这个车在您选择的时间段已被其他客户租用\",\"data\":null}"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();


    }

    @Test
    public void Test_04_ReturnTheCar() throws Exception{
        String requestJson = "{\"phoneNum\":\"13760403346\",\"carId\":\"KY880\",\"startDay\":\"2022-07-06\",\"endDay\":\"2022-07-09\"}";
        mockMvc.perform(post("/rentalCars").contentType(MediaType.APPLICATION_JSON).content(requestJson));
        requestJson = "{\"phoneNum\":\"13760403346\",\"carId\":\"KY880\"}";
        mockMvc.perform(post("/rentalCars/returnCar").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk())
                //.andExpect(content().json("{\"code\":\"000000\",\"msg\":\"succeed\",\"data\":\"KY880 rent total:270.00\"}"))
                .andExpect(content().string(Matchers.containsString("000000")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }
}
