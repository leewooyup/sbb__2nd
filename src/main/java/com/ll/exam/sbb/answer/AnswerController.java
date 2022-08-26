package com.ll.exam.sbb.answer;

import com.ll.exam.sbb.DataNotFoundException;
import com.ll.exam.sbb.question.Question;
import com.ll.exam.sbb.question.QuestionRepository;
import com.ll.exam.sbb.user.SiteUser;
import com.ll.exam.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

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
    public String detail(Principal principal, Model model, @PathVariable int id, @Valid AnswerForm answerForm, BindingResult bindingResult) {
        Optional<Question> oq = questionRepository.findById(id);

        if(oq.isPresent()) {
            Question question = oq.get();

            if(bindingResult.hasErrors()) {
                model.addAttribute("question", question);
                return "question_detail";
            }

            SiteUser siteUser = userService.getUser(principal.getName());

            answerService.create(question, answerForm.getContent(), siteUser);
            return "redirect:/question/detail/%d".formatted(id);
        }

        throw new DataNotFoundException("question not found");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(Principal principal, @PathVariable("id") Integer id,  AnswerForm answerForm) {
        Answer answer = answerService.getAnswer(id);
        if(!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerForm.setContent(answer.getContent());

        return "answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@PathVariable("id") Integer id, Principal principal, @Valid AnswerForm answerForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "answer_form";
        }

        Answer answer = answerService.getAnswer(id);

        if(!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerService.modify(answer, answerForm.getContent());

        return "redirect:/question/detail/%d".formatted(answer.getQuestion().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = answerService.getAnswer(id);

        if(!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        answerService.delete(answer);

        return "redirect:/question/detail/%d".formatted(answer.getQuestion().getId());
    }
}
