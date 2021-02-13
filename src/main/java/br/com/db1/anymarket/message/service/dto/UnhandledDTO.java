package br.com.db1.anymarket.message.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Unhandled entity.
 */
public class UnhandledDTO implements Serializable {

    private Long id;

    @NotNull
    private String message;

    @NotNull
    private String url;

    @NotNull
    private String json;

    private Long originId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UnhandledDTO unhandledDTO = (UnhandledDTO) o;
        if (unhandledDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), unhandledDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UnhandledDTO{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", url='" + getUrl() + "'" +
            ", json='" + getJson() + "'" +
            ", origin=" + getOriginId() +
            "}";
    }
}
