//package com.example.demo;
//
//import api.GivApplication;
//import api.core.entities.ManufacturingPlant;
//import api.core.entities.ManufacturingPlantKey;
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
//public class GivManufacturingPlantTests {
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
//    public void testGetAllManufacturingPlantsByCompanyKey() throws Exception {
//        String companyKey = "LEONISA";
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders
//                .get("/companies/" + companyKey + "/manufacturingplants")
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testGetManufacturingPlantByCompanyKeyAndManufacturingPlantKey() throws Exception {
//        String companyKey = "LEONISA";
//        String manufacturingPlantKey = "MFG001";
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders
//                .get("/companies/" + companyKey + "/manufacturingplants/" + manufacturingPlantKey)
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testGetAllManufacturingPlants() throws Exception {
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/manufacturingplants")
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testGetManufacturingPlantByManufacturingPlantKey() throws Exception {
//        String manufacturingPlantKey = "MFG001";
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders
//                .get("/manufacturingplants/" + manufacturingPlantKey)
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testAddManufacturingPlant() throws Exception {
//        ManufacturingPlant manufacturingPlant = setManufacturingPlantDetails();
//        String companyKey = "LEONISA";
//        String jsonRequest=mapper.writeValueAsString(manufacturingPlant);
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders
//                .post("/companies/" + companyKey + "/manufacturingplants")
//                .content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testUpdateManufacturingPlant() throws Exception {
//        ManufacturingPlant manufacturingPlant = setManufacturingPlantDetails();
//        manufacturingPlant.setCountry("India");
//        String companyKey = "LEONISA";
//        String manufacturingPlantKey = "MFG001";
//        String jsonRequest=mapper.writeValueAsString(manufacturingPlant);
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders
//                .put("/companies/" + companyKey + "/manufacturingplants/" + manufacturingPlantKey)
//                .content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testDeleteManufacturingPlantByCompanyKeyAndManufacturingPlantKey() throws Exception {
//        String companyKey = "LEONISA";
//        String manufacturingPlantKey = "MFG001";
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders
//                .delete("/companies/" + companyKey + "/manufacturingplants/" + manufacturingPlantKey)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testDeleteManufacturingPlantByManufacturingPlantKey() throws Exception {
//        String manufacturingPlantKey = "MFG001";
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders
//                .delete("/manufacturingplants/" + manufacturingPlantKey)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    private ManufacturingPlant setManufacturingPlantDetails() {
//        ManufacturingPlant manufacturingPlant = new ManufacturingPlant();
//        manufacturingPlant.setId(setManufacturingPlantKeyDetails());
//        manufacturingPlant.setCompCode("LEONISA");
//        manufacturingPlant.setName("LEONISA");
//        manufacturingPlant.setDescription("desc");
//        manufacturingPlant.setAddress1("Kor");
//        manufacturingPlant.setAddress2("BTM");
//        manufacturingPlant.setCity("ben");
//        manufacturingPlant.setState("kar");
//        manufacturingPlant.setZip("ZIP534");
//        manufacturingPlant.setCountry("ind");
//        manufacturingPlant.setContactName("Nam");
//        manufacturingPlant.setContactNumber("993689");
//
//        return manufacturingPlant;
//    }
//
//    private ManufacturingPlantKey setManufacturingPlantKeyDetails() {
//        ManufacturingPlantKey manufacturingPlantKey = new ManufacturingPlantKey();
//        manufacturingPlantKey.setCode("MFG001");
//
//        return manufacturingPlantKey;
//    }
//}
