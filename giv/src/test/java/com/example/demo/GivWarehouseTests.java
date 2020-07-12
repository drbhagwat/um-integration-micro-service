//package com.example.demo;
//
//import api.GivApplication;
//import api.core.entities.Warehouse;
//import api.core.entities.WarehouseKey;
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
//public class GivWarehouseTests {
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
//    public void testGetAllWarehousesByDivisionKey() throws Exception {
//        String divisionKey = "LEONISA";
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders
//                .get("/divisions/" + divisionKey + "/warehouses")
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testGetWarehouseByDivisionKeyAndWarehouseKey() throws Exception {
//        String divisionKey = "LEONISA";
//        String warehouseKey = "CSB";
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders
//                .get("/divisions/" + divisionKey + "/warehouses/" + warehouseKey)
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testGetAllWarehouses() throws Exception {
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/warehouses")
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testGetWarehouseKeyByWarehouseKey() throws Exception {
//        String warehouseKey = "CSB";
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders
//                .get("/warehouses/" + warehouseKey)
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testAddWarehouse() throws Exception {
//        Warehouse warehouse = setWarehouseDetails();
//        String divisionKey = "LEONISA";
//        String jsonRequest=mapper.writeValueAsString(warehouse);
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders
//                .post("/divisions/" + divisionKey + "/warehouses")
//                .content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testUpdateWarehouse() throws Exception {
//        Warehouse warehouse = setWarehouseDetails();
//        warehouse.setCountry("India");
//        String divisionKey = "LEONISA";
//        String warehouseKey = "CSB";
//        String jsonRequest=mapper.writeValueAsString(warehouse);
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders
//                .put("/divisions/" + divisionKey + "/warehouses/" + warehouseKey)
//                .content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testDeleteWarehouseByDivisionKeyAndWarehouseKey() throws Exception {
//        String divisionKey = "LEONISA";
//        String warehouseKey = "CSB";
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders
//                .delete("/divisions/" + divisionKey + "/warehouses/" + warehouseKey)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testDeleteManufacturingPlantByWarehouseKey() throws Exception {
//        String warehouseKey = "CSB";
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders.delete("/warehouses/" + warehouseKey)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    private Warehouse setWarehouseDetails() {
//        Warehouse warehouse = new Warehouse();
//        warehouse.setCompanyCode("LEONISA");
//        warehouse.setDivCode("LEONISA");
//        warehouse.setId(setWarehouseKeyDetails());
//        warehouse.setName("Warehouse1");
//        warehouse.setDescription("Warehouse Description");
//        warehouse.setAddress1("Warehouse Address");
//        warehouse.setCity("Warehouse city");
//        warehouse.setState("Warehouse state");
//        warehouse.setZip("w12345");
//        warehouse.setCountry("Warehouse Country");
//        warehouse.setContactName("Warehouse Contact");
//        warehouse.setContactNumber("9876543210");
//
//        return warehouse;
//    }
//
//    private WarehouseKey setWarehouseKeyDetails() {
//        WarehouseKey warehouseKey = new WarehouseKey();
//        warehouseKey.setCode("CSB");
//
//        return warehouseKey;
//    }
//}
