package com.ll.exam.sbb.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/question")
@Controller
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    @RequestMapping("/list")
    //@ResponseBody
    // @RequestBody가 없으면 resources/templates/question_list.html 파일을 뷰로 삼는다.
    public String list(Model model) {
        List<Question> questionList = questionService.getList();

        model.addAttribute("questionList", questionList);

        return "question_list";
    }

    @RequestMapping("/detail/{id}")
    public String detail(Model model, @PathVariable int id) {
        Question question = questionService.getQuestion(id);

        model.addAttribute("question", question);
        return "question_detail";
    }

    @GetMapping("/create")
    public String questionCreate() {
        return "question_form";
    }

    @PostMapping("/create")
    public String questionCreate(Model model, QuestionForm questionForm) {
        if(questionForm.getSubject() == null || questionForm.getSubject().trim().length() == 0) {
            model.addAttribute("errorMsg", "제목을 입력해주세요");
            return "question_form";
        }

        if(questionForm.getContent() == null || questionForm.getContent().trim().length() == 0) {
            model.addAttribute("errorMsg", "내용을 입력해주세요");
            return "question_form";
        }
        questionService.create(questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/list";
    }
}
