package br.com.db1.anymarket.message.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Language entity.
 */
public class LanguageDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String i18nCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geti18nCode() {
        return i18nCode;
    }

    public void seti18nCode(String i18nCode) {
        this.i18nCode = i18nCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LanguageDTO languageDTO = (LanguageDTO) o;
        if (languageDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), languageDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LanguageDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", i18nCode='" + geti18nCode() + "'" +
            "}";
    }
}
