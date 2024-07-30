package com.ablez.jookbiren.log.config;

import com.ablez.jookbiren.log.LogbackConsole;
import com.ablez.jookbiren.log.LogbackRolling;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogbackConfig {
    @Bean
    public LogbackConsole logbackConsole() {
        LogbackConsole logbackConsole = new LogbackConsole();
        logbackConsole.logConfig();
        return logbackConsole;
    }

    @Bean
    public LogbackRolling logbackRolling() {
        LogbackRolling logbackRolling = new LogbackRolling();
        logbackRolling.logConfig();
        return logbackRolling;
    }
}
