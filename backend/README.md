# blog_v1
군대에서 GO연습겸 백엔드 연습용으로 만드는 블로그 프로젝트<br>
프론트엔드는 Line Developer 블로그를 참조하면서 직접 html과 css로 만들었음(반응형).

# 기술 스택
- API : CKEditor4, Fontawesome<br>
- Database : MYSQL<br>
- Backend: Go(Echo framework)<br>
- Frontend: HTML, CSS, Javascript<br>


# 기능
- 게시글CURD
- 로그인 인증
- 이미지 업로드

### handlers.go
- Echo와 <a href="https://github.com/elixter/blogmodel">blogmodel</a>를 엮어서 비즈니스로직을 담아둔곳.
- #### 주요 함수들
    - ServePosts(c echo.Context) error -> 블로그의 게시글 목록을 최신순으로 보여주는 function
    - NewPost(c echo.Context) error -> http method가 GET이면 글쓰기 화면을, POST이면 작성한 게시글을 DB에 추가후 게시글로 redirect
    - ServePost (c.echo.Context) error -> 게시글 자세히보기, url parameter로 게시글 번호 가져와서 해당 게시글을 DB로 부터 가져옴.
    - DeletePost (c.echo.Context) error -> 게시글 삭제, ServePost와 동일한 방식으로 게시글을 DB로 부터 삭제
    - EditPost (c.echo.Context) error -> 게시굴 수정, ServePost와 동일한 방식으로 게시글을 글쓰기 페이지에 불러와서 게시글 수정
    - Conditional... -> 게시글을 해쉬태그 또는 카테고리에 따라서 처리하도록 만든 함수.
    
- ### 이미지 처리
    1. 업로드시키는 이미지를 서버의 이미지 폴더에 저장 후 Map["이미지이름.jpg"]의 값을 1로 만들어준다.
    2. 게시글 작성중 이미지를 제거했을 땐 Map["이미지이름.jpg"]의 값을 0으로 만들어준다.
    3. 게시글을 http metoh post로 서버로 보냈을 때 Map["이미지이름.jpg"]의 값이 0인 이미지들을 키값을 이용하여 서버의 임시폴더에서 모두 제거.
    
---
# Demo화면
- ## index page
<img src="https://github.com/elixter/blog_v1/blob/master/index.jpg?raw=true">

- ## blog main page
<img src="https://github.com/elixter/blog_v1/blob/master/blog.jpg?raw=true">

- ## login page
<img src="https://github.com/elixter/blog_v1/blob/master/login.jpg?raw=true">
