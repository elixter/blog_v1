# Platform project
- 기존 Go기반 블로그 서버에서 Java Spring boot와 typescript react로 전환 작업중

## 기술스택
- Backend : Java11 Spring boot
- Frontend : Typescript, React
- Database : MySQL

## 기능
- [X] 블로그
- [ ] SNS 통합 피드

## TODO List
### Backend
- [x] POST CRUD 
- [x] User auth
- [x] Image upload
- [x] JPA 전환
- [ ] XSS 방지 => 프론트에서 처리되기 때문에 보류
- [ ] 회원가입시 아이디 중복확인 API
- [x] 이메일 인증 API (Optional)

### Frontend
- [X] 게시글 작성 중 이미지 업로드 했을 때 url들 state로 저장
- [X] 카테고리별 게시글 페이지 
- [X] 해쉬태그별 게시글 페이지
- [X] 로그인, 회원가입 페이지
- [ ] 로그아웃
- [ ] 내비게이션바에서 사용자 옵션 드롭메뉴
- [ ] SNS 피드 페이지
