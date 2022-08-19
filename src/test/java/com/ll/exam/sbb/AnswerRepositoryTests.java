package com.ll.exam.sbb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

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

    }

    private void clearData() {
        questionRepository.disableForeignKeyChecks();
        answerRepository.truncate();
        questionRepository.enableForeignKeyChecks();
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
}
