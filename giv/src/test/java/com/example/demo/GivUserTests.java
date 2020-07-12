//package com.example.demo;
//
//import api.GivApplication;
//import api.core.entities.apidata.JsonUser;
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
//public class GivUserTests {
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
//    public void testGetAllActiveUsers() throws Exception {
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/users")
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testGetAllChannelsForAUser() throws Exception {
//        int userId = 1;
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId + "/channels")
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testGetUser() throws Exception {
//        int id = 1;
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/users/" + id)
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testAddUser() throws Exception {
//        JsonUser jsonUser = setUserDetails();
//        String jsonRequest=mapper.writeValueAsString(jsonUser);
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders.post("/users").content(jsonRequest)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testUpdateUser() throws Exception {
//        JsonUser jsonUser = setUserDetails();
//        jsonUser.setFullName("UpdatedUser001");
//        int id = 2;
//        String jsonRequest=mapper.writeValueAsString(jsonUser);
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders.put("/users/" + id)
//                .content(jsonRequest).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testDeleteUser() throws Exception {
//        int id = 2;
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders.delete("/users/" + id)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    private JsonUser setUserDetails() {
//        JsonUser jsonUser = new JsonUser();
//        jsonUser.setApiEnabled(true);
//        jsonUser.setEmail("user001@gmail.com");
//        jsonUser.setFullName("User01");
//        jsonUser.setUserName("user001");
//
//        return jsonUser;
//    }
//}
