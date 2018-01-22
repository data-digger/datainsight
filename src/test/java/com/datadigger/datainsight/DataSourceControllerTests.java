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
public class DataSourceControllerTests {
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Before
    public void setUp() throws Exception {
    	 mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    public void getAllDataSources() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/datasource/list").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
   
//    @Test
//    public void createChart() throws Exception {
//    Chart chart = new Chart();
//    chart.setId("CR.TEST");
//    chart.setBizViewId("BZ.TEST");
//    chart.setName("ChartName");
//    chart.setAlias("ChartAlias");
//    chart.setDesc("ChartDesc");
//    chart.setDefineJSON("ChartDefine");
//    ObjectMapper mapper = new ObjectMapper();
//      ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//      java.lang.String requestJson = ow.writeValueAsString(chart);
//      
//    mvc.perform(MockMvcRequestBuilders.post("/chart/new").contentType(MediaType.APPLICATION_JSON).content(requestJson))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
//    
    @Test
    public void getHello() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/hello").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}

