package com.tabisketch.configuration;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(a -> a
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/", "/top").permitAll()
                        .requestMatchers("/register/**").permitAll()
                        .requestMatchers("/mail/**").permitAll()
                        .requestMatchers("/password-reset/**").permitAll()
                        .requestMatchers("/share/**").permitAll()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/plan/**").authenticated()
                        .anyRequest().denyAll()
                ).formLogin(a -> a
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        // TODO: ログイン成功後に遷移するページURLに書き換える
                        .defaultSuccessUrl("/top")
                        .failureUrl("/login?error")
                        // NOTE: メールアドレスを "username" として扱う
                        .usernameParameter("mail")
                        .permitAll()
                ).logout(a -> a
                        .logoutSuccessUrl("/login")
                        .permitAll()
                ).build();
    }
}