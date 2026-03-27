package com.phasmidsoftware.dsaipg.util.benchmark;

import com.phasmidsoftware.dsaipg.util.config.Config;
import com.phasmidsoftware.dsaipg.util.logging.LazyLogger;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Class which is able to time the running of functions.
 */
public class Timer {

    final static LazyLogger logger = new LazyLogger(Timer.class);

    public <T> double repeat(int n, Supplier<T> function) {
        for (int i = 0; i < n; i++) {
            function.get();
            lap();
        }
        pause();
        final double result = meanLapTime();
        resume();
        return result;
    }

    public <T, U> double repeat(int n, Supplier<T> supplier, Function<T, U> function) {
        return repeat(n, false, supplier, function, null, null);
    }

    public <T, U> double repeat(int n, boolean warmup, Supplier<T> supplier, Function<T, U> function,
                                UnaryOperator<T> preFunction, Consumer<U> postFunction) {
        pause();
        doTrace(() -> "repeat: with " + n + " runs");
        doTrace(warmup, () -> "warmup");
        int lastx = -1;
        for (int i = 0; i < n; i++)
            lastx = doRepeatForIteration(n, warmup, supplier, function, preFunction, postFunction, lastx, i);
        final double result = meanLapTime();
        showProgress.accept("\r");
        resume();
        return result;
    }

    public double stop() {
        pauseAndLap();
        doTrace(() -> "stop timer");
        return meanLapTime();
    }

    public double meanLapTime() {
        if (running) throw new TimerException();
        return toMillisecs(ticks) / laps;
    }

    public void pauseAndLap() {
        lap();
        ticks += getClock();
        running = false;
        doTrace(() -> "pause timer and lap after millisecs: " + ticks * 1.0E-6);
    }

    public void resume() {
        if (running) throw new TimerException();
        ticks -= getClock();
        doTrace(() -> "resume timer");
        running = true;
    }

    public void lap() {
        if (!running) throw new TimerException();
        laps++;
        doTrace(() -> "lap " + laps);
    }

    public void pause() {
        pauseAndLap();
        laps--;
        doTrace(() -> "pause timer");
    }

    public double millisecs() {
        if (running) throw new TimerException();
        return toMillisecs(ticks);
    }

    @Override
    public String toString() {
        return "Timer{" +
                "ticks=" + ticks +
                ", laps=" + laps +
                ", running=" + running +
                '}';
    }

    public Timer(Consumer<String> showProgress) {
        this.showProgress = showProgress;
        Supplier<String> f = () -> "create new timer";
        doTrace(f);
        resume();
    }

    public Timer(Config config) {
        this(progressFunction(config.getBoolean("timer", "showprogress")));
    }

    /**
     * 执行单次迭代：暂停计时 → 准备数据（preFunction）→ 恢复计时 → 执行函数 → 暂停计时（lap）
     * → 执行后处理（postFunction）→ 恢复计时（为下一轮做准备）
     */
    private <T, U> int doRepeatForIteration(int n, boolean warmup, Supplier<T> supplier,
                                            Function<T, U> function,
                                            UnaryOperator<T> preFunction,
                                            Consumer<U> postFunction,
                                            int lastx, int i) {
        // 1. 获取输入值（此时计时器已暂停）
        T t = supplier.get();

        // 2. 可选：预处理（不计时）
        if (preFunction != null)
            t = preFunction.apply(t);

        // 3. 恢复计时，执行被测函数，再暂停（计为一个 lap）
        resume();
        U result = function.apply(t);
        pauseAndLap();

        // 4. 可选：后处理（不计时）
        if (postFunction != null)
            postFunction.accept(result);

        // 5. 显示进度
        int x = (int) Math.round(10.0 * (i + 1) / n);
        return doPrintStatus(lastx, x);
    }

    private int doPrintStatus(int lastx, final int x) {
        if (x != lastx) {
            if (x % 10 == 0)
                showProgress.accept("" + (10 - x / 10));
            else
                showProgress.accept(".");
        }
        return x;
    }

    private void doTrace(final boolean condition, Supplier<String> messageFunction) {
        if (condition) logger.trace(messageFunction);
    }

    private void doTrace(Supplier<String> f) {
        doTrace(true, f);
    }

    private long ticks = 0L;
    private int laps = 0;
    private boolean running = false;

    private long getTicks() { return ticks; }
    private int getLaps()   { return laps; }
    private boolean isRunning() { return running; }

    private final Consumer<String> showProgress;

    /**
     * 使用 System.nanoTime() 作为时钟源，与 toMillisecs 保持一致。
     */
    private static long getClock() {
        return System.nanoTime();
    }

    static Consumer<String> progressFunction(boolean showProgress) {
        return showProgress ? System.out::print : (s) -> { };
    }

    /**
     * 纳秒 → 毫秒，与 getClock() 保持一致。
     */
    private static double toMillisecs(long ticks) {
        return ticks / 1_000_000.0;
    }

    static class TimerException extends RuntimeException {
        public TimerException() { }
        public TimerException(String message) { super(message); }
        public TimerException(String message, Throwable cause) { super(message, cause); }
        public TimerException(Throwable cause) { super(cause); }
    }
}