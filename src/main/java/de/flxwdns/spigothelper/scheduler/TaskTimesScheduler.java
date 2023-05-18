package de.flxwdns.spigothelper.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Consumer;

public final class TaskTimesScheduler {
    private int task;
    private Runnable whenFinished;

    public TaskTimesScheduler(Plugin plugin, Consumer<Integer> consumer, int times, long ticks) {
        final int[] value = {0};
        this.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            value[0]++;
            var tempValue = value[0];
            if(value[0] >= times) stop();
            else consumer.accept(tempValue + 1);
        }, 0L, ticks).getTaskId();
    }

    public TaskTimesScheduler(Plugin plugin, Consumer<Integer> consumer, int times, long ticks, long slowerPerTick) {
        final int[] value = {0};
        final long[] tick = {ticks};

        runSlower(plugin, consumer, 0, times, ticks, slowerPerTick);
    }

    private void runSlower(Plugin plugin, Consumer<Integer> consumer, int timesValue, int times, long ticks, long slowerPerTick) {
        final int[] value = {timesValue};
        final long[] tick = {ticks};

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            value[0]++;
            var tempValue = value[0];
            if(value[0] >= times) stop();
            else {
                consumer.accept(tempValue + 1);
                tick[0] = tick[0] + slowerPerTick;
                runSlower(plugin, consumer, value[0], times, tick[0], slowerPerTick);
            }
        }, tick[0]);
    }

    public void whenFinished(Runnable runnable) {
        whenFinished = runnable;
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(this.task);
        if(whenFinished != null) whenFinished.run();
    }
}
