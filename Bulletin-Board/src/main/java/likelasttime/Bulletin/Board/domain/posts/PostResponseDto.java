package likelasttime.Bulletin.Board.domain.posts;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Setter
public class PostResponseDto implements Serializable {
    private Long postId;
    private String postTitle;
    private String author;
    private String postContent;
    private Long fileId;

    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    private LocalDateTime createdDate;

    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    private LocalDateTime modifiedDate;

    private int view;
    private List<CommentResponseDto> comment;
    private int comment_cnt;

    @Builder
    public PostResponseDto(Post post) {
        this.postId=post.getId();
        this.postTitle=post.getTitle();
        this.author=post.getAuthor();
        this.postContent=post.getContent();
        this.createdDate=post.getCreatedDate();
        this.modifiedDate=post.getModifiedDate();
        this.view=post.getView();
        this.comment=post.getComment().stream().map(CommentResponseDto::new).collect(Collectors.toList());
        this.comment_cnt=post.getComment_cnt();
    }
}
