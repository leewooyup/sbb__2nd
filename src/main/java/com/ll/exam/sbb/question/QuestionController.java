package com.ll.exam.sbb.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    @RequestMapping("/question/list")
    //@ResponseBody
    // @RequestBody가 없으면 resources/templates/question_list.html 파일을 뷰로 삼는다.
    public String list(Model model) {
        List<Question> questionList = questionService.getList();

        model.addAttribute("questionList", questionList);

        return "question_list";
    }

    @RequestMapping("/question/detail/{id}")
    public String detail(Model model, @PathVariable int id) {
        Question question = questionService.getQuestioin(id);

        model.addAttribute("question", question);
        return "question_detail";
    }
}
