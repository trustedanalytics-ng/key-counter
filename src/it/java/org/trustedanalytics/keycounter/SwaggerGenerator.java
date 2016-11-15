/**
 * Copyright (c) 2016 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
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
