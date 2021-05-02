package com.sample.reviewservice;

import com.sample.reviewservice.repository.search.ReviewSearchRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableElasticsearchRepositories(basePackageClasses = ReviewSearchRepository.class)
@EnableJpaRepositories(excludeFilters=@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ReviewSearchRepository.class))
public class ReviewserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewserviceApplication.class, args);
	}

}
