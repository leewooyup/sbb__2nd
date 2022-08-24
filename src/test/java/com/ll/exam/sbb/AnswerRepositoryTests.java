package com.ll.exam.sbb;

import com.ll.exam.sbb.answer.Answer;
import com.ll.exam.sbb.answer.AnswerRepository;
import com.ll.exam.sbb.question.Question;
import com.ll.exam.sbb.question.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AnswerRepositoryTests {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    private int lastSampleDataId;

    @BeforeEach
    void beforeEach() {
        clearData();
        createSampleData();
    }

    private void createSampleData() {
        QuestionRepositoryTests.createSampleData(questionRepository);

        Question q = questionRepository.findById(1).get();

        Answer a1 = new Answer();
        a1.setContent("sbb는 질문답변 게시판 입니다.");
        a1.setCreateDate(LocalDateTime.now());
        q.addAnswer(a1);

        
        Answer a2 = new Answer();
        a2.setContent("sbb에서는 주로 스프링부트관련 내용을 다룹니다.");
        a2.setCreateDate(LocalDateTime.now());
        q.addAnswer(a2);

        questionRepository.save(q);
    }

    public static void clearData(AnswerRepository answerRepository, QuestionRepository questionRepository) {
        QuestionRepositoryTests.clearData(questionRepository);

        answerRepository.deleteAll();
        answerRepository.truncateTable();

    }

    private void clearData() {
        clearData(answerRepository, questionRepository);
    }

    @Test
    @Transactional
    @Rollback(false)
    void 저장() {
        Optional<Question> oq = this.questionRepository.findById(1);
        if(oq.isPresent()) {
            Question q = oq.get();

            Answer a1 = new Answer();
            a1.setContent("sbb는 질문답변 게시판 입니다.");
            a1.setCreateDate(LocalDateTime.now());
            q.addAnswer(a1);


            Answer a2 = new Answer();
            a2.setContent("sbb에서는 주로 스프링부트관련 내용을 다룹니다.");
            a2.setCreateDate(LocalDateTime.now());
            q.addAnswer(a2);

            questionRepository.save(q);
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    void 조회() {
        Optional<Answer> oa = this.answerRepository.findById(1);
        if(oa.isPresent()) {
            Answer a = oa.get();
            assertThat(a.getContent()).isEqualTo("sbb는 질문답변 게시판 입니다.");
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    void 답변에_관련_질문_조회() {
        Optional<Answer> oa = this.answerRepository.findById(1);
        if(oa.isPresent()) {
            Answer a = oa.get();
            Question q = a.getQuestion();

            assertThat(q.getId()).isEqualTo(1);
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    void 질문_관련_답변_조회() {
        Optional<Question> oq = questionRepository.findById(1);
        if(oq.isPresent()) {
            Question q = oq.get();
            //DB 연결이 끊김
            List<Answer> answerList = q.getAnswerList();
            // 오류 발생, DB연결이 끊겨서
            assertThat(answerList.size()).isEqualTo(2);
            assertThat(answerList.get(0).getContent()).isEqualTo("sbb는 질문답변 게시판 입니다.");
        }
    }
}
