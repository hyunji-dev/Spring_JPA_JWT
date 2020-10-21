package com.cos.jwt.domain.person;
/*
 * 2020.10.21-3
 * 선행: Person.java
 * 후행: FilterConfig.java
 */

import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonRepository extends JpaRepository<Person, Integer>{

	Person findByUsernameAndPassword(String username, String password); // Integer: P.K의 타입

}
