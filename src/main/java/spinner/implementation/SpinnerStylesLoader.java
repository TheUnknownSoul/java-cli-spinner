package spinner.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Loader for spinner styles from JSON
 * @param styles   Map of spinner styles loaded from JSON
 */
public record SpinnerStylesLoader(Map<String, SpinnerStyle> styles) {

    /** Load spinner styles from a JSON file path.
     * @param jsonFile Path to the JSON file
     * @throws IOException if file not found or loading fails
     * @return SpinnerStylesLoader instance with loaded styles.
     */
    public static SpinnerStylesLoader fromPath(Path jsonFile) throws IOException {
        try (InputStream in = Files.newInputStream(jsonFile)) {
            return fromInputStream(in);
        }
    }

    /** Load spinner styles from a classpath resource.
     * @param resource Classpath resource name
     * @throws IOException if resource not found or loading fails
     * @return SpinnerStylesLoader instance with loaded styles.
     */
    public static SpinnerStylesLoader fromClasspath(String resource) throws IOException {
        try (InputStream in = SpinnerStylesLoader.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) throw new IOException("Resource not found: " + resource);
            return fromInputStream(in);
        }
    }

    /** Setup spinner options from input stream.
     * @param in Input stream of the JSON data.
     * @return SpinnerStylesLoader instance with loaded styles.
     */
    private static SpinnerStylesLoader fromInputStream(InputStream in) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(in);

        Map<String, SpinnerStyle> map = new LinkedHashMap<>();
        Iterator<String> names = root.fieldNames();
        while (names.hasNext()) {
            String name = names.next();
            JsonNode node = root.get(name);
            int interval = node.path("interval").asInt(80);
            List<String> frames = new ArrayList<>();
            for (JsonNode f : node.withArray("frames")) frames.add(f.asText());
            map.put(name, new SpinnerStyle(name, interval, frames));
        }
        if (map.isEmpty()) throw new IOException("No spinner styles found");
        return new SpinnerStylesLoader(map);
    }

    /** Get spinner style by name from spinners.json file.
     * @param name Spinner style name.
     * @return SpinnerStyle name.
     */
    public SpinnerStyle get(String name) {
        SpinnerStyle s = styles.get(name);
        if (s == null) throw new IllegalArgumentException("Unknown spinner style: " + name);
        return s;
    }
    /** Get available spinner style names.
     * @return Set of available spinner style names.
     */
    public Set<String> available() {
        return Collections.unmodifiableSet(styles.keySet());
    }
}
