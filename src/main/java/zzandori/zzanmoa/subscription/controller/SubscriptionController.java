package zzandori.zzanmoa.subscription.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.subscription.dto.SubscriptionDTO;
import zzandori.zzanmoa.subscription.dto.SubscriptionStatusDTO;
import zzandori.zzanmoa.subscription.service.EmailScheduler;
import zzandori.zzanmoa.subscription.service.SubscriptionService;
import zzandori.zzanmoa.subscription.service.ValidatorService;

@Tag(name = "SubscriptionController", description = "서울시 할인 소식 구독하기 기능 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/subscription")
public class SubscriptionController {

    private final ValidatorService validatorService;
    private final SubscriptionService subscriptionService;
    private final EmailScheduler emailScheduler;

    @PostMapping
    public ResponseEntity<?> subscribe(@Valid @RequestBody SubscriptionDTO subscriptionDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validatorService.processBindingResultErrors(bindingResult);
        }

        SubscriptionStatusDTO status = subscriptionService.subscribe(subscriptionDto);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/send-test")
    public void sendEmail(){
        emailScheduler.sendEmailsForPostsLastTwoMonths();
    }
}
