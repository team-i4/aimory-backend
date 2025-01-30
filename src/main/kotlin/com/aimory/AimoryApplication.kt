package com.aimory

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class AimoryApplication

fun main(args: Array<String>) {
    runApplication<AimoryApplication>(*args)
}
