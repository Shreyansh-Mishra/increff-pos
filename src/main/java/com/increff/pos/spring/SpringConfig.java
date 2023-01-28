package com.increff.pos.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ComponentScan("com.increff.pos")
@PropertySources({ //
		@PropertySource(value = "file:C:\\Users\\Shreyansh\\Desktop\\increff\\employee-spring-full\\employee.properties", ignoreResourceNotFound = true) //
})
public class SpringConfig {


}
