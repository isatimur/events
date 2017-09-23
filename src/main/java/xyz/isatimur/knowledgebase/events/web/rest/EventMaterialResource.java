package xyz.isatimur.knowledgebase.events.web.rest;

import com.codahale.metrics.annotation.Timed;
import xyz.isatimur.knowledgebase.events.domain.EventMaterial;

import xyz.isatimur.knowledgebase.events.repository.EventMaterialRepository;
import xyz.isatimur.knowledgebase.events.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing EventMaterial.
 */
@RestController
@RequestMapping("/api")
public class EventMaterialResource {

    private final Logger log = LoggerFactory.getLogger(EventMaterialResource.class);

    private static final String ENTITY_NAME = "eventMaterial";

    private final EventMaterialRepository eventMaterialRepository;

    public EventMaterialResource(EventMaterialRepository eventMaterialRepository) {
        this.eventMaterialRepository = eventMaterialRepository;
    }

    /**
     * POST  /event-materials : Create a new eventMaterial.
     *
     * @param eventMaterial the eventMaterial to create
     * @return the ResponseEntity with status 201 (Created) and with body the new eventMaterial, or with status 400 (Bad Request) if the eventMaterial has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/event-materials")
    @Timed
    public ResponseEntity<EventMaterial> createEventMaterial(@RequestBody EventMaterial eventMaterial) throws URISyntaxException {
        log.debug("REST request to save EventMaterial : {}", eventMaterial);
        if (eventMaterial.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new eventMaterial cannot already have an ID")).body(null);
        }
        EventMaterial result = eventMaterialRepository.save(eventMaterial);
        return ResponseEntity.created(new URI("/api/event-materials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /event-materials : Updates an existing eventMaterial.
     *
     * @param eventMaterial the eventMaterial to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated eventMaterial,
     * or with status 400 (Bad Request) if the eventMaterial is not valid,
     * or with status 500 (Internal Server Error) if the eventMaterial couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/event-materials")
    @Timed
    public ResponseEntity<EventMaterial> updateEventMaterial(@RequestBody EventMaterial eventMaterial) throws URISyntaxException {
        log.debug("REST request to update EventMaterial : {}", eventMaterial);
        if (eventMaterial.getId() == null) {
            return createEventMaterial(eventMaterial);
        }
        EventMaterial result = eventMaterialRepository.save(eventMaterial);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, eventMaterial.getId().toString()))
            .body(result);
    }

    /**
     * GET  /event-materials : get all the eventMaterials.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of eventMaterials in body
     */
    @GetMapping("/event-materials")
    @Timed
    public List<EventMaterial> getAllEventMaterials() {
        log.debug("REST request to get all EventMaterials");
        return eventMaterialRepository.findAll();
        }

    /**
     * GET  /event-materials/:id : get the "id" eventMaterial.
     *
     * @param id the id of the eventMaterial to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the eventMaterial, or with status 404 (Not Found)
     */
    @GetMapping("/event-materials/{id}")
    @Timed
    public ResponseEntity<EventMaterial> getEventMaterial(@PathVariable Long id) {
        log.debug("REST request to get EventMaterial : {}", id);
        EventMaterial eventMaterial = eventMaterialRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(eventMaterial));
    }

    /**
     * DELETE  /event-materials/:id : delete the "id" eventMaterial.
     *
     * @param id the id of the eventMaterial to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/event-materials/{id}")
    @Timed
    public ResponseEntity<Void> deleteEventMaterial(@PathVariable Long id) {
        log.debug("REST request to delete EventMaterial : {}", id);
        eventMaterialRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
