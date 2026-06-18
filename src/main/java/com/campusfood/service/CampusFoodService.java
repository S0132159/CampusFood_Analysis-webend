package com.campusfood.service;

import com.campusfood.entity.Checkin;
import com.campusfood.entity.Review;
import com.campusfood.entity.Stall;
import com.campusfood.repository.CheckinRepository;
import com.campusfood.repository.ReviewRepository;
import com.campusfood.repository.StallRepository;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.List;

@Service
public class CampusFoodService {

    private final StallRepository stallRepository;
    private final CheckinRepository checkinRepository;
    private final ReviewRepository reviewRepository;

    public CampusFoodService(StallRepository stallRepository, CheckinRepository checkinRepository, ReviewRepository reviewRepository) {
        this.stallRepository = stallRepository;
        this.checkinRepository = checkinRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<Stall> getAllStalls() { return stallRepository.findAll(); }
    public List<Review> getReviewsByStallId(Long stallId) { return reviewRepository.findByStallId(stallId); }
    public Checkin saveCheckin(Checkin checkin) { return checkinRepository.save(checkin); }

    public String getCrowdLevel(Long stallId) {
        int currentHour = LocalTime.now().getHour();
        boolean isPeakTime = (currentHour >= 11 && currentHour <= 13) || (currentHour >= 17 && currentHour <= 19);
        
        Double avgCheckins = checkinRepository.getAverageCheckinsByHour(stallId, currentHour);
        if (avgCheckins == null) avgCheckins = 0.0;

        if (isPeakTime) {
            if (avgCheckins >= 20.0) return "非常忙碌 (預計需等候 15 分鐘以上)";
            else if (avgCheckins >= 10.0) return "忙碌 (預計需等候 5-10 分鐘)";
            else return "普通 (無須久候)";
        } else {
            if (avgCheckins >= 5.0) return "普通 (有些許人潮)";
            else return "空閒 (可直接點餐)";
        }
    }

    public Review processAndSaveReview(Review review) {
        String content = review.getContent() == null ? "" : review.getContent();
        int posScore = 0;
        int negScore = 0;

        if (content.contains("好吃")) posScore++;
        if (content.contains("親切")) posScore++;
        if (content.contains("划算")) posScore++;

        if (content.contains("等太久")) negScore++;
        if (content.contains("態度不好")) negScore++;
        if (content.contains("份量變少")) negScore++;

        if (posScore > negScore) {
            if (posScore >= 2 && negScore == 0) review.setSentiment("positive (極力推薦)");
            else review.setSentiment("positive");
        } else if (negScore > posScore) {
            if (negScore >= 2) review.setSentiment("negative (強烈負評)");
            else review.setSentiment("negative");
        } else {
            if (content.length() > 30 && posScore > 0) review.setSentiment("positive (偏向正面)");
            else review.setSentiment("neutral");
        }
        return reviewRepository.save(review);
    }
}
