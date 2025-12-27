package mobile.backend.global.config.converter;

import mobile.backend.videoEdit.domain.command.ScrollDirection;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ScrollDirectionConverter implements Converter<String, ScrollDirection> {

    @Override
    public ScrollDirection convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return ScrollDirection.valueOf(source.toUpperCase());
    }
}
