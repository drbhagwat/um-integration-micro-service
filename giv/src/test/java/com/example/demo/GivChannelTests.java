//package com.example.demo;
//
//import api.GivApplication;
//import api.core.entities.Channel;
//import api.core.entities.ChannelKey;
//import api.core.entities.Company;
//import api.core.entities.CompanyKey;
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
//public class GivChannelTests {
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
//    public void testGetAllChannels() throws Exception {
//        String companyKey = "LEONISA";
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/companies/" + companyKey + "/channels")
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testGetChannel() throws Exception {
//        String companyKey = "LEONISA";
//        String channelKey = "WEB";
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/companies/" + companyKey + "/channels/" +channelKey)
//                .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testAddChannel() throws Exception {
//        Channel channel = setChannelDetails();
//        String companyKey = "LEONISA";
//        String jsonRequest=mapper.writeValueAsString(channel);
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders.post("/companies/" + companyKey +"/channels")
//                .content(jsonRequest).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testUpdateChannel() throws Exception {
//        Channel channel = setChannelDetails();
//        channel.setName("CATALOG");
//        String companyKey = "LEONISA";
//        String channelKey = "WEB";
//        String jsonRequest=mapper.writeValueAsString(channel);
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders
//                .put("/companies/" + companyKey + "/channels/" + channelKey)
//                .content(jsonRequest).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testDeleteChannel() throws Exception {
//        String companyKey = "LEONISA";
//        String channelKey = "CATALOG";
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders
//                .delete("/companies/" + companyKey + "/channels/" + channelKey)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testGetAllUsersInAChannel() throws Exception {
//        String channelKey = "CATALOG";
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/channels/" + channelKey + "/users")
//                    .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testAssociateUserToAChannel() throws Exception {
//        String channelKey = "CATALOG";
//        int userId = 1;
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders
//                .post("/channels/" + channelKey+ "/users/" + userId)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testDisAssociateUserFromAChannel() throws Exception {
//        int userId = 1;
//        String channelKey = "CATALOG";
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders
//                .delete("/channels/" + channelKey + "/users/" + userId)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    private Company setCompanyDetails() {
//        Company company = new Company();
//        CompanyKey companyKey = setCompanyKeyDetails();
//        company.setId(companyKey);
//        company.setName("Company1");
//        company.setDescription("Company Description");
//        company.setAddress1("Elita Promenade");
//        company.setCity("Bangalore");
//        company.setState("Karnataka");
//        company.setZip("560078");
//        company.setCountry("India");
//        company.setContactNumber("9731033370");
//        company.setContactName("Dinesh Bhagwat");
//
//        return company;
//    }
//
//    private CompanyKey setCompanyKeyDetails() {
//        CompanyKey companyKey = new CompanyKey();
//        companyKey.setCode("LEONISA");
//
//        return companyKey;
//    }
//
//    private Channel setChannelDetails() {
//        Channel channel = new Channel();
//        ChannelKey channelKey = setChannelKeyDetails();
//        channel.setId(channelKey);
//        channel.setName("CATALOG");
//        channel.setCompany(setCompanyDetails());
//
//        return channel;
//    }
//
//    private ChannelKey setChannelKeyDetails() {
//        ChannelKey channelKey = new ChannelKey();
//        channelKey.setCode("CATALOG");
//        return channelKey;
//    }
//}
