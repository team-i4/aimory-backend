package com.aimory

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AimoryApplication

fun main(args: Array<String>) {
    runApplication<AimoryApplication>(*args)
}
