package spinner.implementation;

import java.util.List;

/**
 * Spinner style definition
 * SpinnerStyle Defines a spinner style with name, interval, and frames.
 * @param name          Shows the name of the style from spinners.json
 * @param intervalMs    Indicates the interval in milliseconds between frames
 * @param frames        List of frames for the spinner animation
 */
public record SpinnerStyle(String name, int intervalMs, List<String> frames) {
    /** Constructor for SpinnerStyle.
     * @param name       Name of the spinner style
     * @param intervalMs Interval in milliseconds between frames
     * @param frames     List of frames for the spinner animation
     * @throws IllegalArgumentException if frames list is null or empty
     */
    public SpinnerStyle(String name, int intervalMs, List<String> frames) {
        if (frames == null || frames.isEmpty()) {
            throw new IllegalArgumentException("frames must not be empty");
        }
        this.name = name;
        this.intervalMs = intervalMs;
        this.frames = List.copyOf(frames);
    }
}
