package com.ll.exam.sbb.answer;

import com.ll.exam.sbb.DataNotFoundException;
import com.ll.exam.sbb.question.Question;
import com.ll.exam.sbb.question.QuestionRepository;
import com.ll.exam.sbb.user.SiteUser;
import com.ll.exam.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RequestMapping("/answer")
@Controller
@RequiredArgsConstructor
public class AnswerController {
    private final QuestionRepository questionRepository;
    private final AnswerService answerService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String detail(Principal principal, Model model, @PathVariable int id, @Valid AnswerForm answerform, BindingResult bindingResult) {
        Optional<Question> oq = questionRepository.findById(id);

        if(oq.isPresent()) {
            Question question = oq.get();

            if(bindingResult.hasErrors()) {
                model.addAttribute("question", question);
                return "question_detail";
            }

            SiteUser siteUser = userService.getUser(principal.getName());

            answerService.create(question, answerform.getContent(), siteUser);
            return "redirect:/question/detail/%d".formatted(id);
        }

        throw new DataNotFoundException("question not found");
    }
}
