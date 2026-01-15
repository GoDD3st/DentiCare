package ma.dentalTech.conf;

import ma.dentalTech.conf.util.PropertiesExtractor;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ApplicationContext {

    private static final String BEANS_PROPERTIES_FILE = "beans.properties";
    private static final String CONFIG_PROPERTIES_FILE = "config.properties";
    private static final Map<String, Object> beans = new HashMap<>();
    private static Properties configProperties;
    private static ApplicationContext instance;

    static {
        loadBeans();
        loadConfigProperties();
    }

    private ApplicationContext() {}

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    private static void loadConfigProperties() {
        try {
            configProperties = PropertiesExtractor.loadConfigFile(CONFIG_PROPERTIES_FILE);
        } catch (Exception e) {
            // If config.properties doesn't exist, create empty properties
            configProperties = new Properties();
            System.out.println("Warning: config.properties not found, using default values");
        }
    }

    public String getProperty(String key, String defaultValue) {
        return configProperties.getProperty(key, defaultValue);
    }
    
    private static void loadBeans() {
        try {
            Properties props = PropertiesExtractor.loadConfigFile(BEANS_PROPERTIES_FILE);
            for (String key : props.stringPropertyNames()) {
                String className = props.getProperty(key);
                try {
                    Class<?> clazz = Class.forName(className);
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    beans.put(key, instance);
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement du bean " + key + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chargement des beans", e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        String beanName = getBeanNameByClass(clazz);
        if (beanName != null) {
            return (T) beans.get(beanName);
        }
        throw new RuntimeException("Bean non trouvé pour la classe: " + clazz.getName());
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        Object bean = beans.get(beanName);
        if (bean == null) {
            throw new RuntimeException("Bean non trouvé: " + beanName);
        }
        return (T) bean;
    }
    
    private static String getBeanNameByClass(Class<?> clazz) {
        Properties props = PropertiesExtractor.loadConfigFile(BEANS_PROPERTIES_FILE);
        for (String key : props.stringPropertyNames()) {
            String className = props.getProperty(key);
            if (className.equals(clazz.getName())) {
                return key;
            }
        }
        // Recherche par nom de classe simple
        String simpleName = clazz.getSimpleName();
        for (String key : props.stringPropertyNames()) {
            if (key.toLowerCase().contains(simpleName.toLowerCase())) {
                return key;
            }
        }
        return null;
    }
}

