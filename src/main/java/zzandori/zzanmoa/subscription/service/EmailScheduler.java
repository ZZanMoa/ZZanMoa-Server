package zzandori.zzanmoa.subscription.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    private static final String SITE_URL = "https://zzanmoa.vercel.app/";

    @Scheduled(cron = "0 0 12 * * ?", zone = "Asia/Seoul")
    public void scheduleEmailTasks() {
        LocalDate twoMonthsAgo = LocalDate.now().minusMonths(3);
        List<BargainBoard> recentPosts = findRecentPosts(twoMonthsAgo);
        logger.info("Found " + recentPosts.size() + " posts since " + twoMonthsAgo);
        recentPosts.forEach(this::processPostForEmailing);
    }

    private List<BargainBoard> findRecentPosts(LocalDate sinceDate) {
        return bargainBoardRepository.findByCreatedAtAfter(sinceDate);
    }

    private void processPostForEmailing(BargainBoard post) {
        List<Subscription> subscriptions = subscriptionRepository.findByDistrict(post.getDistrict());
        subscriptions.forEach(subscription -> sendEmailToUpdateSubscribers(subscription, post));
    }

    private void sendEmailToUpdateSubscribers(Subscription subscription, BargainBoard post) {
        String emailContent = post.getContent() + "\n자세한 정보는 여기에서 확인하세요: " + SITE_URL;
        subscriptionService.sendEmail(subscription.getEmail(), "내가 구독한 자치구의 할인 소식 업데이트: " + post.getTitle(), emailContent, subscription, post);
    }

}
