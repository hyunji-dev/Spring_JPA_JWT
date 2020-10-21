package com.cos.jwt.web;
/*
 * 2020.10.21-1 
 * 선행: yml 파일 설정 
 * 후행: Person.java
 * 
 * 리액트와 연동하는 방법 
 * 		1.@CrossOrigin
 * 		2.response.setHeader("Access-Control-Allow-Origin", "*");
 * 		3.filter
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cos.jwt.domain.person.Person;
import com.cos.jwt.domain.person.PersonRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class IndexController {
	
	private final PersonRepository personRepository;
	
	@GetMapping({"", "/"})
	public String index() {
		return "index";
	}
	
	@PostMapping("/joinProc")
	public String 회원가입(@RequestBody Person person) {
		personRepository.save(person);
		return "ok";
	}
	
	// @CrossOrigin: 시큐리티에서는 시큐리티 직전 필터에 적용해야함
	//@CrossOrigin(origins = "http://127.0.0.1.5500", methods = RequestMethod.GET)  
	@GetMapping("/person/{id}")
	public ResponseEntity<?> 회원정보(@PathVariable int id, HttpServletResponse response, HttpServletRequest request) {
		//response.setHeader("Access-Control-Allow-Origin", "*");
		
		// 인증이 필요
		String jwtToken = request.getHeader("Authorization"); // 베리어가 붙은 토큰 
		System.out.println("회원정보요청: " + jwtToken);
		
		
		
		if(jwtToken == null) {
			// ResponseEntity<?> 리턴될 값이 오브젝트일때도 있고 String일때도 있을 때 사용(리턴값이 정해지지 않았을 경우에 씀) 
			return new ResponseEntity<String>("인증안됨", HttpStatus.FORBIDDEN); // 403 에러 리턴 
		} else {
			// 토큰 변경 
			jwtToken = jwtToken.replace("Bearer ", ""); // 베리어 뗀 토큰 
			System.out.println("베리어 뗀 jwtToken: " + jwtToken);
			
			int personId = 
					JWT.require(Algorithm.HMAC512("비밀키")).build().verify(jwtToken).getClaim("id").asInt(); // 디코딩된 jwt 리턴해줌 
			System.out.println("personID" + personId);
			
			if(personId != 0) {
				System.out.println("personId : "+personId);
				return new ResponseEntity<Person>(personRepository.findById(id).get(), HttpStatus.OK); // 200번 리턴(정상)
			}else {
				return new ResponseEntity<String>("JWT 토큰 검증 실패", HttpStatus.BAD_REQUEST);
			}
			
			
			//return personRepository.findById(id).get();
		}
	}
}
