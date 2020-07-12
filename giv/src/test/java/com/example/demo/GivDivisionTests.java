//package com.example.demo;
//
//import api.GivApplication;
//import api.core.entities.Division;
//import api.core.entities.DivisionKey;
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
//public class GivDivisionTests {
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
//    public void testGetAllDivisionsByCompanyKey() throws Exception {
//        String companyKey = "LEONISA";
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/companies/" + companyKey + "/divisions")
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testGetAllDivisions() throws Exception {
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/divisions")
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testGetDivisionByDivisionKey() throws Exception {
//        String divisionKey = "LEONISA";
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/divisions/" + divisionKey)
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testGetDivisionByCompanyKeyAndDivisionKey() throws Exception {
//        String companyKey = "LEONISA";
//        String divisionKey = "LEONISA";
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.
//                get("/companies/" + companyKey + "/divisions/" + divisionKey)
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testAddDivision() throws Exception {
//        Division division = setDivisionDetails();
//        String companyKey = "LEONISA";
//        String jsonRequest=mapper.writeValueAsString(division);
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders
//                .post("/companies/" + companyKey + "/divisions").content(jsonRequest)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testUpdateDivision() throws Exception {
//        Division division = setDivisionDetails();
//        division.setDescription("Updated Division Description");
//        String companyKey = "LEONISA";
//        String divisionKey = "LEONISA";
//        String jsonRequest=mapper.writeValueAsString(division);
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders
//                .put("/companies/" + companyKey + "/divisions/" + divisionKey)
//                .content(jsonRequest).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testDeleteDivisionByCompanyKeyAndDivisionKey() throws Exception {
//        String companyKey = "LEONISA";
//        String divisionKey = "LEONISA";
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders
//                .delete("/companies/" + companyKey + "/divisions/" + divisionKey)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testDeleteDivisionByDivisionKey() throws Exception {
//        String divisionKey = "LEONISA";
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders.delete("/divisions/" + divisionKey)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    private Division setDivisionDetails() {
//        Division division = new Division();
//        division.setCompCode("LEONISA");
//        division.setId(setDivisionKeyDetails());
//        division.setName("Division1");
//        division.setDescription("Division Description");
//        division.setAddress1("Subham Complex");
//        division.setCity("Bangalore");
//        division.setState("Karnataka");
//        division.setZip("560078");
//        division.setCountry("India");
//        division.setContactName("Division Contact");
//        division.setContactNumber("1234567890");
//
//        return division;
//    }
//
//    private DivisionKey setDivisionKeyDetails() {
//        DivisionKey divisionKey = new DivisionKey();
//        divisionKey.setCode("LEONISA");
//
//        return divisionKey;
//    }
//}
