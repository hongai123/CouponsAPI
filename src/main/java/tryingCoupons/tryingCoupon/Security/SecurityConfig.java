package tryingCoupons.tryingCoupon.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tryingCoupons.tryingCoupon.beans.Roles;

import java.util.Arrays;
import java.util.Collections;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDetailsService usd;
    private final JwtRequestFilter jwtRequestFilter = new JwtRequestFilter();

    /**
     * Configure the authentication manager
     * @param auth - Accept AuthenticationManagerBuilder object which belongs to spring security.
     * @throws Exception - catch any exception that can accrued.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usd).passwordEncoder(passwordEncoder);
    }

    /**
     * Configure spring security on the http
     * @param http - accept httpSecurity object which set the spring security on our web
     * @throws Exception - catch any exception that can accrued.
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/","index","/css","/js","media","img","/login/**")
                .permitAll()
                .antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/admin/**").hasRole(Roles.ADMIN.name())
                .antMatchers("/company/**").hasRole(Roles.COMPANY.name())
                .antMatchers("/customer/**").hasRole(Roles.CUSTOMER.name()).and()
                .authorizeRequests()
                .antMatchers("/token/**").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/guest/**").permitAll()
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .defaultSuccessUrl("http://localhost:8080/swagger-ui.html#/")
                .and()
                .logout()
                .logoutSuccessUrl("http://localhost:8080/token/lognout");
                //.logoutSuccessUrl("http://localhost:8080/login");


    }

    /**
     * Configure the paths the spring security will ignore.
     * @param web - accept WebSecurity object.
     * @throws Exception - catch any exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**")
                .antMatchers("/token/**")
                .antMatchers("/guest/**")
//                .and()

//                .antMatchers("/admin/**")
//                .antMatchers("/company/**")
//                .antMatchers("/customer/**")

        ;

    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.addAllowedMethod(HttpMethod.TRACE);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "OPTIONS"));
        configuration.addExposedHeader("Authorization");
        configuration.addAllowedHeader("*");
        //allow to get credentials in cors
        configuration.addAllowedOriginPattern("*");
        //allow to get from any ip/domain
        configuration.addAllowedOrigin("*");
        //allow to get any header
        configuration.addAllowedHeader("*");
        //tell which VERB is allowed
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader("Authorization");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }





}


