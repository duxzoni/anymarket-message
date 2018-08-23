package br.com.db1.anymarket.message.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Regex.
 */
@Entity
@Table(name = "regex")
public class Regex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "string_pattern", nullable = false)
    private String stringPattern;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStringPattern() {
        return stringPattern;
    }

    public Regex stringPattern(String stringPattern) {
        this.stringPattern = stringPattern;
        return this;
    }

    public void setStringPattern(String stringPattern) {
        this.stringPattern = stringPattern;
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
        Regex regex = (Regex) o;
        if (regex.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), regex.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Regex{" +
            "id=" + getId() +
            ", stringPattern='" + getStringPattern() + "'" +
            "}";
    }
}
