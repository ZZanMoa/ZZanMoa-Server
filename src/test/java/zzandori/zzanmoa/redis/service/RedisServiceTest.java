package zzandori.zzanmoa.redis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class RedisServiceTest {

    final String KEY = "key";
    final String VALUE = "value";

    @Autowired
    private RedisService redisService;

    @AfterEach
    void tearDown() {
        redisService.deleteValues(KEY);
    }

    @Test
    @DisplayName("Redis에 데이터를 저장하면 정상적으로 조회된다.")
    void saveAndFind() {
        // given
        redisService.setValues(KEY, VALUE);

        // when
        String findValue = redisService.getValues(KEY);

        // then
        assertThat(findValue).isEqualTo(VALUE);
    }

    @Test
    @DisplayName("Redis에 저장된 데이터를 수정할 수 있다.")
    void update() {
        // given
        redisService.setValues(KEY, VALUE);

        String updateValue = "updateValue";
        redisService.setValues(KEY, updateValue);

        // when
        String findValue = redisService.getValues(KEY);

        // then
        assertThat(updateValue).isEqualTo(findValue);
        assertThat(VALUE).isNotEqualTo(findValue);
    }

    @Test
    @DisplayName("Redis에 저장된 데이터를 삭제할 수 있다.")
    void delete() {
        // given
        redisService.setValues(KEY, VALUE);

        // when
        redisService.deleteValues(KEY);
        String findValue = redisService.getValues(KEY);

        // then
        assertThat(findValue).isEqualTo("false");
    }

    @Test
    @DisplayName("Redis에 만료시간과 함께 데이터를 저장할 수 있고 만료시간이 지나면 삭제된다.")
    void saveWithDurationAndExpired() {
        // given
        Duration DURATION = Duration.ofMillis(5000);

        // when
        redisService.setValues(KEY, VALUE, DURATION);
        String findValue = redisService.getValues(KEY);

        // then
        await().pollDelay(Duration.ofMillis(6000)).untilAsserted(
            () -> {
                String expiredValue = redisService.getValues(KEY);
                assertThat(expiredValue).isNotEqualTo(findValue);
                assertThat(expiredValue).isEqualTo("false");
            }
        );
    }

    @DisplayName("Redis에 저장된 데이터에 만료시간을 설정할 수 있고 만료시간이 지나면 삭제된다.")
    @Test
    void setDurationAfterSaveAndExpired() {
        // given
        redisService.setValues(KEY, VALUE);

        // when
        int timeout = 5000;
        redisService.expireValues(KEY, timeout);

        String findValue = redisService.getValues(KEY);

        // then
        await().pollDelay(Duration.ofMillis(6000)).untilAsserted(
            () -> {
                String expiredValue = redisService.getValues(KEY);
                assertThat(expiredValue).isNotEqualTo(findValue);
                assertThat(expiredValue).isEqualTo("false");
            }
        );
    }

}