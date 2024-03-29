package likelasttime.Bulletin.Board.validator;

import likelasttime.Bulletin.Board.domain.posts.PostRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

@Component
public class PostValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz){
        return PostRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors){
        PostRequestDto p= (PostRequestDto) obj;

        if(StringUtils.isEmpty(p.getTitle())){
            errors.rejectValue("title", "empty.title", "제목을 입력하세요.");
        }

        if(StringUtils.isEmpty(p.getContent())){
            errors.rejectValue("content", "empty.content", "내용을 입력하세요.");
        }
    }
}
