package org.trustedanalytics.keycounter;

import static com.jayway.restassured.RestAssured.get;

import com.jayway.restassured.RestAssured;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@ActiveProfiles("integration-test")
public class SwaggerGenerator {

  @Value("${local.server.port}")
  int port;

  @Before
  public void setUp() {
    RestAssured.port = port;
  }

  @Test
  public void generateSwaggerJson() throws IOException {
    String response = retrieveSwaggerJson();
    savePrettyResponseToFile(response, "swagger.json");
  }

  private void savePrettyResponseToFile(String response, String fileName) throws IOException {
    String currentDir = System.getProperty("user.dir");
    ObjectMapper mapper = new ObjectMapper();
    ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
    JsonNode jsonNode = mapper.readTree(response);
    Files.write(
            Paths.get(currentDir).resolve(fileName),
            writer.withDefaultPrettyPrinter().writeValueAsString(jsonNode).getBytes(),
            StandardOpenOption.CREATE, StandardOpenOption.WRITE);
  }

  private String retrieveSwaggerJson() {
    String response = get(KeyCounterIT.SWAGGER_PATH).then().extract().asString();
    return removeRandomPortNumber(response);
  }

  private String removeRandomPortNumber(String response) {
    return response.replaceAll("localhost:" + port, "localhost");
  }

}
