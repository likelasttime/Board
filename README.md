# 📌CRUD 게시판  
> SpringBoot와 Spring Data JPA를 이용하여 웹 게시판 구현

![내 게시판](https://user-images.githubusercontent.com/46569105/128589230-04251c89-9971-4ee4-ac04-db2b36b5aa59.PNG)

</br>

📚 ToC
---------------
1. [기술 스택](#1-기술-스택)
2. [핵심 기능](#2-핵심-기능)
3. [프로젝트를 통해 배운 점](#3-프로젝트를-통해-배운-점)
</br>

## 1. 기술 스택

|Spring Boot|Spring Boot 2.5.2로 개발했습니다.|
|:----------:|:----------------------------------|
|**Java**|OOP를 이해하고 프로젝트에 적용했습니다.|
|**Spring Data JPA**|메소드 이름만으로 쿼리를 생성했습니다.|
|**MySQL**|영속적인 데이터 사용을 위해 RDBMS인 MySQL을 사용했습니다.|
|**Thymeleaf**|화면에 서버의 데이터를 출력하기 위해 사용했습니다.|
|**HTML**|화면을 만들기 위해 사용했습니다.|
</br>

## 2. 핵심 기능
- [작성](#2-1-게시글-작성)
- [조회 / 상세보기](#2-2-전체-게시글-조회-및-상세보기)
- [수정](#2-3-게시글-수정)
- [삭제](#2-4-글-삭제) 
- [Pagination](#2-5-pagination)
- [검색](#2-6-검색)
- [조회수](#2-7-조회수)
</br>

### 2-1. 게시글 작성
![글 작성](https://user-images.githubusercontent.com/46569105/128581530-11754618-b0d6-47dc-a12c-addc8dfde9db.gif)

제목, 작성자, 내용을 입력합니다.  
```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(updatable=false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
```
글을 작성하면 등록일자, 최근 수정한 일자가 데이터베이스에 저장됩니다.  
createdDate 속성을 updatable-false로 해서 글을 수정하더라도 변하지 않게 했습니다.  
updatable-false를 넣지 않았을 때는 게시글 수정시 null이 되는 에러가 발생했었습니다.

</br>

### 2-2. 전체 게시글 조회 및 상세보기
 
![상세보기](https://user-images.githubusercontent.com/46569105/128456571-3b830207-d07b-444c-8774-1238c7cf9b0e.gif)

게시글 리스트에서 게시글 번호, 제목, 작성자, 작성일자, 조회수를 출력합니다.  
상단 좌측에는 총 게시글 수를 나타냈습니다.  
```java
 // 상세 게시판 조회
    @GetMapping("/post/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Post post = postService.findOne(id);
        postService.updateView(id);     // 조회수 증가
        post.setView(post.getView()+1);     // 게시글 들어가면 바로 조회수 증가 시키기 위해서
        model.addAttribute("post", post);
        return "post/detail";
    }
```
getView 메소드로 조회수를 가져오고 1을 더한 값을 멤버변수 view에 저장합니다.

</br>

### 2-3. 게시글 수정

![글 수정](https://user-images.githubusercontent.com/46569105/128581705-1979e768-d4e0-4708-ba0b-99b833b311d4.gif)

get 요청으로 받은 id로 게시글을 찾습니다.  
updateForm.html을 반환해서 해당 게시글을 수정하는 페이지를 출력합니다.  
JS로 수정 버튼을 클릭했을 때 메시지를 띄웁니다.  
수정 버튼을 누르면 put 요청이 들어옵니다.  
id 값을 이용해서 해당 게시글의 조회수를 찾아 저장합니다.  
이 작업을 하지 않으면, 글 수정시 조회수가 0으로 초기화됩니다.

</br>

### 2-4. 글 삭제

![글 삭제](https://user-images.githubusercontent.com/46569105/128581890-3c4efa68-7af7-4784-9208-7e89d3012d4e.gif)

상세보기에서 해당 글을 삭제할 수 있습니다.  
JS로 삭제하기 전에 메시지를 띄웁니다.
id를 기준으로 게시글을 삭제합니다.  

</br>

### 2-5. Pagination

![페이징네이션](https://user-images.githubusercontent.com/46569105/128456185-2b7c3ab2-a337-4aa9-b004-4ba8b8d03322.gif)

한 페이지에 최대 5개의 게시글을 나타냅니다.  
최근에 등록한 게시글 순으로 내림차순 정렬했습니다.
이전, 다음 버튼으로 왼쪽 또는 오른쪽 이동이 가능합니다.  
맨 처음, 맨 끝에 있을 경우 각각 이전, 다음 버튼이 비활성화됩니다.

</br>

### 2-6. 검색

![검색](https://user-images.githubusercontent.com/46569105/128582027-6ab77af5-daa3-45e1-b909-c3e7b47dfd07.gif)

제목, 작성자, 내용을 대소문자 구분 없이 검색할 수 있습니다.  
전체 게시글을 조회하는 컨트롤러에서 검색어가 파라미터로 들어옵니다.  
검색한 내용들도 페이징네이션됩니다.  
```java
Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String content, String author, Pageable pageable);
```

</br>

### 2-7. 조회수 

```java
@Modifying
@Query("UPDATE Post p SET view=view+1 WHERE p.id=:id")
int updateView(@Param("id") Long id);
```  
    
다음과 같은 update 쿼리를 작성했습니다.  
게시글 상세보기를 할 때만 조회수가 1씩 증가합니다.

</br>

## 3. 프로젝트를 통해 배운 점
### 3-1. 스프링 데이터 JPA의 편리함
JPA를 사용하다가 스프링 데이터 JPA에 대해서 알게되고 적용했습니다.  
JpaRepository를 상속받는 인터페이스를 생성하고, 메소드 이름으로 쿼리가 자동으로 생성되는 점이 매우 흥미로웠습니다.  







