package xyz.isatimur.knowledgebase.events.web.rest;

import xyz.isatimur.knowledgebase.events.EventsApp;

import xyz.isatimur.knowledgebase.events.domain.Speaker;
import xyz.isatimur.knowledgebase.events.repository.SpeakerRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import xyz.isatimur.knowledgebase.events.domain.enumeration.SpeakerStatusType;
/**
 * Test class for the SpeakerResource REST controller.
 *
 * @see SpeakerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventsApp.class)
public class SpeakerResourceIntTest {

    private static final String DEFAULT_FIO = "AAAAAAAAAA";
    private static final String UPDATED_FIO = "BBBBBBBBBB";

    private static final String DEFAULT_BIOGRAPHY = "AAAAAAAAAA";
    private static final String UPDATED_BIOGRAPHY = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_URL = "BBBBBBBBBB";

    private static final Long DEFAULT_COMPANY_ID = 1L;
    private static final Long UPDATED_COMPANY_ID = 2L;

    private static final SpeakerStatusType DEFAULT_STATUS = SpeakerStatusType.PUBLISHED;
    private static final SpeakerStatusType UPDATED_STATUS = SpeakerStatusType.UNPUBLISHED;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSpeakerMockMvc;

    private Speaker speaker;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SpeakerResource speakerResource = new SpeakerResource(speakerRepository);
        this.restSpeakerMockMvc = MockMvcBuilders.standaloneSetup(speakerResource)
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
    public static Speaker createEntity(EntityManager em) {
        Speaker speaker = new Speaker()
            .fio(DEFAULT_FIO)
            .biography(DEFAULT_BIOGRAPHY)
            .logoUrl(DEFAULT_LOGO_URL)
            .companyId(DEFAULT_COMPANY_ID)
            .status(DEFAULT_STATUS);
        return speaker;
    }

    @Before
    public void initTest() {
        speaker = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpeaker() throws Exception {
        int databaseSizeBeforeCreate = speakerRepository.findAll().size();

        // Create the Speaker
        restSpeakerMockMvc.perform(post("/api/speakers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(speaker)))
            .andExpect(status().isCreated());

        // Validate the Speaker in the database
        List<Speaker> speakerList = speakerRepository.findAll();
        assertThat(speakerList).hasSize(databaseSizeBeforeCreate + 1);
        Speaker testSpeaker = speakerList.get(speakerList.size() - 1);
        assertThat(testSpeaker.getFio()).isEqualTo(DEFAULT_FIO);
        assertThat(testSpeaker.getBiography()).isEqualTo(DEFAULT_BIOGRAPHY);
        assertThat(testSpeaker.getLogoUrl()).isEqualTo(DEFAULT_LOGO_URL);
        assertThat(testSpeaker.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testSpeaker.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createSpeakerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = speakerRepository.findAll().size();

        // Create the Speaker with an existing ID
        speaker.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpeakerMockMvc.perform(post("/api/speakers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(speaker)))
            .andExpect(status().isBadRequest());

        // Validate the Speaker in the database
        List<Speaker> speakerList = speakerRepository.findAll();
        assertThat(speakerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSpeakers() throws Exception {
        // Initialize the database
        speakerRepository.saveAndFlush(speaker);

        // Get all the speakerList
        restSpeakerMockMvc.perform(get("/api/speakers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(speaker.getId().intValue())))
            .andExpect(jsonPath("$.[*].fio").value(hasItem(DEFAULT_FIO.toString())))
            .andExpect(jsonPath("$.[*].biography").value(hasItem(DEFAULT_BIOGRAPHY.toString())))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL.toString())))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getSpeaker() throws Exception {
        // Initialize the database
        speakerRepository.saveAndFlush(speaker);

        // Get the speaker
        restSpeakerMockMvc.perform(get("/api/speakers/{id}", speaker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(speaker.getId().intValue()))
            .andExpect(jsonPath("$.fio").value(DEFAULT_FIO.toString()))
            .andExpect(jsonPath("$.biography").value(DEFAULT_BIOGRAPHY.toString()))
            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL.toString()))
            .andExpect(jsonPath("$.companyId").value(DEFAULT_COMPANY_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSpeaker() throws Exception {
        // Get the speaker
        restSpeakerMockMvc.perform(get("/api/speakers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpeaker() throws Exception {
        // Initialize the database
        speakerRepository.saveAndFlush(speaker);
        int databaseSizeBeforeUpdate = speakerRepository.findAll().size();

        // Update the speaker
        Speaker updatedSpeaker = speakerRepository.findOne(speaker.getId());
        updatedSpeaker
            .fio(UPDATED_FIO)
            .biography(UPDATED_BIOGRAPHY)
            .logoUrl(UPDATED_LOGO_URL)
            .companyId(UPDATED_COMPANY_ID)
            .status(UPDATED_STATUS);

        restSpeakerMockMvc.perform(put("/api/speakers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSpeaker)))
            .andExpect(status().isOk());

        // Validate the Speaker in the database
        List<Speaker> speakerList = speakerRepository.findAll();
        assertThat(speakerList).hasSize(databaseSizeBeforeUpdate);
        Speaker testSpeaker = speakerList.get(speakerList.size() - 1);
        assertThat(testSpeaker.getFio()).isEqualTo(UPDATED_FIO);
        assertThat(testSpeaker.getBiography()).isEqualTo(UPDATED_BIOGRAPHY);
        assertThat(testSpeaker.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
        assertThat(testSpeaker.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testSpeaker.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingSpeaker() throws Exception {
        int databaseSizeBeforeUpdate = speakerRepository.findAll().size();

        // Create the Speaker

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSpeakerMockMvc.perform(put("/api/speakers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(speaker)))
            .andExpect(status().isCreated());

        // Validate the Speaker in the database
        List<Speaker> speakerList = speakerRepository.findAll();
        assertThat(speakerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSpeaker() throws Exception {
        // Initialize the database
        speakerRepository.saveAndFlush(speaker);
        int databaseSizeBeforeDelete = speakerRepository.findAll().size();

        // Get the speaker
        restSpeakerMockMvc.perform(delete("/api/speakers/{id}", speaker.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Speaker> speakerList = speakerRepository.findAll();
        assertThat(speakerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Speaker.class);
        Speaker speaker1 = new Speaker();
        speaker1.setId(1L);
        Speaker speaker2 = new Speaker();
        speaker2.setId(speaker1.getId());
        assertThat(speaker1).isEqualTo(speaker2);
        speaker2.setId(2L);
        assertThat(speaker1).isNotEqualTo(speaker2);
        speaker1.setId(null);
        assertThat(speaker1).isNotEqualTo(speaker2);
    }
}
