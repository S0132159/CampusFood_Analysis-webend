package com.campusfood.controller;

import com.campusfood.entity.Checkin;
import com.campusfood.entity.Review;
import com.campusfood.entity.Stall;
import com.campusfood.service.CampusFoodService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CampusFoodController {

    private final CampusFoodService service;

    public CampusFoodController(CampusFoodService service) {
        this.service = service;
    }

    @GetMapping("/stalls")
    public List<Stall> getStalls() {
        return service.getAllStalls();
    }

    @PostMapping("/checkins")
    public Checkin createCheckin(@RequestBody Checkin checkin) {
        return service.saveCheckin(checkin);
    }

    @GetMapping("/stalls/{id}/reviews")
    public List<Review> getStallReviews(@PathVariable Long id) {
        return service.getReviewsByStallId(id);
    }

    @GetMapping("/stalls/{id}/crowd-level")
    public Map<String, String> getCrowdLevel(@PathVariable Long id) {
        String level = service.getCrowdLevel(id);
        return Map.of("stallId", String.valueOf(id), "crowdLevel", level);
    }

    @PostMapping("/reviews")
    public Review createReview(@RequestBody Review review) {
        return service.processAndSaveReview(review);
    }
}
