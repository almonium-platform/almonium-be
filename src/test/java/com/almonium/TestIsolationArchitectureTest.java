package com.almonium;

import static org.assertj.core.api.Assertions.assertThat;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.properties.HasAnnotations;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

/**
 * Every database-backed test shares one Postgres container for the whole JVM, so a row that outlives its test
 * transaction leaks into every class that runs afterwards. Which class runs afterwards depends on the filesystem
 * order of the compiled classes, so such a leak passes on one machine and fails on another.
 */
@AnalyzeClasses(packages = "com.almonium", importOptions = ImportOption.OnlyIncludeTests.class)
class TestIsolationArchitectureTest {

    @ArchTest
    static void sql_fixtures_must_run_inside_the_test_transaction(JavaClasses classes) {
        List<String> violations = new ArrayList<>();

        annotatedElements(classes)
                .forEach(element -> sqlAnnotations(element).forEach(sql -> {
                    if (sql.executionPhase() == Sql.ExecutionPhase.BEFORE_TEST_CLASS
                            || sql.executionPhase() == Sql.ExecutionPhase.AFTER_TEST_CLASS) {
                        violations.add(describe(element) + " runs @Sql in phase " + sql.executionPhase());
                    }
                }));

        assertThat(violations)
                .as(
                        "class-level @Sql phases commit outside the test transaction and leak rows into the shared database")
                .isEmpty();
    }

    @ArchTest
    static void tests_must_not_commit_their_transaction(JavaClasses classes) {
        List<String> violations = new ArrayList<>();

        annotatedElements(classes).forEach(element -> {
            if (element.isAnnotatedWith(Commit.class)) {
                violations.add(describe(element) + " is annotated with @Commit");
            }
            if (element.isAnnotatedWith(Rollback.class)
                    && !element.getAnnotationOfType(Rollback.class).value()) {
                violations.add(describe(element) + " is annotated with @Rollback(false)");
            }
        });

        assertThat(violations)
                .as("committed test transactions leak rows into the shared database")
                .isEmpty();
    }

    private static Stream<HasAnnotations<?>> annotatedElements(JavaClasses classes) {
        return classes.stream()
                .flatMap(javaClass -> Stream.concat(Stream.of(javaClass), javaClass.getMethods().stream()));
    }

    private static Stream<Sql> sqlAnnotations(HasAnnotations<?> element) {
        Stream<Sql> single =
                element.isAnnotatedWith(Sql.class) ? Stream.of(element.getAnnotationOfType(Sql.class)) : Stream.empty();
        Stream<Sql> grouped = element.isAnnotatedWith(SqlGroup.class)
                ? Stream.of(element.getAnnotationOfType(SqlGroup.class).value())
                : Stream.empty();
        return Stream.concat(single, grouped);
    }

    private static String describe(HasAnnotations<?> element) {
        return switch (element) {
            case JavaClass javaClass -> javaClass.getName();
            case JavaMethod method -> method.getFullName();
            default -> element.toString();
        };
    }
}
