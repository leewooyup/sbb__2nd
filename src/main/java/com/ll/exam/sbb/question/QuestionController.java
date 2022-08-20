package com.ll.exam.sbb.question;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QuestionController {
    @RequestMapping("/question/list")
    //@ResponseBody
    // @RequestBody가 없으면 resources/templates/question_list.html 파일을 뷰로 삼는다.
    public String list() {
        return "question_list";
    }
}
