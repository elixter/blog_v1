package elixter.blog;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .anyRequest().permitAll();
//                .and()
//                .logout()
//                .logoutUrl("/api/auth/signout")
//                .logoutSuccessUrl("http://www.naver.com")
//                .deleteCookies("JSESSIONID").clearAuthentication(true)
//                .invalidateHttpSession(true).permitAll();
//                .antMatchers("/").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/users/*").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/users").permitAll()
//                .antMatchers(HttpMethod.PUT, "/api/users").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/hashtags/*").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/posts/*").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/posts").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/posts").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/auth/*").permitAll()
//                .anyRequest().authenticated()
//                .and().logout()
//                .logoutUrl("/api/auth/logout")
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID")
//                .clearAuthentication(true)
//                .permitAll()
//                .and()
//                .sessionManagement()
//                .maximumSessions(1)
//                .maxSessionsPreventsLogin(true);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}