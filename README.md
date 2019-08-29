# Hotspot-Aggregator Application
###API 

- Member Session   
세션 관리를 위함. (redis 사용)  
개요: Member B/C에서 로그인처리 후 넘어온 멤버 정보를 redis에 저장. (유효기간 설정).  
저장되어 있는 값이 있다면 Validation(Member 도메인 호출) 후 customerId를 조회하여 Redis에 저장  
(RequestContext 사용)

- Find store list with review by current location  
개요: Review정보와 Store정보를 같이 조회  
TODO: Review정보를 Bulk Select하도록? (Review쪽 기능 추가가 선행되어야 함)

- Post Review  
리뷰 작성 시 customerId는 현재 로그인 한 사용자의 id가 자동으로 셋팅됨.