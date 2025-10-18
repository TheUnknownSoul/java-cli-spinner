package spinner.implementation;

import java.io.PrintStream;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Console spinner
 */
public final class Spinner implements AutoCloseable {
    private final SpinnerStyle style;
    private final PrintStream out;
    private final ScheduledExecutorService ses;
    private final AtomicInteger frameIdx = new AtomicInteger(0);
    private final AtomicBoolean running = new AtomicBoolean(false);
    private volatile String message = "";
    private volatile boolean lastWasStatusLine = false;

    /** Create a spinner with the specified style and output stream.
     */
    private Spinner(SpinnerStyle style, PrintStream out) {
        this.style = style;
        this.out = out;
        this.ses = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "spinner");
            t.setDaemon(true);
            return t;
        });
    }

    /** Start a spinner with the specified style and initial message.
     * @param  style   Spinner style
     * @param  message       Initial message
     * @return Spinner instance
     */
    public static Spinner start(SpinnerStyle style, String message) {
        Spinner s = new Spinner(style, System.out);
        s.startInternal(message);
        return s;
    }

    /** Start a spinner with the specified style, output stream, and initial message.
     * @param  msg   Spinner msg
     */
    private void startInternal(String msg) {
        this.message = msg == null ? "" : msg;
        running.set(true);
        ses.scheduleAtFixedRate(this::tick, 0, style.intervalMs(), TimeUnit.MILLISECONDS);
    }

    /** Update the spinner message.
     * @param msg New message
     */
    public void update(String msg) {
        this.message = msg == null ? "" : msg;
    }

    /** Spinner tick - update the frame.
     */
    private void tick() {
        if (!running.get()) return;
        var frames = style.frames();
        int idx = Math.abs(frameIdx.getAndIncrement() % frames.size());
        String frame = frames.get(idx);

        // ANSI: \r carriage return , \u001B[K — clear to end of line
        out.print("\r" + frame + " " + message + "\u001B[K");
        out.flush();
        lastWasStatusLine = true;
    }

    /** Mark the spinner as succeeded with an optional final message.
     * @param finalMsg Final message
     */
    public void succeed(String finalMsg) {
        stopWithSymbol("✔", finalMsg);
    }

    /** Mark the spinner as failed with an optional final message.
     * @param finalMsg Final message
     */
    public void fail(String finalMsg) {
        stopWithSymbol("✖", finalMsg);
    }

    /** Stop the spinner and print a final message with a symbol.
     * @param symbol   Symbol to display
     * @param finalMsg Final message
     */
    private void stopWithSymbol(String symbol, String finalMsg) {
        stop();
        out.print("\r" + symbol + " " + (finalMsg == null ? "" : finalMsg) + "\u001B[K\n");
        out.flush();
        lastWasStatusLine = false;
    }

    /** Stop the spinner.
     */
    public void stop() {
        if (running.getAndSet(false)) {
            ses.shutdownNow();
            try {
                ses.awaitTermination(200, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ignored) {
            }
            if (lastWasStatusLine) {
                out.print("\r\u001B[K");
                out.flush();
                lastWasStatusLine = false;
            }
        }
    }

    /** Close the spinner, stopping it if necessary.
     */
    @Override
    public void close() {
        stop();
    }
}
