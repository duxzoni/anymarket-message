package br.com.db1.anymarket.message.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Translated.
 */
@Entity
@Table(name = "translated")
public class Translated implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "translated_message", nullable = false)
    private String translatedMessage;

    @NotNull
    @Column(name = "reason", nullable = false)
    private String reason;

    @NotNull
    @Column(name = "action_to_fix", nullable = false)
    private String actionToFix;

    @Column(name = "article_link")
    private String articleLink;

    @ManyToOne
    @JsonIgnoreProperties("translateds")
    private Message message;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Language language;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTranslatedMessage() {
        return translatedMessage;
    }

    public Translated translatedMessage(String translatedMessage) {
        this.translatedMessage = translatedMessage;
        return this;
    }

    public void setTranslatedMessage(String translatedMessage) {
        this.translatedMessage = translatedMessage;
    }

    public String getReason() {
        return reason;
    }

    public Translated reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getActionToFix() {
        return actionToFix;
    }

    public Translated actionToFix(String actionToFix) {
        this.actionToFix = actionToFix;
        return this;
    }

    public void setActionToFix(String actionToFix) {
        this.actionToFix = actionToFix;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public Translated articleLink(String articleLink) {
        this.articleLink = articleLink;
        return this;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public Message getMessage() {
        return message;
    }

    public Translated message(Message message) {
        this.message = message;
        return this;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Language getLanguage() {
        return language;
    }

    public Translated language(Language language) {
        this.language = language;
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
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
        Translated translated = (Translated) o;
        if (translated.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), translated.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Translated{" +
            "id=" + getId() +
            ", translatedMessage='" + getTranslatedMessage() + "'" +
            ", reason='" + getReason() + "'" +
            ", actionToFix='" + getActionToFix() + "'" +
            ", articleLink='" + getArticleLink() + "'" +
            "}";
    }
}
