package xyz.isatimur.knowledgebase.events.repository;

import xyz.isatimur.knowledgebase.events.domain.MaterialReview;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MaterialReview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialReviewRepository extends JpaRepository<MaterialReview, Long> {

}
