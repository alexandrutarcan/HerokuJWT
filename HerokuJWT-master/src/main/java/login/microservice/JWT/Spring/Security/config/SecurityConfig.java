package login.microservice.JWT.Spring.Security.config;

import login.microservice.JWT.Spring.Security.config.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/permission/admin").hasRole("ADMIN")
                .antMatchers("/permission/student").hasAnyRole("ADMIN","STUDENT","PROFESSOR")
                .antMatchers("/permission/professor").hasAnyRole("ADMIN", "PROFESSOR")
               // .antMatchers("/modify/role").hasRole("ADMIN")
                .antMatchers("/user/update").hasRole("ADMIN")
                .antMatchers("/user/{id}").hasRole("ADMIN")
                .antMatchers("/user/delete/{id}").hasRole("ADMIN")
                .antMatchers("/user/all").hasRole("ADMIN")
                .antMatchers("/auth/register", "/auth/authenticate", "/auth").permitAll()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
