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

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository revisionRepository;

    private final ReviewMapper reviewMapper;

    private final ReviewSearchRepository reviewSearchRepository;

    private final MessagingService messagingService;

    public ReviewServiceImpl(
            ReviewRepository revisionRepository,
            ReviewMapper reviewMapper,
            ReviewSearchRepository reviewSearchRepository,
            MessagingService messagingService) {
        this.revisionRepository = revisionRepository;
        this.reviewMapper = reviewMapper;
        this.reviewSearchRepository = reviewSearchRepository;
        this.messagingService = messagingService;
    }

    @Override
    public ReviewDTO save(ReviewDTO reviewDTO) {
        log.debug("Request to save Review : {}", reviewDTO);
        Review review = reviewMapper.toEntity(reviewDTO);
        review = revisionRepository.save(review);
        ReviewDTO result = reviewMapper.toDto(review);
        messagingService.send(result);
        return result;
    }

    @Override
    public Optional<ReviewDTO> partialUpdate(ReviewDTO reviewDTO) {
        log.debug("Request to partially update Review : {}", reviewDTO);

        return revisionRepository
                .findById(reviewDTO.getId())
                .map(existingReview -> {
                    reviewMapper.partialUpdate(existingReview, reviewDTO);
                    messagingService.send(reviewDTO);
                    return existingReview;
                })
                .map(revisionRepository::save)
                .map(reviewMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reviews");
        return reviewSearchRepository.findAll(pageable).map(reviewMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewDTO> findOne(Long id) {
        log.debug("Request to get Review : {}", id);
        return reviewSearchRepository.findById(id).map(reviewMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewDTO> findByProductId(Long id) {
        log.debug("Request to get Review : {}", id);
        return revisionRepository.findByProductId(id).map(reviewMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Review : {}", id);
        revisionRepository.deleteById(id);
//        productSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Reviews for query {}", query);
        return reviewSearchRepository.search(queryStringQuery(query), pageable).map(reviewMapper::toDto);
    }

}
