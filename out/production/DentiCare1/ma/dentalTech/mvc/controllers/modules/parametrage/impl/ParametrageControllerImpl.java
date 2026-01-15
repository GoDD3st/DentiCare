package ma.dentalTech.mvc.controllers.modules.parametrage.impl;

import ma.dentalTech.mvc.controllers.modules.parametrage.api.ParametrageController;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ParametrageControllerImpl implements ParametrageController {

    private final Map<String, String> settings = new HashMap<>();

    public ParametrageControllerImpl() {
        loadSettings();
    }

    @Override
    public void loadSettings() {
        // TODO: Charger les paramètres depuis la base de données ou un fichier de configuration
        settings.put("theme", "light");
        settings.put("language", "fr");
    }

    @Override
    public void saveSettings() {
        // TODO: Sauvegarder les paramètres dans la base de données ou un fichier de configuration
        JOptionPane.showMessageDialog(null,
                "Paramètres sauvegardés avec succès",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public String getSetting(String key) {
        return settings.getOrDefault(key, "");
    }

    @Override
    public void setSetting(String key, String value) {
        settings.put(key, value);
    }
}
