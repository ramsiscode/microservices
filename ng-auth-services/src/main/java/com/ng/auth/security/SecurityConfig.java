package com.ng.auth.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.codahale.passpol.BreachDatabase;
import com.codahale.passpol.PasswordPolicy;
import com.ng.auth.filter.JwtFilter;
import com.ng.auth.service.UserService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return authentication -> {
			throw new AuthenticationServiceException("Cannot authenticate " + authentication);
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new Argon2PasswordEncoder(16, 32, 8, 1 << 16, 4);
	}

	@Bean
	public PasswordPolicy passwordPolicy() {
		return new PasswordPolicy(BreachDatabase.top100K(), 8, 256);
	}

	@Autowired
	private UserService myUserDetailsService;
	@Autowired
	private JwtFilter filter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.userDetailsService(myUserDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf(customizer -> customizer.disable()).authorizeRequests(customizer -> {
			try {
				customizer
						//.antMatchers("/authenticate", "/signin", "/verify-totp", "/verify-totp-additional-security",
						//		"/signup", "/signup-confirm-secret", "/getcaptcha")
						.antMatchers("/auth/*")
						//.permitAll().antMatchers(HttpMethod.GET, "/", "/v2/api-docs", // swagger
						//		"/webjars/**", // swagger-ui webjars
						//		"/swagger-resources/**", // swagger-ui resources
						//		"/configuration/**", // swagger configuration
						//		"/swagger-ui/", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js")
						.permitAll().
						anyRequest().authenticated().and().sessionManagement()
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
			;
			try {	 
				http.cors().and();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}).logout(customizer -> customizer.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()));
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/", "/assets/**", "/svg*", ".br", "/*.gz", "/*.html", "/*.js", "/*.css", "/*.woff2",
				"/*.ttf", "/*.eot", "/*.svg", "/*.woff", "/*.ico");

	}
	

  @Bean
  CorsConfigurationSource corsConfigurationSource() {

      final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      source.registerCorsConfiguration("/**", config.applyPermitDefaultValues());
      config.setExposedHeaders(Arrays.asList("Authorization"));
      config.setAllowedMethods(Arrays.asList("HEAD",
              "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

      return source;
  }


	
}
