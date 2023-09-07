package com.increff.pos.config;

import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import com.increff.pos.spring.SpringConfig;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(//
		basePackages = { "com.increff.pos" }, //
		excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, value = { SpringConfig.class })//
)
@PropertySources({ //
		@PropertySource(value = "classpath:./com/increff/pos/test.properties", ignoreResourceNotFound = true) //
})
public class QaConfig {
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
