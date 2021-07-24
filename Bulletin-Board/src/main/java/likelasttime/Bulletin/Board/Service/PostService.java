package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.Repository.SpringDataJpaPostRepository;
import likelasttime.Bulletin.Board.domain.posts.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostService {
    private final SpringDataJpaPostRepository postRepository;
    private static final int PAGE_NUM_COUNT=5;
    private static final int POST_COUNT=4;  // 페이지당 게시글 수

    public PostService(SpringDataJpaPostRepository postRepository){
        this.postRepository = postRepository;
    }

    // 게시글 작성
    public Long join(Post post){
        //validateDuplicatePost(post);  // 중복 게시글
        postRepository.save(post);
        return post.getId();
    }

    private void validateDuplicatePost(Post post){
        postRepository.findByTitle(post.getTitle())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 게시글입니다.");
                });
    }

    //전체 게시글 조회
    public Page<Post> findPost(Pageable pageable){
        return postRepository.findAll(pageable);
    }

    // 특정 게시글 조회
    public <Optional>Post findOne(Long postsId){
        return postRepository.findById(postsId).get();
    }

    // 삭제
    public void deletePost(Long id){
        postRepository.deleteById(id);
    }

    // 조회수
   public void updateView(Long id) {
        Post post=findOne(id);
        if(post.getView() == null) post.setView(1);
        else post.setView(post.getView()+1);
        postRepository.save(post);
    }

}
