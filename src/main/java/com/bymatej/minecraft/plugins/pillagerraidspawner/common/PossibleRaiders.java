package com.bymatej.minecraft.plugins.pillagerraidspawner.common;

import java.util.List;

import org.bukkit.entity.EntityType;

import static com.bymatej.minecraft.plugins.pillagerraidspawner.PillagerRaidSpawner.getPluginReference;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public class PossibleRaiders {

    public enum PossibleEasyRaiders {
        PILLAGER(EntityType.PILLAGER),
        VINDICATOR(EntityType.VINDICATOR);

        private final EntityType entityType;

        private static final List<PossibleEasyRaiders> VALUES = unmodifiableList(asList(values()));

        private static final int SIZE = VALUES.size();

        PossibleEasyRaiders(EntityType entityType) {
            this.entityType = entityType;
        }

        public static PossibleEasyRaiders getRandomRaider() {
            return VALUES.get(getPluginReference().getRandom().nextInt(SIZE));
        }

        public EntityType getEntityType() {
            return entityType;
        }
    }

    public enum PossibleMediumRaiders {
        PILLAGER(EntityType.PILLAGER),
        VINDICATOR(EntityType.VINDICATOR),
        WITCH(EntityType.WITCH);

        private final EntityType entityType;

        private static final List<PossibleMediumRaiders> VALUES = unmodifiableList(asList(values()));

        private static final int SIZE = VALUES.size();

        PossibleMediumRaiders(EntityType entityType) {
            this.entityType = entityType;
        }

        public static PossibleMediumRaiders getRandomRaider() {
            return VALUES.get(getPluginReference().getRandom().nextInt(SIZE));
        }

        public EntityType getEntityType() {
            return entityType;
        }
    }

    public enum PossibleHardRaiders {
        PILLAGER(EntityType.PILLAGER),
        VINDICATOR(EntityType.VINDICATOR),
        RAVAGER(EntityType.RAVAGER),
        EVOKER(EntityType.EVOKER),
        WITCH(EntityType.WITCH),
        ILLUSIONER(EntityType.ILLUSIONER);

        private final EntityType entityType;

        private static final List<PossibleHardRaiders> VALUES = unmodifiableList(asList(values()));

        private static final int SIZE = VALUES.size();

        PossibleHardRaiders(EntityType entityType) {
            this.entityType = entityType;
        }

        public static PossibleHardRaiders getRandomRaider() {
            return VALUES.get(getPluginReference().getRandom().nextInt(SIZE));
        }

        public EntityType getEntityType() {
            return entityType;
        }

    }

}


/*
 public enum Letter {
 A,
 B,
 C,
 //...

 private static final List<Letter> VALUES =
 Collections.unmodifiableList(Arrays.asList(values()));
 private static final int SIZE = VALUES.size();
 private static final Random RANDOM = new Random();

 public static Letter randomLetter()  {
 return VALUES.get(RANDOM.nextInt(SIZE));
 }
 }
 */