package com.sample.reviewservice.service;

import com.sample.reviewservice.service.dto.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ReviewService {
    /**
     * Save a review.
     *
     * @param reviewDTO the entity to save.
     * @return the persisted entity.
     */
    ReviewDTO save(ReviewDTO reviewDTO);

    /**
     * Partially updates a review.
     *
     * @param reviewDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReviewDTO> partialUpdate(ReviewDTO reviewDTO);

    /**
     * Get all the reviews.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReviewDTO> findAll(Pageable pageable);

    /**
     * Get the "id" review.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReviewDTO> findOne(Long id);

    /**
     * Get the review by product id.
     *
     * @param id the id of the product.
     * @return the entity.
     */
    Optional<ReviewDTO> findByProductId(Long id);

    /**
     * Delete the "id" review.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the review corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
//    Page<ReviewDTO> search(String query, Pageable pageable);
}
