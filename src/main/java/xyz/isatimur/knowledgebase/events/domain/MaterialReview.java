package xyz.isatimur.knowledgebase.events.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A MaterialReview.
 */
@Entity
@Table(name = "material_review")
public class MaterialReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created")
    private ZonedDateTime created;

    @Column(name = "author")
    private String author;

    @Column(name = "text")
    private String text;

    @ManyToOne
    private EventMaterial eventMaterial;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public MaterialReview created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getAuthor() {
        return author;
    }

    public MaterialReview author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public MaterialReview text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public EventMaterial getEventMaterial() {
        return eventMaterial;
    }

    public MaterialReview eventMaterial(EventMaterial eventMaterial) {
        this.eventMaterial = eventMaterial;
        return this;
    }

    public void setEventMaterial(EventMaterial eventMaterial) {
        this.eventMaterial = eventMaterial;
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
        MaterialReview materialReview = (MaterialReview) o;
        if (materialReview.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), materialReview.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MaterialReview{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", author='" + getAuthor() + "'" +
            ", text='" + getText() + "'" +
            "}";
    }
}
