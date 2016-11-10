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
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.post;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("integration-test")
public class KeyCounterIT {

  @Value("${local.server.port}")
  int port;

  private static final String COUNTER_PATH = "/api/v1/counter/key";
  private static final String SWAGGER_PATH = "/v2/api-docs";
  private static final String HEALTH_CHECK_PATH = "/api/v1/healthz";

  @Before
  public void setUp() {
    RestAssured.port = port;
  }

  @Test
  public void counter_shouldReturnKeyValue_whenProperCredentialsProvided() {
    final String username = "test_user";
    final String password = "test_pass";
    // @formatter:off
    given().
            auth().basic(username, password).
    when().
            post(COUNTER_PATH).
    then().
            statusCode(HttpStatus.OK.value()).
            body(equalTo(ITConfig.keyValue()));
    // @formatter:on
  }

  @Test
  public void counter_shouldReturnUnauthorizedStatus_whenNoAuthGiven() {
    post(COUNTER_PATH).then().statusCode(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  public void counter_shouldReturnUnauthorizedStatus_whenBadCredentialsGiven() {
    // @formatter:off
    given().
            auth().basic("bad_username", "bad_password").
    when().
            post(COUNTER_PATH).
    then().
            statusCode(HttpStatus.UNAUTHORIZED.value());
    // @formatter:on
  }

  @Test
  public void keyCounter_shouldExposeInsecureSwaggerEndpoint() {
    get(SWAGGER_PATH).then().
            statusCode(HttpStatus.OK.value()).
            contentType(ContentType.JSON).
            body("info.title", containsString("Key Counter"));
  }

  @Test
  public void healthCheckEndpoint_shouldBeInsecureAndReturn200_whenAllOk() {
    get(HEALTH_CHECK_PATH).then().
            statusCode(HttpStatus.OK.value());
  }

  @Test
  public void healthCheckEndpoint_shouldReturn500_whenRedisDoesNotWork() {
    ITConfig.throwErrorWhenTalkingToRedis();
    get(HEALTH_CHECK_PATH).then().
            statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

}
