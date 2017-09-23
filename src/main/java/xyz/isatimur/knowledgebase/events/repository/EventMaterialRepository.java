package xyz.isatimur.knowledgebase.events.repository;

import xyz.isatimur.knowledgebase.events.domain.EventMaterial;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EventMaterial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventMaterialRepository extends JpaRepository<EventMaterial, Long> {

}
