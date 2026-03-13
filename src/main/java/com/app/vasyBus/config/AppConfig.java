package com.app.vasyBus.config;

import com.app.vasyBus.model.User;
import com.app.vasyBus.enums.Role;
import com.app.vasyBus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public FilterRegistrationBean<HiddenHttpMethodFilter>
    disableHiddenMethodFilter() {
        FilterRegistrationBean<HiddenHttpMethodFilter> filter =
                new FilterRegistrationBean<>(new HiddenHttpMethodFilter());
        filter.setEnabled(false);
        return filter;
    }

}