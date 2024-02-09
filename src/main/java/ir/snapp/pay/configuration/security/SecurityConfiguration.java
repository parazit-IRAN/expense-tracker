package ir.snapp.pay.configuration.security;


import ir.snapp.pay.configuration.security.jwt.JWTConfigurer;
import ir.snapp.pay.configuration.security.jwt.TokenProvider;
import ir.snapp.pay.constant.Constants;
import ir.snapp.pay.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.List;

@EnableWebSecurity
@AllArgsConstructor
@Import(SecurityProblemSupport.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {

	private final TokenProvider tokenProvider;
	private final SecurityProblemSupport problemSupport;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf()
				.disable()
				.addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling()
				.authenticationEntryPoint(problemSupport)
				.accessDeniedHandler(problemSupport)
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.antMatchers("/swagger-ui/**").permitAll()
				.antMatchers("/test/**").permitAll()
				.antMatchers("/authenticate").permitAll()
				.antMatchers("/users/**").permitAll()
				.antMatchers("/api/admin/**").hasAuthority(Constants.ADMIN)
				.antMatchers("/management/health").permitAll()
				.antMatchers("/management/health/**").permitAll()
				.antMatchers("/management/info").permitAll()
				.antMatchers("/management/prometheus").permitAll()
				.antMatchers("/management/**").hasAuthority(Constants.ADMIN)
				.and()
				.httpBasic()
				.and()
				.apply(securityConfigurerAdapter());
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public GrantedAuthorityDefaults grantedAuthorityDefaults() {
		return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
	}

	private JWTConfigurer securityConfigurerAdapter() {
		return new JWTConfigurer(tokenProvider);
	}

	public CorsFilter corsFilter() {
		return new CorsFilter(SecurityUtils.createCorsConfigurationSource(List.of("*")));
	}

}
