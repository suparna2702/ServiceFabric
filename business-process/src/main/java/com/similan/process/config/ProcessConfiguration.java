package com.similan.process.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.similan.process.ProcessPackage;


@Configuration
@ComponentScan(basePackageClasses = ProcessPackage.class)
public class ProcessConfiguration {
}
