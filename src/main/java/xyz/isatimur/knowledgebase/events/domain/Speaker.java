package xyz.isatimur.knowledgebase.events.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import xyz.isatimur.knowledgebase.events.domain.enumeration.SpeakerStatusType;

/**
 * A Speaker.
 */
@Entity
@Table(name = "speaker")
public class Speaker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "fio")
    private String fio;

    @Column(name = "biography")
    private String biography;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "company_id")
    private Long companyId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SpeakerStatusType status;

    @ManyToMany
    @JoinTable(name = "speaker_tags",
               joinColumns = @JoinColumn(name="speakers_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="tags_id", referencedColumnName="id"))
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany(mappedBy = "speakers")
    @JsonIgnore
    private Set<Event> events = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public Speaker fio(String fio) {
        this.fio = fio;
        return this;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getBiography() {
        return biography;
    }

    public Speaker biography(String biography) {
        this.biography = biography;
        return this;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public Speaker logoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public Speaker companyId(Long companyId) {
        this.companyId = companyId;
        return this;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public SpeakerStatusType getStatus() {
        return status;
    }

    public Speaker status(SpeakerStatusType status) {
        this.status = status;
        return this;
    }

    public void setStatus(SpeakerStatusType status) {
        this.status = status;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Speaker tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Speaker addTags(Tag tag) {
        this.tags.add(tag);
        tag.getSpeakers().add(this);
        return this;
    }

    public Speaker removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.getSpeakers().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Speaker events(Set<Event> events) {
        this.events = events;
        return this;
    }

    public Speaker addEvent(Event event) {
        this.events.add(event);
        event.getSpeakers().add(this);
        return this;
    }

    public Speaker removeEvent(Event event) {
        this.events.remove(event);
        event.getSpeakers().remove(this);
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
        Speaker speaker = (Speaker) o;
        if (speaker.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), speaker.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Speaker{" +
            "id=" + getId() +
            ", fio='" + getFio() + "'" +
            ", biography='" + getBiography() + "'" +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", companyId='" + getCompanyId() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
