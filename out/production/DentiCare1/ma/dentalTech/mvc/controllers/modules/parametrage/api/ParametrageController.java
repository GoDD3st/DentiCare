package ma.dentalTech.mvc.controllers.modules.parametrage.api;

public interface ParametrageController {
    void loadSettings();
    void saveSettings();
    String getSetting(String key);
    void setSetting(String key, String value);
}
