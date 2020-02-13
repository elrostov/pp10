package org.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@ComponentScan("org.example")
//Класс WebSecurityConfigurerAdapter позволяет сконфигурировать безопасность
//нашего приложения
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override //Этот метод конфигурирует цепочку фильтров Spring Security.
    //Порядок фильтров имеет значение
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
//                .anyRequest().permitAll()
                .antMatchers("/login").anonymous()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/user").hasAuthority("USER")
                .antMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
//            .and()
//                .exceptionHandling()
//                .accessDeniedPage("/user")
            .and()
                .formLogin()
                .defaultSuccessUrl("/login/process");

        http.csrf().disable(); //Что делает csrf: Когда пользователем запрашивается форма,
        //сервер отдает её, прикрепляя токен, затем, когда от пользователя возвращается
        //заполненная форма, сервер проверяет, тот ли токен пришел с ётой формой
    }

    @Override //В этом методе мы говорим Спрингу какой UserDetailsService надо
    //использовать, для того, чтобы его единственны методом loadUserByUsername()
    //Спринг мог получить экземпляр UserDetails.
    //Также сообщаем наш PasswordEncoder
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

}
