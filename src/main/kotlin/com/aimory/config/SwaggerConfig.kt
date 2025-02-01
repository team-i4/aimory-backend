package com.aimory.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(configurationInfo())
            .components(
                Components()
                    .addSecuritySchemes("api_key",
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
}
