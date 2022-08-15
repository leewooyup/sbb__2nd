package com.ll.exam.sbb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SbbApplicationTests {



    @Test
    void contextLoads() {
    }

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void testJpa() {
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);
    }

    @Test
    void 모든_질문_가져오기() {
        List<Question> all = this.questionRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        Question q = all.get(0);
        assertThat(q.getSubject()).isEqualTo("sbb가 무엇인가요?");
    }

    @Test
    void 특정_질문_가져오기() {
        Optional<Question> oq = this.questionRepository.findById(1);
        if (oq.isPresent()) {
            Question q = oq.get();
            assertThat(q.getSubject()).isEqualTo("sbb가 무엇인가요?");
        }
    }

    @Test
    void 인터페이스_변경을_통해_새로운_메서드_사용() {
        Optional<Question> oq = this.questionRepository.findBySubject("sbb가 무엇인가요?");
        if (oq.isPresent()) {
            Question q = oq.get();
            assertThat(q.getId()).isEqualTo(1);
        }
    }

    @Test
    void 두개의_엔티티속성명을_통해_조회() {
        Optional<Question> oq = this.questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
        if (oq.isPresent()) {
            Question q = oq.get();
            assertThat(q.getId()).isEqualTo(1);
        }
    }

    @Test
    void 조건이_like인_경우_조회() {
        List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
        Question q = qList.get(0);
        assertThat(q.getSubject()).isEqualTo("sbb가 무엇인가요?");
    }

    @Test
    void 데이터_수정하기() {
        Optional<Question> oq = this.questionRepository.findById(1);
        if(oq.isPresent()) {
            Question q = oq.get();
            q.setSubject("수정된제목");
            this.questionRepository.save(q);
        }
    }

    @Test
    void 데이터_삭제하기() {
        assertThat(this.questionRepository.count()).isEqualTo(4);
        Optional<Question> oq = this.questionRepository.findById(2);
        if(oq.isPresent()) {
            Question q = oq.get();
            this.questionRepository.delete(q);
        }
        assertThat(this.questionRepository.count()).isEqualTo(3);
    }

    @Test
    void 답변생성후_저장하기() {
        Optional<Question> oq = this.questionRepository.findById(1);
        if(oq.isPresent()) {
            Question q = oq.get();

            Answer a = new Answer();
            a.setContent("springboot board의 줄임말입니다.");
            a.setCreateDate(LocalDateTime.now());
            a.setQuestion(q);
            this.answerRepository.save(a);
        }
    }
}
