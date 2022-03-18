package elixter.blog;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/hashtags/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts").permitAll()
                .anyRequest().authenticated();
    }
}