package com.pluralsight.security.entity;

import javax.validation.constraints.Email;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class User {

	@Id
	private String id;
	@NonNull
	private String username;
	@NonNull
	private String firstName;
	@NonNull
	private String lastName;
	@Email
	@NonNull
	private String email;
	@NonNull
	private String password;
	
}
