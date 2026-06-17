package com.eeshanoor.factory;

import com.github.javafaker.Faker;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for generating test user data.
 * Implements the Factory + Builder pattern for flexible test data creation.
 */
public class UserFactory {
    private static final Faker faker = new Faker();

    public static Map<String, String> validUser() {
        return new HashMap<>() {{
            put("name", faker.name().fullName());
            put("job",  faker.job().title());
        }};
    }

    public static Map<String, String> userWithName(String name) {
        return new HashMap<>() {{
            put("name", name);
            put("job",  faker.job().title());
        }};
    }

    public static Map<String, String> userWithJob(String job) {
        return new HashMap<>() {{
            put("name", faker.name().fullName());
            put("job",  job);
        }};
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String name = faker.name().fullName();
        private String job  = faker.job().title();

        public Builder name(String name)  { this.name = name; return this; }
        public Builder job(String job)    { this.job  = job;  return this; }

        public Map<String, String> build() {
            return new HashMap<>() {{ put("name", name); put("job", job); }};
        }
    }
}