package com.ll.exam.sbb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class QuestionRepositoryTests {

    @Autowired
    private QuestionRepository questionRepository;
    private static int lastSampleDataId;

    @BeforeEach
    void beforeEach() {
        clearData();
        createSampleData();
    }

    public static int createSampleData(QuestionRepository questionRepository) {
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        questionRepository.save(q1);

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        questionRepository.save(q2);

        return q2.getId();
    }

    public static void clearData(QuestionRepository questionRepository) {
        questionRepository.disableForeignKeyChecks();
        questionRepository.truncate();
        questionRepository.enableForeignKeyChecks();
    }

    private void createSampleData() {
        lastSampleDataId = createSampleData(questionRepository);
    }

    private void clearData() {
        clearData(questionRepository);
    }
    @Test
    void 저장() {
        assertThat(lastSampleDataId).isEqualTo(2);
    }

    @Test
    void 삭제() {
        assertThat(questionRepository.count()).isEqualTo(lastSampleDataId);
        Question q = this.questionRepository.findById(1).orElse(null);
        questionRepository.delete(q);
        assertThat(this.questionRepository.count()).isEqualTo(lastSampleDataId-1);
    }

    @Autowired
    private AnswerRepository answerRepository;

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
    void 답변_조회하기() {
        Optional<Answer> oa = this.answerRepository.findById(1);
        if(oa.isPresent()) {
            Answer a = oa.get();
            assertThat(a.getQuestion().getId()).isEqualTo(1);
        }
    }

//    @Test
//    @Transactional
//    void 질문에_달린_답변찾기() {
//        Optional<Question> oq = this.questionRepository.findById(1);
//        if (oq.isPresent()) {
//            Question q = oq.get();
//
//            List<Answer> answerList = q.getAnswerList();
//            assertThat(answerList.size()).isEqualTo(1);
//            assertThat(answerList.get(0).getContent()).isEqualTo("springboot board의 줄임말입니다.");
//        }
//    }

}
