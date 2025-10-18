package spinner;

import spinner.implementation.Spinner;
import spinner.implementation.SpinnerStyle;
import spinner.implementation.SpinnerStylesLoader;

import java.io.IOException;

/**
 * Spinner application
 * @author Andrii Roi
 * @version 1.0
 *
 */
public class Main {

    /** Main method to demonstrate spinner usage.
     * @param args Command line arguments
     * @throws IOException if spinner styles loading fails
     */
    public static void main(String[] args) throws IOException {
        SpinnerStylesLoader loader = SpinnerStylesLoader.fromClasspath("spinners.json");
        SpinnerStyle style = loader.get("dots2");

        try (Spinner spinner = Spinner.start(style, "Processing…")) {
            for (int i = 1; i <= 5; i++) {
                Thread.sleep(600);
                spinner.update("Step " + i + "/5…");
            }
            spinner.succeed("Done!");
        } catch (Exception e) {
            System.err.println("Operation failed: " + e.getMessage());
        }
    }
}