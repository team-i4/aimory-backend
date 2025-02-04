package com.aimory.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.rekognition.RekognitionClient

@Configuration
class RekognitionConfig(
    @Value("\${cloud.aws.credentials.access-key}") val accessKey: String,
    @Value("\${cloud.aws.credentials.secret-key}") val secretKey: String,
    @Value("\${cloud.aws.region.static}") val region: String,
) {

    @Bean
    fun rekognitionClient(): RekognitionClient {
        val credentialsProvider = StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)
        )
        return RekognitionClient.builder()
            .region(Region.of(region))
            .credentialsProvider(credentialsProvider)
            .build()
    }
}
