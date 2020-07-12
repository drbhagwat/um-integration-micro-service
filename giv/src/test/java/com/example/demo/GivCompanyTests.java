//package com.example.demo;
//
//import api.GivApplication;
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
//import org.springframework.web.context.WebApplicationContext;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=GivApplication.class)
//@AutoConfigureMockMvc
//@TestPropertySource(value = "classpath:application.yml")
//public class GivCompanyTests {
//	@LocalServerPort
//	private int port;
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@Autowired
//	private WebApplicationContext context;
//
//	@Autowired
//	private ObjectMapper mapper;
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testGetAllCompanies() throws Exception {
//		MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/companies")
//		.accept(MediaType.APPLICATION_JSON)).andReturn();
//
//		System.out.println(result.getResponse().getContentAsString());
//	}
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testAddCompany() throws Exception {
//        Company company = setCompanyDetails();
//        String jsonRequest=mapper.writeValueAsString(company);
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders.post("/companies").content(jsonRequest)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testGetCompany() throws Exception {
//        CompanyKey companyKey = new CompanyKey("LEONISA");
//        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/companies/" + companyKey)
//                    .accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testUpdateCompany() throws Exception {
//        Company company = setCompanyDetails();
//        company.setDescription("Company Description");
//        String companyKey = "LEONISA";
//        String jsonRequest=mapper.writeValueAsString(company);
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders.put("/companies/" + companyKey)
//                .content(jsonRequest).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    @WithMockUser(username="givadmin",roles={"USER","ADMIN"})
//    @Test
//    public void testDeleteCompany() throws Exception {
//        String companyKey = "LEONISA";
//        MvcResult result=mockMvc.perform( MockMvcRequestBuilders.delete("/companies/" + companyKey)
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
//}
//
