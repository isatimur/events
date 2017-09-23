package xyz.isatimur.knowledgebase.events.web.rest;

import xyz.isatimur.knowledgebase.events.EventsApp;

import xyz.isatimur.knowledgebase.events.domain.EventMaterial;
import xyz.isatimur.knowledgebase.events.repository.EventMaterialRepository;
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

import xyz.isatimur.knowledgebase.events.domain.enumeration.EventMaterialType;
/**
 * Test class for the EventMaterialResource REST controller.
 *
 * @see EventMaterialResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventsApp.class)
public class EventMaterialResourceIntTest {

    private static final ZonedDateTime DEFAULT_UPLOADED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPLOADED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final EventMaterialType DEFAULT_TYPE = EventMaterialType.PRESENTATION;
    private static final EventMaterialType UPDATED_TYPE = EventMaterialType.VIDEO;

    @Autowired
    private EventMaterialRepository eventMaterialRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEventMaterialMockMvc;

    private EventMaterial eventMaterial;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventMaterialResource eventMaterialResource = new EventMaterialResource(eventMaterialRepository);
        this.restEventMaterialMockMvc = MockMvcBuilders.standaloneSetup(eventMaterialResource)
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
    public static EventMaterial createEntity(EntityManager em) {
        EventMaterial eventMaterial = new EventMaterial()
            .uploaded(DEFAULT_UPLOADED)
            .url(DEFAULT_URL)
            .text(DEFAULT_TEXT)
            .type(DEFAULT_TYPE);
        return eventMaterial;
    }

    @Before
    public void initTest() {
        eventMaterial = createEntity(em);
    }

    @Test
    @Transactional
    public void createEventMaterial() throws Exception {
        int databaseSizeBeforeCreate = eventMaterialRepository.findAll().size();

        // Create the EventMaterial
        restEventMaterialMockMvc.perform(post("/api/event-materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventMaterial)))
            .andExpect(status().isCreated());

        // Validate the EventMaterial in the database
        List<EventMaterial> eventMaterialList = eventMaterialRepository.findAll();
        assertThat(eventMaterialList).hasSize(databaseSizeBeforeCreate + 1);
        EventMaterial testEventMaterial = eventMaterialList.get(eventMaterialList.size() - 1);
        assertThat(testEventMaterial.getUploaded()).isEqualTo(DEFAULT_UPLOADED);
        assertThat(testEventMaterial.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testEventMaterial.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testEventMaterial.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createEventMaterialWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventMaterialRepository.findAll().size();

        // Create the EventMaterial with an existing ID
        eventMaterial.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMaterialMockMvc.perform(post("/api/event-materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventMaterial)))
            .andExpect(status().isBadRequest());

        // Validate the EventMaterial in the database
        List<EventMaterial> eventMaterialList = eventMaterialRepository.findAll();
        assertThat(eventMaterialList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEventMaterials() throws Exception {
        // Initialize the database
        eventMaterialRepository.saveAndFlush(eventMaterial);

        // Get all the eventMaterialList
        restEventMaterialMockMvc.perform(get("/api/event-materials?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventMaterial.getId().intValue())))
            .andExpect(jsonPath("$.[*].uploaded").value(hasItem(sameInstant(DEFAULT_UPLOADED))))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getEventMaterial() throws Exception {
        // Initialize the database
        eventMaterialRepository.saveAndFlush(eventMaterial);

        // Get the eventMaterial
        restEventMaterialMockMvc.perform(get("/api/event-materials/{id}", eventMaterial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eventMaterial.getId().intValue()))
            .andExpect(jsonPath("$.uploaded").value(sameInstant(DEFAULT_UPLOADED)))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEventMaterial() throws Exception {
        // Get the eventMaterial
        restEventMaterialMockMvc.perform(get("/api/event-materials/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEventMaterial() throws Exception {
        // Initialize the database
        eventMaterialRepository.saveAndFlush(eventMaterial);
        int databaseSizeBeforeUpdate = eventMaterialRepository.findAll().size();

        // Update the eventMaterial
        EventMaterial updatedEventMaterial = eventMaterialRepository.findOne(eventMaterial.getId());
        updatedEventMaterial
            .uploaded(UPDATED_UPLOADED)
            .url(UPDATED_URL)
            .text(UPDATED_TEXT)
            .type(UPDATED_TYPE);

        restEventMaterialMockMvc.perform(put("/api/event-materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEventMaterial)))
            .andExpect(status().isOk());

        // Validate the EventMaterial in the database
        List<EventMaterial> eventMaterialList = eventMaterialRepository.findAll();
        assertThat(eventMaterialList).hasSize(databaseSizeBeforeUpdate);
        EventMaterial testEventMaterial = eventMaterialList.get(eventMaterialList.size() - 1);
        assertThat(testEventMaterial.getUploaded()).isEqualTo(UPDATED_UPLOADED);
        assertThat(testEventMaterial.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testEventMaterial.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testEventMaterial.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingEventMaterial() throws Exception {
        int databaseSizeBeforeUpdate = eventMaterialRepository.findAll().size();

        // Create the EventMaterial

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEventMaterialMockMvc.perform(put("/api/event-materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventMaterial)))
            .andExpect(status().isCreated());

        // Validate the EventMaterial in the database
        List<EventMaterial> eventMaterialList = eventMaterialRepository.findAll();
        assertThat(eventMaterialList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEventMaterial() throws Exception {
        // Initialize the database
        eventMaterialRepository.saveAndFlush(eventMaterial);
        int databaseSizeBeforeDelete = eventMaterialRepository.findAll().size();

        // Get the eventMaterial
        restEventMaterialMockMvc.perform(delete("/api/event-materials/{id}", eventMaterial.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EventMaterial> eventMaterialList = eventMaterialRepository.findAll();
        assertThat(eventMaterialList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventMaterial.class);
        EventMaterial eventMaterial1 = new EventMaterial();
        eventMaterial1.setId(1L);
        EventMaterial eventMaterial2 = new EventMaterial();
        eventMaterial2.setId(eventMaterial1.getId());
        assertThat(eventMaterial1).isEqualTo(eventMaterial2);
        eventMaterial2.setId(2L);
        assertThat(eventMaterial1).isNotEqualTo(eventMaterial2);
        eventMaterial1.setId(null);
        assertThat(eventMaterial1).isNotEqualTo(eventMaterial2);
    }
}
