package de.flxwdns.spigothelper.injection;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class InjectionLayer {
    private static final Map<Class<?>, Object> instances = new HashMap<>();

    public static @NotNull <T> T next(@NotNull Class<T> clazz) {
        if (instances.containsKey(clazz)) {
            return clazz.cast(instances.get(clazz));
        }
        throw new RuntimeException("No implementation for " + clazz.getName() + " registered!");
    }

    public static void register(@NotNull Class<?> clazzInterface, @NotNull Object instance) {
        if (instances.containsKey(clazzInterface)) {
            throw new RuntimeException(clazzInterface.getSimpleName() + " is already registered!");
        }
        instances.put(clazzInterface, instance);
        instances.put(instance.getClass(), instance);
    }
}
