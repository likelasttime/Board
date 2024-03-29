package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.Repository.PostRepository;
import likelasttime.Bulletin.Board.Repository.UserRepository;
import likelasttime.Bulletin.Board.domain.posts.Comment;
import likelasttime.Bulletin.Board.domain.posts.CommentResponseDto;
import likelasttime.Bulletin.Board.domain.posts.Post;
import likelasttime.Bulletin.Board.domain.posts.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentServiceTest {
    @Autowired
    CommentService commentService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @BeforeEach
    public void beforeEach(){
        User user=User.builder()
                .name("츄츄")
                .phone("01012345678")
                .email("a@gmail.com")
                .password("goodday00!")
                .username("alswjd00")
                .enabled(true)
                .build();
        userRepository.save(user);
        Post post=Post.builder()
                .title("첫 번째")
                .content("안녕")
                .author("im")
                .view(0)
                .comment_cnt(0)
                .build();
        postRepository.save(post);
    }

    @AfterEach
    public void afterEach(){
        commentService.deleteAll();
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @Transactional
    public void commentSave(){
        // given
        String name="alswjd00";
        Long id=postRepository.findAll(Sort.by("id")).get(0).getId();
        String content="반가워요";

        // when
        Long comment_id=commentService.commentSave(name, id, content);
        Comment comment=commentService.findById(comment_id);

        // then
        assertThat(comment.getUser().getUsername()).isEqualTo("alswjd00");
        assertThat(comment.getContent()).isEqualTo("반가워요");
        assertThat(comment.getPost().getId()).isEqualTo(id);
    }

    @Test
    public void update(){
        // given
        String name="alswjd00";
        Long id=postRepository.findAll(Sort.by("id")).get(0).getId();
        String content="반가워요";

        // when
        Long comment_id=commentService.commentSave(name, id, content);
        Comment comment=commentService.findById(comment_id);
        content="사랑합니다.";
        commentService.update(comment_id, content);

        // then
        assertThat(commentService.findById(comment_id).getContent()).isEqualTo("사랑합니다.");
    }

    @Test
    public void delete(){
        // given
        String name="alswjd00";
        Long id=postRepository.findAll(Sort.by("id")).get(0).getId();
        String content="반가워요";

        // when
        Long comment_id=commentService.commentSave(name, id, content);
        commentService.delete(comment_id, id);

        // then
        assertThat(commentService.getCommentList(id).size()).isEqualTo(0);
    }

    @Test
    public void deleteAll(){
        // given
        String name="alswjd00";
        Long id=postRepository.findAll(Sort.by("id")).get(0).getId();
        String content="반가워요";

        // when
        Long comment_id=commentService.commentSave(name, id, content);
        commentService.deleteAll();

        // then
        assertThat(commentService.getCommentList(id).size()).isEqualTo(0);
    }

    @Test
    public void findById() {
        // given
        String name = "alswjd00";
        Long id = postRepository.findAll(Sort.by("id")).get(0).getId();
        String content = "반가워요";

        // when
        Long comment_id = commentService.commentSave(name, id, content);
        Comment comment = commentService.findById(comment_id);

        // then
        assertThat(comment.getId()).isEqualTo(comment_id);
    }

    @Test
    public void findAll(){
        // given
        String name_1 = "alswjd00";
        Long id = postRepository.findAll(Sort.by("id")).get(0).getId();
        String content_1 = "반가워요";

        String name_2="alswjd00";
        String content_2="사랑해요";

        // when
        commentService.commentSave(name_1, id, content_1);
        commentService.commentSave(name_2, id, content_2);
        List<CommentResponseDto> commentList=commentService.getCommentList(id);

        // then
        assertThat(commentList.size()).isEqualTo(2);

    }
}
