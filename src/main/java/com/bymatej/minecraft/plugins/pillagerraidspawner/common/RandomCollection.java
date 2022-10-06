package com.bymatej.minecraft.plugins.pillagerraidspawner.common;

import java.util.NavigableMap;
import java.util.TreeMap;

import static com.bymatej.minecraft.plugins.pillagerraidspawner.PillagerRaidSpawner.getPluginReference;

/**
 * Credits: https://stackoverflow.com/a/6409791
 *
 * @param <E> Type
 */
public class RandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<>();

    private double total = 0;

    public RandomCollection<E> add(double weight, E result) {
        if (weight <= 0) {
            return this;
        }

        total += weight;
        map.put(total, result);
        return this;
    }

    public E next() {
        double value = getPluginReference().getRandom().nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
}
