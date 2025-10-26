package mobile.backend.global.domain.model;

import lombok.Getter;

@Getter
public class Uuid {

    private final String value;

    public Uuid(String value) {
        this.value = value;
    }

    public static Uuid from(String value) {
        return new Uuid(value);
    }
}
