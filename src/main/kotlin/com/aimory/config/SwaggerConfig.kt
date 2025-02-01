package com.aimory.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter
import org.springframework.lang.Nullable
import java.lang.reflect.Type

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(configurationInfo())
            .components(
                Components()
                    .addSecuritySchemes(
                        "api_key",
                        SecurityScheme()
                            .type(SecurityScheme.Type.APIKEY)
                            .`in`(SecurityScheme.In.HEADER)
                            .name("api_key")
                    )
            )
            .addSecurityItem(SecurityRequirement().addList("api_key"))
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
