/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vasiand.spring.boot.log4jdbc;


import jakarta.annotation.PostConstruct;
import net.sf.log4jdbc.log.slf4j.Slf4jSpyLogDelegator;
import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

import static ru.vasiand.spring.boot.log4jdbc.Log4jdbcProperties.PROPERTIES;

/**
 * A {@link BeanPostProcessor} implementation that sets up log4jdbc logging.
 *
 * @see net.sf.log4jdbc.Properties
 */
public class Log4jdbcBeanPostProcessor implements BeanPostProcessor {
    @Autowired
    private Environment environment;

    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        if (bean instanceof DataSource) {
            return new DataSourceSpy((DataSource) bean);
        }
        else {
            return bean;
        }
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        return bean;
    }

    @PostConstruct
    public void postConstruct() {
        // Log4jdbc only reads configuration from system properties, so copy relevant environment property to system properties
        // See net.sf.log4jdbc.Properties.getProperties()
        for (final String property : PROPERTIES) {
            if (this.environment.containsProperty(property)) {
                System.setProperty(property, this.environment.getRequiredProperty(property));
            }
        }
        // Use slf4j by default.
        // Most users will have slf4j configured (because Spring does that by default) and they won't be using log4j (which is the log4jdbc default)
        System.setProperty(
            "log4jdbc.spylogdelegator.name",
            this.environment.getProperty("log4jdbc.spylogdelegator.name", Slf4jSpyLogDelegator.class.getName())
        );
    }
}
