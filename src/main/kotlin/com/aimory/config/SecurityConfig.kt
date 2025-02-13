package com.aimory.config

import com.aimory.enums.Role
import com.aimory.security.EntryPointUnauthorizedHandler
import com.aimory.security.Jwt
import com.aimory.security.JwtAccessDeniedHandler
import com.aimory.security.JwtAuthenticationProvider
import com.aimory.security.JwtAuthenticationTokenFilter
import com.aimory.service.MemberService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwt: Jwt,
    private val jwtTokenConfig: JwtTokenConfig,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val entryPointUnauthorizedHandler: EntryPointUnauthorizedHandler,
) {

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun jwtAuthenticationProvider(jwt: Jwt, memberService: MemberService): JwtAuthenticationProvider {
        return JwtAuthenticationProvider(jwt, memberService)
    }

    @Bean
    fun jwtAuthenticationTokenFilter(): JwtAuthenticationTokenFilter {
        return JwtAuthenticationTokenFilter(jwtTokenConfig.header, jwt)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            formLogin { disable() }
            headers { disable() }
            authorizeHttpRequests {
                authorize("/health-check", permitAll)
                authorize("/signup", permitAll)
                authorize("/login", permitAll)
                authorize("/swagger-ui/**", permitAll)
                authorize("/swagger-resources/**", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                authorize("/h2-console/**", permitAll)
                authorize(HttpMethod.POST, "/notices/**", hasRole(Role.TEACHER.name))
                authorize(HttpMethod.PUT, "/notices/**", hasRole(Role.TEACHER.name))
                authorize(HttpMethod.DELETE, "/notices/**", hasRole(Role.TEACHER.name))
                authorize(HttpMethod.POST, "/notes/**", hasRole(Role.TEACHER.name))
                authorize(HttpMethod.PUT, "/notes/**", hasRole(Role.TEACHER.name))
                authorize(HttpMethod.DELETE, "/notes/**", hasRole(Role.TEACHER.name))
                authorize("/photos", hasRole(Role.TEACHER.name))
                authorize("/photos/pending", hasRole(Role.TEACHER.name))
                authorize("/photos/assign", hasRole(Role.TEACHER.name))
                authorize(HttpMethod.DELETE, "/photos/child", hasRole(Role.TEACHER.name))
                authorize("/teacher/**", hasRole(Role.TEACHER.name))
                authorize("**", authenticated)
                // authorize("**", permitAll)
            }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            exceptionHandling {
                accessDeniedHandler = jwtAccessDeniedHandler
                authenticationEntryPoint = entryPointUnauthorizedHandler
            }
        }
        http.addFilterBefore(
            jwtAuthenticationTokenFilter(),
            UsernamePasswordAuthenticationFilter::class.java
        )
        return http.build()
    }
}
