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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.ValueOperations;

@Configuration
@Profile("integration-test")
public class ITConfig {

  private static final long KEY_VALUE = 13l;
  private static final ValueOperations<String, Integer> redisMock = mock(ValueOperations.class);

  @Bean
  public ValueOperations<String, Integer> valueOperations() {
    Mockito.reset(redisMock);
    when(redisMock.increment(any(String.class), anyLong())).thenReturn(KEY_VALUE);
    return redisMock;
  }

  public static String keyValue() {
    return String.valueOf(KEY_VALUE);
  }

  public static void throwErrorWhenTalkingToRedis() {
    when(redisMock.increment(any(String.class), anyLong())).then(
            invocationOnMock -> {
              throw new Error();
            });
  }

}
