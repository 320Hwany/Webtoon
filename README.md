# 웹툰 서비스

## 기술 스택 

Java 17, Spring Boot, Spring Data JPA, QueryDsl, Spring Rest Docs, JUnit 5, H2, MySQL, AWS EC2, AWS RDS

## Trouble Shooting
- [JPA 양방향 매핑 사용하는게 맞을까?](https://320hwany.tistory.com/75)

  - JPA 양방향 매핑을 사용하면 분명 편의성이 존재합니다.        
  - 하지만 이러한 편의성이 과연 순환 참조를 만들지 않는 것보다 더 중요한 지에 대해 생각해보았습니다.   

- [Spring Data JPA, QueryDsl 사용하여 조회 기능 구현하기](https://320hwany.tistory.com/66)     
  
  - 구현한 여러 조회 기능 중 대표적인 사례 4개를 정리해보았습니다.   
  
- [OSIV 설정을 False로 변경하기위한 리팩토링 과정](https://320hwany.tistory.com/65)        
  - OSIV 설정을 True로 하면 개발하기는 편하지만 성능상으로 문제가 발생할 수 있습니다.  
  - OSIV 설정을 False로 하여 각 계층이 가져야 할 역할을 정리하면서 리팩토링 해보았습니다.  
  
- [테스트 코드 작성시 @Transactional 사용하지 않기](https://320hwany.tistory.com/64)       
  - 테스트 코드 작성시 @Transactional을 사용하면 데이터베이스가 롤백되어 편리하지만 
  - 원래 코드를 테스트하기 위한 목적이 잘 이루어지지 않는다는 문제가 있습니다.
  - 이를 해결하기 위한 방법에 대해 생각해보았습니다.
  
- [요청과 응답에서 엔티티를 직접 사용하면 안되는 이유](https://320hwany.tistory.com/63)   
  - 기계적으로 사용했던 Dto를 언제, 왜 사용해야 하는지에 대해 생각해보았습니다.  
- [JPA 지연 로딩 전략 사용시 Json 반환이 안될 때, N+1 문제 해결](https://320hwany.tistory.com/61)   
  - DB에는 반영이 되지만 Json 반환이 안되는 원인에 대해 생각해보고 N+1 문제도 해결해보았습니다.  
  
- [각 계층이 가지고 있어야 할 코드에 대한 생각 - Controller, Service, Domain](https://320hwany.tistory.com/60)    
  - Layered architecture에서 각 계층이 어떤 역할과 책임을 가져야 하는 지에 생각해보았습니다.  
  
- [테스트 코드 작성시 로그인이 필요할 때](https://320hwany.tistory.com/59)    
  - 쿠키, 세션을 이용한 로그인 방식에서 로그인이 필요한 로직은 어떻게 테스트 해야하는 지에 대해 생각해보았습니다.   

