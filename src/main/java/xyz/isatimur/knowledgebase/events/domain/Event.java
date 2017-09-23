package xyz.isatimur.knowledgebase.events.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import xyz.isatimur.knowledgebase.events.domain.enumeration.EventType;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "title")
    private String title;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private EventType type;

    @Column(name = "price", precision=10, scale=2)
    private BigDecimal price;

    @Column(name = "date_time_start")
    private ZonedDateTime dateTimeStart;

    @Column(name = "date_time_finish")
    private ZonedDateTime dateTimeFinish;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "is_published")
    private Boolean isPublished;

    @Column(name = "is_archived")
    private Boolean isArchived;

    @OneToOne
    @JoinColumn(unique = true)
    private Category category;

    @OneToMany(mappedBy = "event")
    @JsonIgnore
    private Set<EventMaterial> events = new HashSet<>();

    @ManyToOne
    private Event parent;

    @ManyToMany
    @JoinTable(name = "event_tags",
               joinColumns = @JoinColumn(name="events_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="tags_id", referencedColumnName="id"))
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "event_speakers",
               joinColumns = @JoinColumn(name="events_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="speakers_id", referencedColumnName="id"))
    private Set<Speaker> speakers = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public Event url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public Event title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public Event shortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public Event description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public Event location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public EventType getType() {
        return type;
    }

    public Event type(EventType type) {
        this.type = type;
        return this;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Event price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ZonedDateTime getDateTimeStart() {
        return dateTimeStart;
    }

    public Event dateTimeStart(ZonedDateTime dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
        return this;
    }

    public void setDateTimeStart(ZonedDateTime dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public ZonedDateTime getDateTimeFinish() {
        return dateTimeFinish;
    }

    public Event dateTimeFinish(ZonedDateTime dateTimeFinish) {
        this.dateTimeFinish = dateTimeFinish;
        return this;
    }

    public void setDateTimeFinish(ZonedDateTime dateTimeFinish) {
        this.dateTimeFinish = dateTimeFinish;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public Event logoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Boolean isIsPublished() {
        return isPublished;
    }

    public Event isPublished(Boolean isPublished) {
        this.isPublished = isPublished;
        return this;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public Boolean isIsArchived() {
        return isArchived;
    }

    public Event isArchived(Boolean isArchived) {
        this.isArchived = isArchived;
        return this;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    public Category getCategory() {
        return category;
    }

    public Event category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<EventMaterial> getEvents() {
        return events;
    }

    public Event events(Set<EventMaterial> eventMaterials) {
        this.events = eventMaterials;
        return this;
    }

    public Event addEvent(EventMaterial eventMaterial) {
        this.events.add(eventMaterial);
        eventMaterial.setEvent(this);
        return this;
    }

    public Event removeEvent(EventMaterial eventMaterial) {
        this.events.remove(eventMaterial);
        eventMaterial.setEvent(null);
        return this;
    }

    public void setEvents(Set<EventMaterial> eventMaterials) {
        this.events = eventMaterials;
    }

    public Event getParent() {
        return parent;
    }

    public Event parent(Event event) {
        this.parent = event;
        return this;
    }

    public void setParent(Event event) {
        this.parent = event;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Event tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Event addTags(Tag tag) {
        this.tags.add(tag);
        tag.getEvents().add(this);
        return this;
    }

    public Event removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.getEvents().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Speaker> getSpeakers() {
        return speakers;
    }

    public Event speakers(Set<Speaker> speakers) {
        this.speakers = speakers;
        return this;
    }

    public Event addSpeakers(Speaker speaker) {
        this.speakers.add(speaker);
        speaker.getEvents().add(this);
        return this;
    }

    public Event removeSpeakers(Speaker speaker) {
        this.speakers.remove(speaker);
        speaker.getEvents().remove(this);
        return this;
    }

    public void setSpeakers(Set<Speaker> speakers) {
        this.speakers = speakers;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        if (event.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", title='" + getTitle() + "'" +
            ", shortDescription='" + getShortDescription() + "'" +
            ", description='" + getDescription() + "'" +
            ", location='" + getLocation() + "'" +
            ", type='" + getType() + "'" +
            ", price='" + getPrice() + "'" +
            ", dateTimeStart='" + getDateTimeStart() + "'" +
            ", dateTimeFinish='" + getDateTimeFinish() + "'" +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", isPublished='" + isIsPublished() + "'" +
            ", isArchived='" + isIsArchived() + "'" +
            "}";
    }
}
