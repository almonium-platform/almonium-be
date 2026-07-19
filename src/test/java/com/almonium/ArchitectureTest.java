package com.almonium;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.user.core.model.entity.User;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@AnalyzeClasses(packages = "com.almonium", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    static final ArchRule controllers_must_not_access_repositories = noClasses()
            .that()
            .resideInAPackage("..controller..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..repository..")
            .because("controllers should delegate persistence work to application services");

    @ArchTest
    static final ArchRule services_must_not_depend_on_controllers = noClasses()
            .that()
            .resideInAPackage("..service..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..controller..")
            .because("application services must remain independent of the web layer");

    @ArchTest
    static final ArchRule repositories_must_not_depend_on_controllers = noClasses()
            .that()
            .resideInAPackage("..repository..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..controller..")
            .because("persistence must remain independent of the web layer");

    @ArchTest
    static void controllers_must_not_bind_persistence_entities(JavaClasses classes) {
        List<String> violations = new ArrayList<>();

        classes.stream()
                .filter(javaClass -> javaClass.getPackageName().contains(".controller."))
                .flatMap(javaClass -> javaClass.getMethods().stream())
                .forEach(method -> method.getParameters().forEach(parameter -> {
                    if (parameter.getRawType().isEquivalentTo(User.class)) {
                        if (!parameter.isAnnotatedWith(Auth.class)) {
                            violations.add(method.getFullName() + " accepts User without @Auth");
                        }
                    } else if (parameter.getRawType().isAnnotatedWith(Entity.class)) {
                        violations.add(method.getFullName() + " binds JPA entity "
                                + parameter.getRawType().getName());
                    }
                }));

        assertThat(violations)
                .as("controllers may receive User only through the trusted @Auth resolver and must not bind entities")
                .isEmpty();
    }
}
