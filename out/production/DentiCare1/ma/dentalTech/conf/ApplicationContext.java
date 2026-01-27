package ma.dentalTech.conf;

import ma.dentalTech.conf.util.PropertiesExtractor;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ApplicationContext {

    private static final String BEANS_PROPERTIES_FILE = "config/beans.properties";
    private static final String CONFIG_PROPERTIES_FILE = "config/config.properties";
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
            // Charger d'abord les repositories (pas de dépendances)
            for (String key : props.stringPropertyNames()) {
                String className = props.getProperty(key);
                if (key.toLowerCase().contains("repo")) {
                    try {
                        Class<?> clazz = Class.forName(className);
                        Object instance = clazz.getDeclaredConstructor().newInstance();
                        beans.put(key, instance);
                        System.out.println("✓ Bean chargé: " + key);
                    } catch (Exception e) {
                        System.err.println("✗ Erreur lors du chargement du bean " + key + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
            // Ensuite charger les services (peuvent dépendre des repositories)
            for (String key : props.stringPropertyNames()) {
                String className = props.getProperty(key);
                if (key.toLowerCase().contains("service") && !beans.containsKey(key)) {
                    try {
                        Class<?> clazz = Class.forName(className);
                        Object instance = clazz.getDeclaredConstructor().newInstance();
                        beans.put(key, instance);
                        System.out.println("✓ Bean chargé: " + key);
                    } catch (Exception e) {
                        System.err.println("✗ Erreur lors du chargement du bean " + key + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
            // Enfin charger les controllers (peuvent dépendre des services)
            for (String key : props.stringPropertyNames()) {
                String className = props.getProperty(key);
                if (key.toLowerCase().contains("controller") && !beans.containsKey(key)) {
                    try {
                        Class<?> clazz = Class.forName(className);
                        Object instance = clazz.getDeclaredConstructor().newInstance();
                        beans.put(key, instance);
                        System.out.println("✓ Bean chargé: " + key);
                    } catch (Exception e) {
                        System.err.println("✗ Erreur lors du chargement du bean " + key + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("⚠ Erreur lors du chargement des beans: " + e.getMessage());
            e.printStackTrace();
            // Ne pas lancer d'exception pour permettre à l'application de continuer
            // Les beans seront chargés à la demande via getBean()
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        String beanName = getBeanNameByClass(clazz);
        if (beanName != null && beans.containsKey(beanName)) {
            return (T) beans.get(beanName);
        }
        
        // Si le bean n'est pas trouvé, essayer de le créer à la volée
        if (beanName != null) {
            try {
                Properties props = PropertiesExtractor.loadConfigFile(BEANS_PROPERTIES_FILE);
                String className = props.getProperty(beanName);
                if (className != null) {
                    Class<?> beanClass = Class.forName(className);
                    Object instance = beanClass.getDeclaredConstructor().newInstance();
                    beans.put(beanName, instance);
                    System.out.println("✓ Bean créé à la volée: " + beanName);
                    return (T) instance;
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la création du bean " + beanName + ": " + e.getMessage());
            }
        }
        
        throw new RuntimeException("Bean non trouvé pour la classe: " + clazz.getName() + 
            " (beanName: " + beanName + ", beans disponibles: " + beans.keySet() + ")");
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
        try {
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
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche du bean pour " + clazz.getName() + ": " + e.getMessage());
        }
        return null;
    }
}

