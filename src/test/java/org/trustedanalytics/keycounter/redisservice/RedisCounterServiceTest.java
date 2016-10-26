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
package org.trustedanalytics.keycounter.redisservice;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.redis.core.ValueOperations;

@RunWith(MockitoJUnitRunner.class)
public class RedisCounterServiceTest {

  @Mock
  ValueOperations<String, Integer> valueOperations;

  @InjectMocks
  RedisCounterService redisCounterService;

  @Test
  public void shouldIncrementByOneAndReturnValue() throws Exception {
    // given
    final String key = "key";
    final long value = 13;
    when(valueOperations.increment(key, 1)).thenReturn(value);
    // when
    int returnedValue = redisCounterService.incrementAndGetValue(key);
    // then
    assertEquals(value, returnedValue);
  }

}