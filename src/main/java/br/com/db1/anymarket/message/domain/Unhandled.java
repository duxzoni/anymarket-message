package br.com.db1.anymarket.message.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Unhandled.
 */
@Entity
@Table(name = "unhandled")
@Document(indexName = "unhandled")
public class Unhandled implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @NotNull
    @Column(name = "json", nullable = false)
    private String json;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Origin origin;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public Unhandled message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public Unhandled url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJson() {
        return json;
    }

    public Unhandled json(String json) {
        this.json = json;
        return this;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Origin getOrigin() {
        return origin;
    }

    public Unhandled origin(Origin origin) {
        this.origin = origin;
        return this;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Unhandled unhandled = (Unhandled) o;
        if (unhandled.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), unhandled.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Unhandled{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", url='" + getUrl() + "'" +
            ", json='" + getJson() + "'" +
            "}";
    }
}
