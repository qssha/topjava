package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.TimingRules;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.Profiles.JDBC;
import static ru.javawebinar.topjava.util.ValidationUtil.getRootCause;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
abstract public class AbstractServiceTest {

    @Autowired
    protected Environment environment;

    @ClassRule
    public static ExternalResource summary = TimingRules.SUMMARY;

    @Rule
    public Stopwatch stopwatch = TimingRules.STOPWATCH;

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    //  Check root cause in JUnit: https://github.com/junit-team/junit4/pull/778
    public <T extends Throwable> void validateRootCause(Runnable runnable, Class<T> rootExceptionClass) {
        assertThrows(rootExceptionClass, () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw getRootCause(e);
            }
        });
    }

    protected void checkJdbcProfile() {
        for (String profile :
                environment.getActiveProfiles()) {
            Assume.assumeFalse(profile.equals(JDBC));
        }
    }
}