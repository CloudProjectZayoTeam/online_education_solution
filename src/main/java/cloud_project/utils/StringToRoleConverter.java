package cloud_project.utils;

import cloud_project.entity.Role;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;

@Component
public abstract class StringToRoleConverter implements Converter<String, Role> {
    @Override
    public Role convert(String source) {
        return Role.valueOf(source.toUpperCase());
    }

}

