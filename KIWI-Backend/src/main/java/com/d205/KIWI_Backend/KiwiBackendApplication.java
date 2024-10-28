package com.d205.KIWI_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 인증번호 저장 및 만료를 위해(mysql사용)
@EnableJpaAuditing
@EnableCaching
@EnableAsync
public class KiwiBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(KiwiBackendApplication.class, args);
	}

}
