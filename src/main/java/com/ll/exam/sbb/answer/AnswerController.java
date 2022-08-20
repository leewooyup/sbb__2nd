package com.ll.exam.sbb.answer;

import com.ll.exam.sbb.DataNotFoundException;
import com.ll.exam.sbb.question.Question;
import com.ll.exam.sbb.question.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequestMapping("/answer")
@Controller
@RequiredArgsConstructor
public class AnswerController {
    private final QuestionRepository questionRepository;
    private final AnswerService answerService;

    @PostMapping("/create/{id}")
    public String detail(Model model, @PathVariable int id, String content) {
        Optional<Question> oq = questionRepository.findById(id);
        if(oq.isPresent()) {
            Question question = oq.get();
            answerService.create(question, content);
            return "redirect:/question/detail/%d".formatted(id);
        }
        throw new DataNotFoundException("question not found");
    }
}
