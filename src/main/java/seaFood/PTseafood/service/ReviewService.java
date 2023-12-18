package seaFood.PTseafood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.dto.ReviewRequest;
import seaFood.PTseafood.entity.Product;
import seaFood.PTseafood.entity.Review;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.repository.IReviewRepository;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private IReviewRepository reviewRepository;
    public Review createReview(User user , ReviewRequest reviewRequest) {
        Review review = new Review();
        review.setContent(reviewRequest.getContent());
        review.setProduct(reviewRequest.getProduct());
        review.setRating(reviewRequest.getRating());
        review.setUser(user);
        return reviewRepository.save(review);
    }
    public List<Review> getReviewsByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    public double calculateAverageRating(Product product) {
        List<Review> reviews = reviewRepository.findByProduct(product);
        int totalRating = 0;
        int reviewCount = reviews.size();
        for (Review review : reviews) {
            totalRating += review.getRating();
        }
        if (reviewCount > 0) {
            return (double) totalRating / reviewCount;
        } else {
            return 0; // Trả về 0 nếu không có đánh giá nào
        }
    }
    public long countReviewsForProduct(Product product) {
        return reviewRepository.countByProduct(product);
    }

}
