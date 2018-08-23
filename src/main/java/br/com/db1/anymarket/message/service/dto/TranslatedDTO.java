package br.com.db1.anymarket.message.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Translated entity.
 */
public class TranslatedDTO implements Serializable {

    private Long id;

    @NotNull
    private String translatedMessage;

    @NotNull
    private String reason;

    @NotNull
    private String actionToFix;

    private String articleLink;

    private Long messageId;

    private Long languageId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTranslatedMessage() {
        return translatedMessage;
    }

    public void setTranslatedMessage(String translatedMessage) {
        this.translatedMessage = translatedMessage;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getActionToFix() {
        return actionToFix;
    }

    public void setActionToFix(String actionToFix) {
        this.actionToFix = actionToFix;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TranslatedDTO translatedDTO = (TranslatedDTO) o;
        if (translatedDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), translatedDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TranslatedDTO{" +
            "id=" + getId() +
            ", translatedMessage='" + getTranslatedMessage() + "'" +
            ", reason='" + getReason() + "'" +
            ", actionToFix='" + getActionToFix() + "'" +
            ", articleLink='" + getArticleLink() + "'" +
            ", message=" + getMessageId() +
            ", language=" + getLanguageId() +
            "}";
    }
}
