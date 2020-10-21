package com.cos.jwt.config.jwt;
/*
 * 2020.10.21-6
 * 선행: CorsFilter.java
 * 후행: 
 * 
 * JWT(제이슨웹토큰)을 만드는 방법
 * 		1. HMAC / 2. RSA
 * 	- 헤더에 암호화 방식, 메시지(민감개인정보X), 앞의 두개를 해쉬한 값이 필요함 
 * 		-> 3가지를 Base64로 암호화(인코딩, 디코딩 가능), 전자서명의 역할(신분증)  
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.domain.person.Person;
import com.cos.jwt.domain.person.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthenticationFilter implements Filter{
	
	private PersonRepository personRepository;
	
	public JwtAuthenticationFilter(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("JwtAuthenticationFilter 작동");
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		PrintWriter out = resp.getWriter();
		
		String method = req.getMethod();
		System.out.println(method);
		
		if(!method.equals("POST")) {
			System.out.println("POST요청이 아니기 때문에 거절");
			out.print("POST로 요청해주세요. Required post method."); // 바디 값으로 넘어감 
			out.flush();
		} else {
			System.out.println("POST요청이 맞습니다.");

			// 버퍼로 읽어서 오브젝트로 파씽해줌 
			ObjectMapper om = new ObjectMapper();
			try {
				Person person = om.readValue(req.getInputStream(), Person.class);
				System.out.println(person); 
				
				// 1.username, password를 DB에 던짐
				Person personEntity = 
						personRepository.findByUsernameAndPassword(person.getUsername(), person.getPassword());
				// 2.값이 있으면 있다, 없으면 없다 
				if(personEntity == null) {
					System.out.println("유저네임 혹은 패스워드가 틀렸습니다.");	
					out.print("fail");
					out.flush();
				}else {
					System.out.println("인증되었습니다.");
					
					//String jwtHeader = "{\"alg\" : \"HS256\"}";
					//String jwtMsg = "{\"userId\" : " + personEntity.getId() + " }";
					
					String jwtToken = JWT
							.create()
							.withSubject("토큰제목") // 토큰의 제목 
							.withExpiresAt(new Date(System.currentTimeMillis()+1000*60*30)) // 토큰유효시간 현재시간 + 10분 
							.withClaim("id", personEntity.getId())
							.withClaim("username", personEntity.getUsername())
							.sign(Algorithm.HMAC512("비밀키")) //키값, 노출되면 안됨
							;
					
					resp.addHeader("Authorization", "Bearer "+jwtToken);
					out.print("ok");
					out.flush();
				}
			} catch(Exception e) {
				System.out.println("오류 : "+e.getMessage());
			}
		}
		
	}
}
