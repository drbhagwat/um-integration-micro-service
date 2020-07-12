//package com.example.demo;
//
//import api.GivApplication;
//import api.core.entities.apidata.JsonLogin;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes= GivApplication.class)
//@AutoConfigureMockMvc
//@TestPropertySource(value = "classpath:application.yml")
//public class GivLoginTests {
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper mapper;
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testLogin() throws Exception {
//        JsonLogin jsonLogin = setJsonLoginDetails();
//        String jsonRequest=mapper.writeValueAsString(jsonLogin);
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders.post("/login").content(jsonRequest)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    private JsonLogin setJsonLoginDetails() {
//        JsonLogin jsonLogin = new JsonLogin();
//        jsonLogin.setUserName("givadmin");
//        jsonLogin.setPassword("admin001password");
//
//        return jsonLogin;
//    }
//}
