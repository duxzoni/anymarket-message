package br.com.db1.anymarket.message.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@Document(indexName = "message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "jhi_key", nullable = false)
    private String key;

    @NotNull
    @Column(name = "retry", nullable = false)
    private Boolean retry;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Regex regex;

    @OneToMany(mappedBy = "message")
    private Set<Translated> translateds = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Origin origin;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Category category;

    @ManyToMany
    @JoinTable(name = "message_tags",
               joinColumns = @JoinColumn(name = "messages_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "id"))
    private Set<HashTag> tags = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public Message key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean isRetry() {
        return retry;
    }

    public Message retry(Boolean retry) {
        this.retry = retry;
        return this;
    }

    public void setRetry(Boolean retry) {
        this.retry = retry;
    }

    public Regex getRegex() {
        return regex;
    }

    public Message regex(Regex regex) {
        this.regex = regex;
        return this;
    }

    public void setRegex(Regex regex) {
        this.regex = regex;
    }

    public Set<Translated> getTranslateds() {
        return translateds;
    }

    public Message translateds(Set<Translated> translateds) {
        this.translateds = translateds;
        return this;
    }

    public Message addTranslated(Translated translated) {
        this.translateds.add(translated);
        translated.setMessage(this);
        return this;
    }

    public Message removeTranslated(Translated translated) {
        this.translateds.remove(translated);
        translated.setMessage(null);
        return this;
    }

    public void setTranslateds(Set<Translated> translateds) {
        this.translateds = translateds;
    }

    public Origin getOrigin() {
        return origin;
    }

    public Message origin(Origin origin) {
        this.origin = origin;
        return this;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public Category getCategory() {
        return category;
    }

    public Message category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<HashTag> getTags() {
        return tags;
    }

    public Message tags(Set<HashTag> hashTags) {
        this.tags = hashTags;
        return this;
    }

    public Message addTags(HashTag hashTag) {
        this.tags.add(hashTag);
        return this;
    }

    public Message removeTags(HashTag hashTag) {
        this.tags.remove(hashTag);
        return this;
    }

    public void setTags(Set<HashTag> hashTags) {
        this.tags = hashTags;
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
        Message message = (Message) o;
        if (message.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), message.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", retry='" + isRetry() + "'" +
            "}";
    }
}
