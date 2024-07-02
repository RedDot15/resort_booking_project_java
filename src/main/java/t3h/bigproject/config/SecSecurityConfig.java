package t3h.bigproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecSecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http builder configurations for authorize requests and form login
        // (see below)
        http
                .csrf().disable()
                .authorizeRequests((authz) -> authz
                        .antMatchers("/backend/**").permitAll()
                        .antMatchers("/booking/**").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling().accessDeniedPage("/403")
                .and()
                .formLogin( (form) -> {
                    form.loginPage("/login")
                            .usernameParameter("email")
                            .passwordParameter("password")
                            .loginProcessingUrl("/doLogin")
                            .defaultSuccessUrl("/backend/user/")
                            .failureUrl("/login?error=true");
                    form.loginPage("/home")
                            .usernameParameter("email")
                            .passwordParameter("password")
                            .loginProcessingUrl("/doLogin")
                            .defaultSuccessUrl("/home")
                            .failureUrl("/login?error=true");
                })

//                .failureHandler(authenticationFailureHandler())
                .logout((logout) -> {
                    logout.logoutUrl("/logout")
                            .deleteCookies("JSESSIONID")
                            .logoutSuccessUrl("/home");
                })

//                .logoutSuccessHandler(logoutSuccessHandler())
        ;
        return http.build();
    }
}
