package com.handson.microservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
//real world routes aren't gonna be like this tho
@RequestMapping("/client")
public class ClientController {
	@Autowired
	Environment env;
	
	@Autowired
	RestTemplate template;
	
	public final static String CLIENT_SERVICE="clientService";
	
	@GetMapping("/port")
	//circuit breaker can be applied both at class level and method level
	//but if on method lvl, it should match the og controller method signature with JUST one extra parameter
	//of an exception object
	@CircuitBreaker(name=CLIENT_SERVICE, fallbackMethod="callOnFail")
	public String getPort() {
		return template.getForEntity("http://localhost:8082/service/port",String.class).getBody().toString();
	}
	

	public String callOnFail(Exception e) {
		return "oops server isn't responding : (";
	}
}
