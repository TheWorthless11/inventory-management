package com.example.inventorymanagement;

import com.example.inventorymanagement.entity.Users;
import com.example.inventorymanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminBootstrapRunnerIntegrationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(RunnerConfig.class);

    @Test
    void createsAdminWhenNoAdminExistsAndPasswordIsSet() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        when(userRepository.existsByRole("ADMIN")).thenReturn(false);
        when(passwordEncoder.encode("renderSecret123")).thenReturn("encoded-secret");

        contextRunner
                .withPropertyValues("ADMIN_USERNAME=admin", "ADMIN_PASSWORD=renderSecret123")
                .withBean(UserRepository.class, () -> userRepository)
                .withBean(PasswordEncoder.class, () -> passwordEncoder)
                .run(context -> {
                    context.getBean(CommandLineRunner.class).run();

                    verify(userRepository).existsByRole("ADMIN");
                    verify(passwordEncoder).encode("renderSecret123");
                    verify(userRepository).save(argThat(user ->
                            "admin".equals(user.getUsername())
                                    && "encoded-secret".equals(user.getPassword())
                                    && "ADMIN".equals(user.getRole())
                    ));
                });
    }

    @Test
    void doesNotCreateAdminWhenAdminAlreadyExists() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        when(userRepository.existsByRole("ADMIN")).thenReturn(true);

        contextRunner
                .withPropertyValues("ADMIN_PASSWORD=renderSecret123")
                .withBean(UserRepository.class, () -> userRepository)
                .withBean(PasswordEncoder.class, () -> passwordEncoder)
                .run(context -> {
                    context.getBean(CommandLineRunner.class).run();

                    verify(userRepository).existsByRole("ADMIN");
                    verify(passwordEncoder, never()).encode(eq("renderSecret123"));
                    verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any(Users.class));
                });
    }

    @Test
    void missingOrBlankPasswordSkipsSafely() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        contextRunner
                .withPropertyValues("ADMIN_PASSWORD=   ")
                .withBean(UserRepository.class, () -> userRepository)
                .withBean(PasswordEncoder.class, () -> passwordEncoder)
                .run(context -> {
                    context.getBean(CommandLineRunner.class).run();

                    verify(userRepository, never()).existsByRole("ADMIN");
                    verify(passwordEncoder, never()).encode(org.mockito.ArgumentMatchers.anyString());
                    verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any(Users.class));
                });
    }

    @Configuration
    static class RunnerConfig {
        @Bean
        CommandLineRunner testCommandLineRunner(UserRepository userRepository,
                                                PasswordEncoder passwordEncoder,
                                                Environment environment) {
            return new InventoryManagementApplication().initAdmin(userRepository, passwordEncoder, environment);
        }
    }
}

