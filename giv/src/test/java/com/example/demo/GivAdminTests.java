//package com.example.demo;
//
//import api.GivApplication;
//import api.core.entities.apidata.JsonAdminCreate;
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
//public class GivAdminTests {
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
//    public void testGetAllActiveAdmins() throws Exception {
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/admins")
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testAddAdmin() throws Exception {
//        JsonAdminCreate jsonAdminCreate = setJsonAdminDetails();
//        String jsonRequest=mapper.writeValueAsString(jsonAdminCreate);
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders.post("/admins").content(jsonRequest)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testUpdateAdmin() throws Exception {
//        JsonAdminCreate jsonAdminCreate = setJsonAdminDetails();
//        jsonAdminCreate.setEmail("givadmin@gmail.com");
//        int id= 1;
//        String jsonRequest=mapper.writeValueAsString(jsonAdminCreate);
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders.put("/admins/" + id).content(jsonRequest)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testDeleteAdmin() throws Exception {
//        int id= 1;
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders.delete("/admins/" + id)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    private JsonAdminCreate setJsonAdminDetails() {
//        JsonAdminCreate jsonAdminCreate = new JsonAdminCreate();
//        jsonAdminCreate.setAdminName("givadmin");
//        jsonAdminCreate.setApiEnabled(true);
//        jsonAdminCreate.setEmail("givadmin1@gmail.com");
//        jsonAdminCreate.setFullName("givadmin");
//        return jsonAdminCreate;
//    }
//}
