package iiot.chapter8.ciruitbreaker;

import com.ge.digital.demo.dto.AssetDto;
import com.ge.digital.demo.dto.Resources;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.parsing.Parser;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.util.Arrays;

import static com.jayway.restassured.RestAssured.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.springframework.http.HttpStatus.OK;


/**
 * Test for
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ApplicationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationTest.class);

    @Value("${local.server.port}")
    int port;

    @Value("${deployed.app.url.base:}")
    String baseUrl;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GatewayController gatewayController;

    @Before
    public void setup() {
        RestAssured.port = port;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        RestAssured.defaultParser = Parser.JSON;
        LOGGER.info("=============>Base Url is: [[[{}]]]", baseUrl);
    }

    @Test
    public void getCustomerAsset() {
        Field field = ReflectionUtils.findField(GatewayController.class, "restTemplate");
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, gatewayController, restTemplate());

        // create resource
        given().contentType(ContentType.JSON)
                .when().get(baseUrl + "/api/v1/customer-assets")
                    .then().statusCode(HttpStatus.SC_OK);

    }

    public RestTemplate restTemplate() {
        LOGGER.info("=============>Setting mock rest template");
        AssetDto[] customerAssets = {new AssetDto(1L, "testName1","testDesc1", "testOwner1", "testOperator1" ),
                                     new AssetDto(2L, "testName2","testDesc2", "testOwner2", "testOperator2" )};
        Resources resources = new Resources(Arrays.asList(customerAssets), new Link("test"));
        ResponseEntity<Resources> responseEntity = new ResponseEntity<>(resources, OK);
        RestTemplate mock = Mockito.mock(RestTemplate.class);
        Mockito.when(mock.getForEntity(anyString(), eq(Resources.class))).thenReturn(responseEntity);
        return mock;
    }

}