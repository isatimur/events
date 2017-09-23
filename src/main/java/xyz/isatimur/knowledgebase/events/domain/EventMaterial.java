package xyz.isatimur.knowledgebase.events.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import xyz.isatimur.knowledgebase.events.domain.enumeration.EventMaterialType;

/**
 * A EventMaterial.
 */
@Entity
@Table(name = "event_material")
public class EventMaterial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "uploaded")
    private ZonedDateTime uploaded;

    @Column(name = "url")
    private String url;

    @Column(name = "text")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private EventMaterialType type;

    @ManyToOne
    private Event event;

    @OneToMany(mappedBy = "eventMaterial")
    @JsonIgnore
    private Set<MaterialReview> materials = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getUploaded() {
        return uploaded;
    }

    public EventMaterial uploaded(ZonedDateTime uploaded) {
        this.uploaded = uploaded;
        return this;
    }

    public void setUploaded(ZonedDateTime uploaded) {
        this.uploaded = uploaded;
    }

    public String getUrl() {
        return url;
    }

    public EventMaterial url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public EventMaterial text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public EventMaterialType getType() {
        return type;
    }

    public EventMaterial type(EventMaterialType type) {
        this.type = type;
        return this;
    }

    public void setType(EventMaterialType type) {
        this.type = type;
    }

    public Event getEvent() {
        return event;
    }

    public EventMaterial event(Event event) {
        this.event = event;
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Set<MaterialReview> getMaterials() {
        return materials;
    }

    public EventMaterial materials(Set<MaterialReview> materialReviews) {
        this.materials = materialReviews;
        return this;
    }

    public EventMaterial addMaterial(MaterialReview materialReview) {
        this.materials.add(materialReview);
        materialReview.setEventMaterial(this);
        return this;
    }

    public EventMaterial removeMaterial(MaterialReview materialReview) {
        this.materials.remove(materialReview);
        materialReview.setEventMaterial(null);
        return this;
    }

    public void setMaterials(Set<MaterialReview> materialReviews) {
        this.materials = materialReviews;
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
        EventMaterial eventMaterial = (EventMaterial) o;
        if (eventMaterial.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), eventMaterial.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EventMaterial{" +
            "id=" + getId() +
            ", uploaded='" + getUploaded() + "'" +
            ", url='" + getUrl() + "'" +
            ", text='" + getText() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
