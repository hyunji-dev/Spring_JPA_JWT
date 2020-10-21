package com.cos.jwt.domain.person;
/*
 * 2020.10.21-2 
 * 선행: IndexController.java
 * 후행: PersonRepository.java
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Person {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;
	
	@Column(unique = true) 
	private String username;
	
	private String password;
	private String email;
	
	
}