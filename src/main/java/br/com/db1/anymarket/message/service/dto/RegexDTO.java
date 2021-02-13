package br.com.db1.anymarket.message.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Regex entity.
 */
public class RegexDTO implements Serializable {

    private Long id;

    @NotNull
    private String stringPattern;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStringPattern() {
        return stringPattern;
    }

    public void setStringPattern(String stringPattern) {
        this.stringPattern = stringPattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RegexDTO regexDTO = (RegexDTO) o;
        if (regexDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), regexDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RegexDTO{" +
            "id=" + getId() +
            ", stringPattern='" + getStringPattern() + "'" +
            "}";
    }
}
