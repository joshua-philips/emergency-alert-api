package com.jphilips.springemergencyapi;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jphilips.springemergencyapi.models.Role;
import com.jphilips.springemergencyapi.models.user.StaffUser;
import com.jphilips.springemergencyapi.repositories.RoleRepository;
import com.jphilips.springemergencyapi.repositories.StaffUserRepository;

@SpringBootApplication
public class SpringEmergencyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEmergencyApiApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, StaffUserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			Role adminRole;
			if (!roleRepository.existsById(1L) || !roleRepository.existsById(2L)) {
				adminRole = roleRepository.save(new Role(0L, "Administrator"));
				roleRepository.save(new Role(0L, "User"));
				roleRepository.save(new Role(0L, "Staff"));

				userRepository.save(StaffUser.builder()
						.roles(Set.of(adminRole))
						.username("philipsjoshua96@gmail.com")
						.first_name("Joshua")
						.last_name("Philips")
						.phone_number("0209120905")
						.password(passwordEncoder.encode("123456"))
						.is_account_enabled(true)
						.is_account_locked(false)
						.build());
			}

		};
	}

}
