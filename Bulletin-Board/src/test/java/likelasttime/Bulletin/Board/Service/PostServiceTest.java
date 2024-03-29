package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.domain.posts.PostRequestDto;
import likelasttime.Bulletin.Board.domain.posts.PostResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.assertj.core.api.Assertions.*;

import java.util.*;

@SpringBootTest
class PostServiceTest {
    @Autowired
    PostServiceImpl postService;

    @AfterEach
    public void afterEach(){    // 각 테스트 종료될 때마다 실행
        postService.deleteAll();
    }

    @Test
    public void create() {
        //given
        PostRequestDto post=new PostRequestDto();
        post.setTitle("spring");
        post.setAuthor("iu");
        post.setContent("안녕");

        //when
        PostResponseDto post_object=postService.create(post);

        //then
        PostResponseDto findPost=postService.findById(post_object.getPostId());
        assertThat(post_object.getPostTitle()).isEqualTo(findPost.getPostTitle());
        assertThat(post_object.getAuthor()).isEqualTo(findPost.getAuthor());
        assertThat(post_object.getPostContent()).isEqualTo(findPost.getPostContent());
    }

    @Test
    public void findAll() {
        //given
        PostRequestDto post1=new PostRequestDto();
        post1.setTitle("Spring1");
        PostResponseDto post_object1=postService.create(post1);

        PostRequestDto post2=new PostRequestDto();
        post2.setTitle("Spring2");
        PostResponseDto post_object2=postService.create(post2);

        Pageable pageable= PageRequest.of(0,5);

        //when
        List<PostResponseDto> result=postService.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void findById() {
        // given
        PostRequestDto post=new PostRequestDto();
        post.setTitle("Spring");
        PostResponseDto post_object=postService.create(post);

        // when
        PostResponseDto result=postService.findById(post_object.getPostId());

        // then
        assertThat(result.getPostTitle()).isEqualTo("Spring");
        assertThat(result.getView()).isEqualTo(1);

    }

    @Test
    public void update(){
        // given
        PostRequestDto post=new PostRequestDto();
        post.setTitle("Spring1");
        post.setContent("hello");
        PostResponseDto post_object1=postService.create(post);

        // when
        post.setTitle("Spring2");
        PostResponseDto post_object2=postService.update(post_object1.getPostId(), post);

        // then
        assertThat(post_object2.getPostTitle()).isEqualTo("Spring2");
        assertThat(post_object2.getPostContent()).isEqualTo("hello");
        assertThat(post_object2.getView()).isEqualTo(0);

    }

    @Test
    public void deletePost(){
        // given
        PostRequestDto post=new PostRequestDto();
        post.setTitle("Spring");
        PostResponseDto post_object=postService.create(post);


        // when
        postService.deletePost(post_object.getPostId());

        // then
        List<PostResponseDto> all_post=postService.findAll();
        assertThat(all_post.size()).isEqualTo(0);
    }

    @Test
    public void search(){
        // given
        PostRequestDto post=new PostRequestDto();
        post.setTitle("hello");
        post.setContent("I like ..");
        post.setAuthor("ju");
        PostResponseDto post_object=postService.create(post);

        String keyword="ju";
        Pageable pageable= PageRequest.of(0,5);
        List<PostResponseDto> page=postService.findAll();

        // when
        Page<PostResponseDto> result=postService.search(keyword, keyword, keyword, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);

    }
}