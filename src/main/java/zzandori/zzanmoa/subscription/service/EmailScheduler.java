package zzandori.zzanmoa.subscription.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import zzandori.zzanmoa.bargainboard.entity.BargainBoard;
import zzandori.zzanmoa.bargainboard.repository.BargainBoardRepository;
import zzandori.zzanmoa.subscription.entity.Subscription;
import zzandori.zzanmoa.subscription.repository.SubscriptionRepository;

@Component
@RequiredArgsConstructor
public class EmailScheduler {

    private final BargainBoardRepository bargainBoardRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionService subscriptionService;

    private static final Logger logger = LoggerFactory.getLogger(EmailScheduler.class);

    //@Scheduled(cron = "0 0 10 * * ?", zone = "Asia/Seoul") // 매일 오전 10시에 실행
    public void sendEmailsForPostsLastTwoMonths() {
        LocalDate twoMonthsAgo = LocalDate.now().minusMonths(3);
        List<BargainBoard> postsLastTwoMonths = bargainBoardRepository.findByCreatedAtAfter(twoMonthsAgo);

        logger.info(twoMonthsAgo + "이후 할인소식 찾기");
        logger.info("해당 게시글 갯수 : " + postsLastTwoMonths.size());

        for (BargainBoard post : postsLastTwoMonths) {
            List<Subscription> subscriptions = subscriptionRepository.findByDistrict(post.getDistrict());
            for (Subscription sub : subscriptions) {
                subscriptionService.sendEmail(sub.getEmail(), "내가 구독한 자치구의 할인 소식 업데이트: " + post.getTitle(), post.getContent(), sub, post);
            }
        }
    }

}

