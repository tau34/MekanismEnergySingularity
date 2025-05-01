package io.github.tau34.mes.mixin;

import mekanism.common.lib.multiblock.IMultiblockBase;
import mekanism.common.lib.multiblock.Structure;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = Structure.class, remap = false)
public interface StructureMixin {
    @Accessor
    Map<BlockPos, IMultiblockBase> getNodes();
}
