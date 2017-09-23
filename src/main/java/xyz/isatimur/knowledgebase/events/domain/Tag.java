package xyz.isatimur.knowledgebase.events.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Tag.
 */
@Entity
@Table(name = "tag")
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Set<Speaker> speakers = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Set<Event> events = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Tag name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Speaker> getSpeakers() {
        return speakers;
    }

    public Tag speakers(Set<Speaker> speakers) {
        this.speakers = speakers;
        return this;
    }

    public Tag addSpeaker(Speaker speaker) {
        this.speakers.add(speaker);
        speaker.getTags().add(this);
        return this;
    }

    public Tag removeSpeaker(Speaker speaker) {
        this.speakers.remove(speaker);
        speaker.getTags().remove(this);
        return this;
    }

    public void setSpeakers(Set<Speaker> speakers) {
        this.speakers = speakers;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Tag events(Set<Event> events) {
        this.events = events;
        return this;
    }

    public Tag addEvent(Event event) {
        this.events.add(event);
        event.getTags().add(this);
        return this;
    }

    public Tag removeEvent(Event event) {
        this.events.remove(event);
        event.getTags().remove(this);
        return this;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
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
        Tag tag = (Tag) o;
        if (tag.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tag.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
