package com.datadigger.datainsight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import com.geccocrawler.gecco.GeccoEngine;

@SpringBootApplication
@ServletComponentScan
public class DatainsightApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatainsightApplication.class, args);
	}
}
