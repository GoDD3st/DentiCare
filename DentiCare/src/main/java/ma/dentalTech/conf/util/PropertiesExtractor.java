package ma.dentalTech.conf.util;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesExtractor {
    
    public static Properties extractProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream inputStream = PropertiesExtractor.class.getClassLoader()
                .getResourceAsStream("config/" + fileName)) {
            if (inputStream == null) {
                throw new RuntimeException("Fichier de propriétés non trouvé: " + fileName);
            }
            properties.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chargement du fichier de propriétés: " + fileName, e);
        }
        return properties;
    }
}

