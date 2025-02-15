package org.example.jtsb02.question.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.example.jtsb02.category.entity.Category;
import org.example.jtsb02.category.repository.CategoryRepository;
import org.example.jtsb02.common.exception.DataNotFoundException;
import org.example.jtsb02.member.entity.Member;
import org.example.jtsb02.member.repository.MemberRepository;
import org.example.jtsb02.question.dto.QuestionDto;
import org.example.jtsb02.question.entity.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("질문 등록")
    void save() {
        //given
        Member member = memberRepository.save(Member.of(
            "onlyTset",
            "onlyTest",
            "onlyTest",
            "onlyTest@gmail.com")
        );
        Category category = categoryRepository.save(Category.of("질문답변"));
        Question question = Question.of("제목1", "내용1", member, category);

        //when
        Question save = questionRepository.save(question);

        //then
        assertThat(save).isNotNull();
        assertThat(save.getId()).isEqualTo(question.getId());
        assertThat(save.getSubject()).isEqualTo("제목1");
        assertThat(save.getContent()).isEqualTo("내용1");
        assertThat(save.getHits()).isEqualTo(0);
        assertThat(save.getAuthor().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("질문 목록 조회")
    void findAll() {
        //given
        Member member = memberRepository.save(Member.of(
            "onlyTset",
            "onlyTest",
            "onlyTest",
            "onlyTest@gmail.com")
        );
        Category category = categoryRepository.save(Category.of("질문답변"));
        Question question1 = Question.of("제목1", "내용1", member, category);
        Question question2 = Question.of("제목2", "내용2", member, category);
        questionRepository.save(question1);
        questionRepository.save(question2);

        //when
        List<QuestionDto> questions = questionRepository.findAll().stream()
            .map(QuestionDto::fromQuestion).toList();

        //then
        assertThat(questions.size()).isEqualTo(2);
        assertThat(questions.getFirst().getSubject()).isEqualTo("제목1");
        assertThat(questions.getFirst().getContent()).isEqualTo("내용1");
        assertThat(questions.get(1).getSubject()).isEqualTo("제목2");
        assertThat(questions.get(1).getContent()).isEqualTo("내용2");
        assertThat(questions.get(1).getAuthor().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("ID로 질문 조회")
    void findById() {
        //given
        Member member = memberRepository.save(Member.of(
            "onlyTset",
            "onlyTest",
            "onlyTest",
            "onlyTest@gmail.com")
        );
        Category category = categoryRepository.save(Category.of("질문답변"));
        Question question = Question.of("제목1", "내용1", member, category);
        questionRepository.save(question);

        //when
        Optional<Question> result = questionRepository.findById(question.getId());

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(question.getId());
        assertThat(result.get().getSubject()).isEqualTo("제목1");
        assertThat(result.get().getContent()).isEqualTo("내용1");
        assertThat(result.get().getHits()).isEqualTo(0);
    }

    @Test
    @DisplayName("질문 수정")
    void modify() {
        //given
        Member member = memberRepository.save(Member.of(
            "onlyTset",
            "onlyTest",
            "onlyTest",
            "onlyTest@gmail.com")
        );
        Category category = categoryRepository.save(Category.of("질문답변"));
        Question question = Question.of("제목1", "내용1", member, category);
        questionRepository.save(question);

        //when
        Question result = questionRepository.findById(question.getId()).map(q -> q.toBuilder()
                .subject("수정한 제목")
                .content("수정한 내용")
                .modifiedAt(LocalDateTime.now())
                .hits(q.getHits() + 1)
                .build())
            .orElseThrow(() -> new DataNotFoundException("Question not found"));

        //then
        assertThat(result.getId()).isEqualTo(question.getId());
        assertThat(result.getSubject()).isEqualTo("수정한 제목");
        assertThat(result.getContent()).isEqualTo("수정한 내용");
        assertThat(result.getModifiedAt()).isNotNull();
        assertThat(result.getHits()).isEqualTo(1);
    }

    @Test
    @DisplayName("질문 삭제")
    void delete() {
        //given
        Member member = memberRepository.save(Member.of(
            "onlyTset",
            "onlyTest",
            "onlyTest",
            "onlyTest@gmail.com")
        );
        Category category = categoryRepository.save(Category.of("질문답변"));
        Question question = Question.of("제목1", "내용1", member, category);
        questionRepository.save(question);

        //when
        questionRepository.findById(question.getId()).ifPresent(q -> questionRepository.delete(q));

        //then
        assertThatThrownBy(() -> questionRepository.findById(question.getId())
            .orElseThrow(() -> new DataNotFoundException("Question not found")))
            .isInstanceOf(DataNotFoundException.class).hasMessageContaining("Question not found");
    }
}