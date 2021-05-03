package com.sample.reviewservice.service.impl;

import com.sample.reviewservice.domain.Review;
import com.sample.reviewservice.repository.ReviewRepository;
import com.sample.reviewservice.repository.search.ReviewSearchRepository;
import com.sample.reviewservice.service.MessagingService;
import com.sample.reviewservice.service.ReviewService;
import com.sample.reviewservice.service.dto.ReviewDTO;
import com.sample.reviewservice.service.mapper.ReviewMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository reviewRepository;

    private final ReviewMapper reviewMapper;

    private final ReviewSearchRepository reviewSearchRepository;

    private final MessagingService messagingService;

    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            ReviewMapper reviewMapper,
            ReviewSearchRepository reviewSearchRepository,
            MessagingService messagingService) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.reviewSearchRepository = reviewSearchRepository;
        this.messagingService = messagingService;
    }

    @Override
    public ReviewDTO save(ReviewDTO reviewDTO) {
        log.debug("Request to save Review : {}", reviewDTO);
        Review review = reviewMapper.toEntity(reviewDTO);
        review = reviewRepository.save(review);
        ReviewDTO result = reviewMapper.toDto(review);
        messagingService.send(result);
        return result;
    }

    @Override
    public Optional<ReviewDTO> partialUpdate(ReviewDTO reviewDTO) {
        log.debug("Request to partially update Review : {}", reviewDTO);

        return reviewRepository
                .findById(reviewDTO.getId())
                .map(existingReview -> {
                    reviewMapper.partialUpdate(existingReview, reviewDTO);
                    return existingReview;
                })
                .map(review -> {
                    review = reviewRepository.save(review);
                    messagingService.send(reviewMapper.toDto(review));
                    return review;
                })
                .map(reviewMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reviews");
        return reviewSearchRepository.findByActive(pageable).map(reviewMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewDTO> findOne(Long id) {
        log.debug("Request to get Review : {}", id);
        return reviewSearchRepository.findByIdAndActive(id, true).map(reviewMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewDTO> findByProductId(Long id) {
        log.debug("Request to get Review : {}", id);
        return reviewRepository.findByProductIdAndActive(id, true).map(reviewMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Review : {}", id);
        reviewRepository.deleteById(id);
        Optional<Review> review = reviewSearchRepository.findById(id);
        review.ifPresent(p -> {
            p.setActive(false);
            messagingService.send(reviewMapper.toDto(p));
        });
    }

//    @Override
//    @Transactional(readOnly = true)
//    public Page<ReviewDTO> search(String query, Pageable pageable) {
//        log.debug("Request to search for a page of Reviews for query {}", query);
//        return reviewSearchRepository.search(queryStringQuery(query), pageable).map(reviewMapper::toDto);
//    }

}
