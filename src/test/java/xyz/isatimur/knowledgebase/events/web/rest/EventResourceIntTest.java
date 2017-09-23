package xyz.isatimur.knowledgebase.events.web.rest;

import xyz.isatimur.knowledgebase.events.EventsApp;

import xyz.isatimur.knowledgebase.events.domain.Event;
import xyz.isatimur.knowledgebase.events.repository.EventRepository;
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
import java.math.BigDecimal;
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

import xyz.isatimur.knowledgebase.events.domain.enumeration.EventType;
/**
 * Test class for the EventResource REST controller.
 *
 * @see EventResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventsApp.class)
public class EventResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final EventType DEFAULT_TYPE = EventType.HACKATON;
    private static final EventType UPDATED_TYPE = EventType.CONFERENCE;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final ZonedDateTime DEFAULT_DATE_TIME_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TIME_START = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_TIME_FINISH = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TIME_FINISH = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PUBLISHED = false;
    private static final Boolean UPDATED_IS_PUBLISHED = true;

    private static final Boolean DEFAULT_IS_ARCHIVED = false;
    private static final Boolean UPDATED_IS_ARCHIVED = true;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEventMockMvc;

    private Event event;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventResource eventResource = new EventResource(eventRepository);
        this.restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource)
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
    public static Event createEntity(EntityManager em) {
        Event event = new Event()
            .url(DEFAULT_URL)
            .title(DEFAULT_TITLE)
            .shortDescription(DEFAULT_SHORT_DESCRIPTION)
            .description(DEFAULT_DESCRIPTION)
            .location(DEFAULT_LOCATION)
            .type(DEFAULT_TYPE)
            .price(DEFAULT_PRICE)
            .dateTimeStart(DEFAULT_DATE_TIME_START)
            .dateTimeFinish(DEFAULT_DATE_TIME_FINISH)
            .logoUrl(DEFAULT_LOGO_URL)
            .isPublished(DEFAULT_IS_PUBLISHED)
            .isArchived(DEFAULT_IS_ARCHIVED);
        return event;
    }

    @Before
    public void initTest() {
        event = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvent() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event
        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate + 1);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testEvent.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEvent.getShortDescription()).isEqualTo(DEFAULT_SHORT_DESCRIPTION);
        assertThat(testEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEvent.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testEvent.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testEvent.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testEvent.getDateTimeStart()).isEqualTo(DEFAULT_DATE_TIME_START);
        assertThat(testEvent.getDateTimeFinish()).isEqualTo(DEFAULT_DATE_TIME_FINISH);
        assertThat(testEvent.getLogoUrl()).isEqualTo(DEFAULT_LOGO_URL);
        assertThat(testEvent.isIsPublished()).isEqualTo(DEFAULT_IS_PUBLISHED);
        assertThat(testEvent.isIsArchived()).isEqualTo(DEFAULT_IS_ARCHIVED);
    }

    @Test
    @Transactional
    public void createEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event with an existing ID
        event.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList
        restEventMockMvc.perform(get("/api/events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].shortDescription").value(hasItem(DEFAULT_SHORT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].dateTimeStart").value(hasItem(sameInstant(DEFAULT_DATE_TIME_START))))
            .andExpect(jsonPath("$.[*].dateTimeFinish").value(hasItem(sameInstant(DEFAULT_DATE_TIME_FINISH))))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL.toString())))
            .andExpect(jsonPath("$.[*].isPublished").value(hasItem(DEFAULT_IS_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].isArchived").value(hasItem(DEFAULT_IS_ARCHIVED.booleanValue())));
    }

    @Test
    @Transactional
    public void getEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.shortDescription").value(DEFAULT_SHORT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.dateTimeStart").value(sameInstant(DEFAULT_DATE_TIME_START)))
            .andExpect(jsonPath("$.dateTimeFinish").value(sameInstant(DEFAULT_DATE_TIME_FINISH)))
            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL.toString()))
            .andExpect(jsonPath("$.isPublished").value(DEFAULT_IS_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.isArchived").value(DEFAULT_IS_ARCHIVED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        Event updatedEvent = eventRepository.findOne(event.getId());
        updatedEvent
            .url(UPDATED_URL)
            .title(UPDATED_TITLE)
            .shortDescription(UPDATED_SHORT_DESCRIPTION)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .type(UPDATED_TYPE)
            .price(UPDATED_PRICE)
            .dateTimeStart(UPDATED_DATE_TIME_START)
            .dateTimeFinish(UPDATED_DATE_TIME_FINISH)
            .logoUrl(UPDATED_LOGO_URL)
            .isPublished(UPDATED_IS_PUBLISHED)
            .isArchived(UPDATED_IS_ARCHIVED);

        restEventMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEvent)))
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEvent.getShortDescription()).isEqualTo(UPDATED_SHORT_DESCRIPTION);
        assertThat(testEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEvent.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testEvent.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEvent.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testEvent.getDateTimeStart()).isEqualTo(UPDATED_DATE_TIME_START);
        assertThat(testEvent.getDateTimeFinish()).isEqualTo(UPDATED_DATE_TIME_FINISH);
        assertThat(testEvent.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
        assertThat(testEvent.isIsPublished()).isEqualTo(UPDATED_IS_PUBLISHED);
        assertThat(testEvent.isIsArchived()).isEqualTo(UPDATED_IS_ARCHIVED);
    }

    @Test
    @Transactional
    public void updateNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Create the Event

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEventMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        int databaseSizeBeforeDelete = eventRepository.findAll().size();

        // Get the event
        restEventMockMvc.perform(delete("/api/events/{id}", event.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Event.class);
        Event event1 = new Event();
        event1.setId(1L);
        Event event2 = new Event();
        event2.setId(event1.getId());
        assertThat(event1).isEqualTo(event2);
        event2.setId(2L);
        assertThat(event1).isNotEqualTo(event2);
        event1.setId(null);
        assertThat(event1).isNotEqualTo(event2);
    }
}
