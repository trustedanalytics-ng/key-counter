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
package org.trustedanalytics.keycounter.rest;

import org.trustedanalytics.keycounter.redisservice.RedisCounterService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

  private static final String HEALTH_CHECK_KEY = "  ҉";

  @Autowired
  RedisCounterService redisCounterService;

  @ApiOperation("Checks service health.")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "key-counter is healthy"),
          @ApiResponse(code = 500, message = "key-counter is indisposed"),
  })
  @RequestMapping(value = "/healthz", method = RequestMethod.GET)
  public void checkHealth() {
    redisCounterService.incrementAndGetValue(HEALTH_CHECK_KEY);
  }

}
