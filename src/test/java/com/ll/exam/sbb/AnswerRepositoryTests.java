package com.ll.exam.sbb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
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
        a1.setQuestion(q);
        answerRepository.save(a1);

        Answer a2 = new Answer();
        a2.setContent("sbb에서는 주로 스프링부트관련 내용을 다룹니다.");
        a2.setCreateDate(LocalDateTime.now());
        a2.setQuestion(q);
        answerRepository.save(a2);

    }

    private void clearData() {
        QuestionRepositoryTests.clearData(questionRepository);

        answerRepository.truncateTable();

    }

    @Test
    void 저장() {
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

    @Test
    void 조회() {
        Optional<Answer> oa = this.answerRepository.findById(1);
        if(oa.isPresent()) {
            Answer a = oa.get();
            assertThat(a.getContent()).isEqualTo("sbb는 질문답변 게시판 입니다.");
        }

    }
}
