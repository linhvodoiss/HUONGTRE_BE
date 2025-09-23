package com.fpt;

import com.fpt.entity.*;
import com.fpt.repository.UserRepository;
import com.fpt.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService; // Service containing getAllUser()

	// 1. Multiple users
	@Test
	void getAllUser_multipleUsers() {
		User u1 = User.builder()
				.id(1L).userName("alice").email("alice@example.com")
				.firstName("Alice").lastName("Smith")
				.phoneNumber("0123456789")
				.role(Role.CUSTOMER).status(UserStatus.ACTIVE)
				.isActive(true)
				.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
				.build();

		User u2 = User.builder()
				.id(2L).userName("bob").email("bob")
				.firstName("Bob").lastName("Johnson")
				.phoneNumber("0987654321")
				.role(Role.CUSTOMER).status(UserStatus.ACTIVE)
				.isActive(true)
				.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
				.build();

		Pageable pageable = PageRequest.of(0, 10);
		when(userRepository.findAll(any(Specification.class), eq(pageable)))
				.thenReturn(new PageImpl<>(List.of(u1, u2)));

		Page<User> result = userService.getAllUser(pageable, "a", 1, true);

		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getContent()).extracting("userName")
				.containsExactly("alice", "bob");
	}

	// 2. Single user
	@Test
	void getAllUser_singleUser() {
		User u1 = User.builder()
				.id(3L).userName("carol").email("carol@example.com")
				.firstName("Carol").lastName("White")
				.phoneNumber("0111222333")
				.role(Role.CUSTOMER).status(UserStatus.NOT_ACTIVE)
				.isActive(true)
				.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
				.build();

		Pageable pageable = PageRequest.of(1, 5);
		when(userRepository.findAll(any(Specification.class), eq(pageable)))
				.thenReturn(new PageImpl<>(List.of(u1)));

		Page<User> result = userService.getAllUser(pageable, "carol", 2, true);

		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().get(0).getUserName()).isEqualTo("carol");
	}

	// 3. Empty result
	@Test
	void getAllUser_emptyResult() {
		Pageable pageable = PageRequest.of(0, 1);
		when(userRepository.findAll(any(Specification.class), eq(pageable)))
				.thenReturn(Page.<User>empty());

		Page<User> result = userService.getAllUser(pageable, "none", null, false);

		assertThat(result).isNotNull();
		assertThat(result.getContent()).isEmpty();
	}

	// 4. Exception from repository
	@Test
	void getAllUser_repositoryThrowsException() {
		Pageable pageable = PageRequest.of(0, 10);
		when(userRepository.findAll(any(Specification.class), eq(pageable)))
				.thenThrow(new RuntimeException("Test DB error"));

		assertThatThrownBy(() -> userService.getAllUser(pageable, "error", 0, null))
				.isInstanceOf(RuntimeException.class)
				.hasMessageContaining("Test DB error");
	}
}

