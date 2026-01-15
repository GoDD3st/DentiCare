package ma.dentalTech.repository.modules.agenda;

import ma.dentalTech.entities.enums.JoursEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class AgendaUtils {
    private AgendaUtils() {}
    public static String serializeJours(List<JoursEnum> jours) {
        if (jours == null || jours.isEmpty()) {
            return "";
        }
        return jours.stream()
                .map(JoursEnum::name)
                .collect(Collectors.joining(","));
    }

    public static List<JoursEnum> deserializeJours(String str) {
        if (str == null || str.isEmpty()) {
            return new ArrayList<>();
        }
        List<JoursEnum> jours = new ArrayList<>();
        String[] parts = str.split(",");
        for (String part : parts) {
            try {
                jours.add(JoursEnum.valueOf(part));
            } catch (IllegalArgumentException e) {
                // Ignore invalid enum values
            }
        }
        return jours;
    }
}
