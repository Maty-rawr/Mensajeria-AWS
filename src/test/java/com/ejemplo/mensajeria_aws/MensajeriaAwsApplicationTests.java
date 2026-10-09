package com.ejemplo.mensajeria_aws;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.cloud.aws.sqs.listener.auto-startup=false")
class MensajeriaAwsApplicationTests {

	@Test
	void contextLoads() {
	}

}
