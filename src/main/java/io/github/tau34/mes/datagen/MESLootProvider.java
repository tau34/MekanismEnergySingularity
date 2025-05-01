package io.github.tau34.mes.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MESLootProvider extends LootTableProvider {
    protected MESLootProvider(PackOutput p_254123_, Set<ResourceLocation> p_254481_, List<SubProviderEntry> p_253798_) {
        super(p_254123_, p_254481_, p_253798_);
    }

    public MESLootProvider(PackOutput p_254123_) {
        this(p_254123_, Collections.emptySet(), List.of(new SubProviderEntry(MESBlockLootTables::new, LootContextParamSets.BLOCK)));
    }
}
