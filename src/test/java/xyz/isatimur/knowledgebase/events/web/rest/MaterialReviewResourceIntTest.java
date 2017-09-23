package xyz.isatimur.knowledgebase.events.web.rest;

import xyz.isatimur.knowledgebase.events.EventsApp;

import xyz.isatimur.knowledgebase.events.domain.MaterialReview;
import xyz.isatimur.knowledgebase.events.repository.MaterialReviewRepository;
import xyz.isatimur.knowledgebase.events.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static xyz.isatimur.knowledgebase.events.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MaterialReviewResource REST controller.
 *
 * @see MaterialReviewResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventsApp.class)
public class MaterialReviewResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    @Autowired
    private MaterialReviewRepository materialReviewRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMaterialReviewMockMvc;

    private MaterialReview materialReview;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MaterialReviewResource materialReviewResource = new MaterialReviewResource(materialReviewRepository);
        this.restMaterialReviewMockMvc = MockMvcBuilders.standaloneSetup(materialReviewResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialReview createEntity(EntityManager em) {
        MaterialReview materialReview = new MaterialReview()
            .created(DEFAULT_CREATED)
            .author(DEFAULT_AUTHOR)
            .text(DEFAULT_TEXT);
        return materialReview;
    }

    @Before
    public void initTest() {
        materialReview = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaterialReview() throws Exception {
        int databaseSizeBeforeCreate = materialReviewRepository.findAll().size();

        // Create the MaterialReview
        restMaterialReviewMockMvc.perform(post("/api/material-reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialReview)))
            .andExpect(status().isCreated());

        // Validate the MaterialReview in the database
        List<MaterialReview> materialReviewList = materialReviewRepository.findAll();
        assertThat(materialReviewList).hasSize(databaseSizeBeforeCreate + 1);
        MaterialReview testMaterialReview = materialReviewList.get(materialReviewList.size() - 1);
        assertThat(testMaterialReview.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testMaterialReview.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testMaterialReview.getText()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    public void createMaterialReviewWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = materialReviewRepository.findAll().size();

        // Create the MaterialReview with an existing ID
        materialReview.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialReviewMockMvc.perform(post("/api/material-reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialReview)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialReview in the database
        List<MaterialReview> materialReviewList = materialReviewRepository.findAll();
        assertThat(materialReviewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMaterialReviews() throws Exception {
        // Initialize the database
        materialReviewRepository.saveAndFlush(materialReview);

        // Get all the materialReviewList
        restMaterialReviewMockMvc.perform(get("/api/material-reviews?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialReview.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getMaterialReview() throws Exception {
        // Initialize the database
        materialReviewRepository.saveAndFlush(materialReview);

        // Get the materialReview
        restMaterialReviewMockMvc.perform(get("/api/material-reviews/{id}", materialReview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(materialReview.getId().intValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMaterialReview() throws Exception {
        // Get the materialReview
        restMaterialReviewMockMvc.perform(get("/api/material-reviews/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaterialReview() throws Exception {
        // Initialize the database
        materialReviewRepository.saveAndFlush(materialReview);
        int databaseSizeBeforeUpdate = materialReviewRepository.findAll().size();

        // Update the materialReview
        MaterialReview updatedMaterialReview = materialReviewRepository.findOne(materialReview.getId());
        updatedMaterialReview
            .created(UPDATED_CREATED)
            .author(UPDATED_AUTHOR)
            .text(UPDATED_TEXT);

        restMaterialReviewMockMvc.perform(put("/api/material-reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMaterialReview)))
            .andExpect(status().isOk());

        // Validate the MaterialReview in the database
        List<MaterialReview> materialReviewList = materialReviewRepository.findAll();
        assertThat(materialReviewList).hasSize(databaseSizeBeforeUpdate);
        MaterialReview testMaterialReview = materialReviewList.get(materialReviewList.size() - 1);
        assertThat(testMaterialReview.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testMaterialReview.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testMaterialReview.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void updateNonExistingMaterialReview() throws Exception {
        int databaseSizeBeforeUpdate = materialReviewRepository.findAll().size();

        // Create the MaterialReview

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMaterialReviewMockMvc.perform(put("/api/material-reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialReview)))
            .andExpect(status().isCreated());

        // Validate the MaterialReview in the database
        List<MaterialReview> materialReviewList = materialReviewRepository.findAll();
        assertThat(materialReviewList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMaterialReview() throws Exception {
        // Initialize the database
        materialReviewRepository.saveAndFlush(materialReview);
        int databaseSizeBeforeDelete = materialReviewRepository.findAll().size();

        // Get the materialReview
        restMaterialReviewMockMvc.perform(delete("/api/material-reviews/{id}", materialReview.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MaterialReview> materialReviewList = materialReviewRepository.findAll();
        assertThat(materialReviewList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialReview.class);
        MaterialReview materialReview1 = new MaterialReview();
        materialReview1.setId(1L);
        MaterialReview materialReview2 = new MaterialReview();
        materialReview2.setId(materialReview1.getId());
        assertThat(materialReview1).isEqualTo(materialReview2);
        materialReview2.setId(2L);
        assertThat(materialReview1).isNotEqualTo(materialReview2);
        materialReview1.setId(null);
        assertThat(materialReview1).isNotEqualTo(materialReview2);
    }
}
