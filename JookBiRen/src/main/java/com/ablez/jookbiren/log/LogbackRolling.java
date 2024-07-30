package com.ablez.jookbiren.log;

import static ch.qos.logback.classic.Level.DEBUG;
import static ch.qos.logback.classic.Level.ERROR;
import static ch.qos.logback.classic.Level.OFF;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import org.slf4j.LoggerFactory;

public class LogbackRolling {
    private final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

    private final String ROLLING_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} %logger{5} - %msg %n";
    private final String FILE_NAME = "~/logs/application.log";
    private final String LOG_NAME_PATTERN = "./logs/application-%d{yyyy-MM-dd-HH-mm}.%i.log";
    private final String MAX_FILE_SIZE = "10MB";
    private final String TOTAL_SIZE = "30MB";
    private final int MAX_HISTORY = 3;

    private RollingFileAppender<ILoggingEvent> rollingAppender;

    public void logConfig() {
        rollingAppender = getLogAppender();
        createLoggers();
    }

    private void createLoggers() {
        createLogger("jdbc", OFF, false);
        createLogger("jdbc.sqlonly", DEBUG, false);
        createLogger("jdbc.sqltiming", OFF, false);
        createLogger("org.hibernate.SQL", DEBUG, false);
        createLogger("com.ablez.jookbiren.advice", ERROR, false);
        createLogger("com.ablez.jookbiren.answer.service", ERROR, false);
        createLogger("com.ablez.jookbiren.buyer.service", ERROR, false);
        createLogger("com.ablez.jookbiren.order.service", ERROR, false);
        createLogger("com.ablez.jookbiren.quiz.service", ERROR, false);
        createLogger("com.ablez.jookbiren.user.service", ERROR, false);
        createLogger("com.ablez.jookbiren.security.filter", ERROR, false);
        createLogger("com.ablez.jookbiren.security.handler", ERROR, false);
        createLogger("com.ablez.jookbiren.security.interceptor", ERROR, false);
        createLogger("com.ablez.jookbiren.security.jwt", ERROR, false);
        createLogger("com.ablez.jookbiren.security.userdetails", ERROR, false);
    }

    private void createLogger(String loggerName, Level logLevel, Boolean additive) {
        Logger logger = loggerContext.getLogger(loggerName);
        logger.setAdditive(additive);
        logger.setLevel(logLevel);
        logger.addAppender(rollingAppender);
    }

    private RollingFileAppender<ILoggingEvent> getLogAppender() {
        final String appendName = "ROLLING_LOG_FILE";
        PatternLayoutEncoder rollingLogEncoder = createLogEncoder(ROLLING_PATTERN);
        RollingFileAppender<ILoggingEvent> rollingFileAppender = createLogAppender(appendName, rollingLogEncoder);
        SizeAndTimeBasedRollingPolicy rollingPolicy = createLogRollingPolicy(rollingFileAppender);

        rollingFileAppender.setRollingPolicy(rollingPolicy);
        rollingFileAppender.start();

        return rollingFileAppender;
    }

    private SizeAndTimeBasedRollingPolicy<RollingPolicy> createLogRollingPolicy(
            RollingFileAppender<ILoggingEvent> rollingLogAppender) {
        SizeAndTimeBasedRollingPolicy<RollingPolicy> policy = new SizeAndTimeBasedRollingPolicy<>();
        policy.setContext(loggerContext);
        policy.setParent(rollingLogAppender);
        policy.setFileNamePattern(LOG_NAME_PATTERN);
        policy.setMaxHistory(MAX_HISTORY);
        policy.setTotalSizeCap(FileSize.valueOf((TOTAL_SIZE)));
        policy.setMaxFileSize(FileSize.valueOf(MAX_FILE_SIZE));
        policy.start();

        return policy;
    }

    private PatternLayoutEncoder createLogEncoder(String pattern) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern(pattern);
        encoder.start();

        return encoder;
    }

    private RollingFileAppender<ILoggingEvent> createLogAppender(String appendName,
                                                                 PatternLayoutEncoder rollingLogEncoder) {
        RollingFileAppender<ILoggingEvent> logRollingAppender = new RollingFileAppender<>();
        logRollingAppender.setName(appendName);
        logRollingAppender.setContext(loggerContext);
        logRollingAppender.setFile(FILE_NAME);
        logRollingAppender.setEncoder(rollingLogEncoder);

        return logRollingAppender;
    }
}
