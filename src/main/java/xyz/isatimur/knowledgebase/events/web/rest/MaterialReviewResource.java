package xyz.isatimur.knowledgebase.events.web.rest;

import com.codahale.metrics.annotation.Timed;
import xyz.isatimur.knowledgebase.events.domain.MaterialReview;

import xyz.isatimur.knowledgebase.events.repository.MaterialReviewRepository;
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
 * REST controller for managing MaterialReview.
 */
@RestController
@RequestMapping("/api")
public class MaterialReviewResource {

    private final Logger log = LoggerFactory.getLogger(MaterialReviewResource.class);

    private static final String ENTITY_NAME = "materialReview";

    private final MaterialReviewRepository materialReviewRepository;

    public MaterialReviewResource(MaterialReviewRepository materialReviewRepository) {
        this.materialReviewRepository = materialReviewRepository;
    }

    /**
     * POST  /material-reviews : Create a new materialReview.
     *
     * @param materialReview the materialReview to create
     * @return the ResponseEntity with status 201 (Created) and with body the new materialReview, or with status 400 (Bad Request) if the materialReview has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/material-reviews")
    @Timed
    public ResponseEntity<MaterialReview> createMaterialReview(@RequestBody MaterialReview materialReview) throws URISyntaxException {
        log.debug("REST request to save MaterialReview : {}", materialReview);
        if (materialReview.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new materialReview cannot already have an ID")).body(null);
        }
        MaterialReview result = materialReviewRepository.save(materialReview);
        return ResponseEntity.created(new URI("/api/material-reviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /material-reviews : Updates an existing materialReview.
     *
     * @param materialReview the materialReview to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated materialReview,
     * or with status 400 (Bad Request) if the materialReview is not valid,
     * or with status 500 (Internal Server Error) if the materialReview couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/material-reviews")
    @Timed
    public ResponseEntity<MaterialReview> updateMaterialReview(@RequestBody MaterialReview materialReview) throws URISyntaxException {
        log.debug("REST request to update MaterialReview : {}", materialReview);
        if (materialReview.getId() == null) {
            return createMaterialReview(materialReview);
        }
        MaterialReview result = materialReviewRepository.save(materialReview);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, materialReview.getId().toString()))
            .body(result);
    }

    /**
     * GET  /material-reviews : get all the materialReviews.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of materialReviews in body
     */
    @GetMapping("/material-reviews")
    @Timed
    public List<MaterialReview> getAllMaterialReviews() {
        log.debug("REST request to get all MaterialReviews");
        return materialReviewRepository.findAll();
        }

    /**
     * GET  /material-reviews/:id : get the "id" materialReview.
     *
     * @param id the id of the materialReview to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the materialReview, or with status 404 (Not Found)
     */
    @GetMapping("/material-reviews/{id}")
    @Timed
    public ResponseEntity<MaterialReview> getMaterialReview(@PathVariable Long id) {
        log.debug("REST request to get MaterialReview : {}", id);
        MaterialReview materialReview = materialReviewRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(materialReview));
    }

    /**
     * DELETE  /material-reviews/:id : delete the "id" materialReview.
     *
     * @param id the id of the materialReview to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/material-reviews/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaterialReview(@PathVariable Long id) {
        log.debug("REST request to delete MaterialReview : {}", id);
        materialReviewRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
