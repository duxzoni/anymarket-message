package br.com.db1.anymarket.message.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the HashTag entity.
 */
public class HashTagDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HashTagDTO hashTagDTO = (HashTagDTO) o;
        if (hashTagDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hashTagDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HashTagDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
