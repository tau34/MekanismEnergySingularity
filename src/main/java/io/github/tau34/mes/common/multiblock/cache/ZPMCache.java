package io.github.tau34.mes.common.multiblock.cache;

import io.github.tau34.mes.common.multiblock.data.ZPMMultiblockData;
import mekanism.common.lib.multiblock.MultiblockCache;
import net.minecraft.nbt.CompoundTag;

public class ZPMCache extends MultiblockCache<ZPMMultiblockData> {
    private boolean isActive;
    private int air;

    @Override
    public void merge(MultiblockCache<ZPMMultiblockData> mergeCache, RejectContents rejectContents) {
        super.merge(mergeCache, rejectContents);
        this.isActive |= ((ZPMCache)mergeCache).isActive;
        this.air = Math.min(air, ((ZPMCache) mergeCache).air);
    }

    @Override
    public void apply(ZPMMultiblockData data) {
        super.apply(data);
        data.setActive(isActive);
        data.setAir(air);
    }

    @Override
    public void load(CompoundTag nbtTags) {
        super.load(nbtTags);
        this.isActive = nbtTags.getBoolean("isActive");
        this.air = nbtTags.getInt("air");
    }

    @Override
    public void save(CompoundTag nbtTags) {
        super.save(nbtTags);
        nbtTags.putBoolean("isActive", this.isActive);
        nbtTags.putInt("air", this.air);
    }
}
