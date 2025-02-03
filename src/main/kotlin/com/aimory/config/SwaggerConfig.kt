package com.aimory.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter
import org.springframework.lang.Nullable
import java.lang.reflect.Type

@Configuration
@SecurityScheme(name = "Authorization", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(configurationInfo())
    }

    private fun configurationInfo(): Info {
        return Info()
            .title("아이모리 API 명세서")
            .description("아이포 팀 프로젝트✌️")
            .version("1.0.0")
    }

    /**
     * 'application/octet-stream' 요청 변환기
     */
    @Bean
    fun multipartJackson2HttpMessageConverter(objectMapper: ObjectMapper): MultipartJackson2HttpMessageConverter {
        return MultipartJackson2HttpMessageConverter(objectMapper)
    }

    class MultipartJackson2HttpMessageConverter(
        objectMapper: ObjectMapper,
    ) : AbstractJackson2HttpMessageConverter(objectMapper, MediaType.APPLICATION_OCTET_STREAM) {
        override fun canWrite(
            clazz: Class<*>,
            mediaType: MediaType?,
        ): Boolean = false

        override fun canWrite(
            @Nullable type: Type?,
            clazz: Class<*>,
            @Nullable mediaType: MediaType?,
        ): Boolean = false

        override fun canWrite(
            mediaType: MediaType?,
        ): Boolean = false
    }
}
