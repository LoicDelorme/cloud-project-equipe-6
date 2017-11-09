package fr.polytech.cloud.configurations;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class DynamicConfiguration implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    public String getProperty(String key) {
        return this.environment.getProperty(key);
    }
}