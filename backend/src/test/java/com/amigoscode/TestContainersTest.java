package com.amigoscode;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class TestContainersTest extends AbstractTestcontainers {

    @Test
    void canStartPostgresSql() {
       assertThat(postgreSQLContainer.isRunning()).isTrue();
       assertThat(postgreSQLContainer.isCreated()).isTrue();

    }


}
