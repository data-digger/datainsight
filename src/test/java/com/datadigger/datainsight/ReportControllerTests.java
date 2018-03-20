package com.datadigger.datainsight;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.datadigger.datainsight.domain.Chart;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportControllerTests {
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Before
    public void setUp() throws Exception {
    	 mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    public void getReportData() throws Exception {
    String reportID = "RP.Sakila";
    mvc.perform(MockMvcRequestBuilders.get("/report/getdata").accept(MediaType.APPLICATION_JSON).param("reportID",reportID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
  
}

