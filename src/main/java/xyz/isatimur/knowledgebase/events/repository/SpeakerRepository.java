package xyz.isatimur.knowledgebase.events.repository;

import xyz.isatimur.knowledgebase.events.domain.Speaker;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Speaker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpeakerRepository extends JpaRepository<Speaker, Long> {
    @Query("select distinct speaker from Speaker speaker left join fetch speaker.tags")
    List<Speaker> findAllWithEagerRelationships();

    @Query("select speaker from Speaker speaker left join fetch speaker.tags where speaker.id =:id")
    Speaker findOneWithEagerRelationships(@Param("id") Long id);

}
