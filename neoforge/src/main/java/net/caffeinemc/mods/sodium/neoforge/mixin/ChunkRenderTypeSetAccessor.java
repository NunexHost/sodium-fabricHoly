package net.caffeinemc.mods.sodium.neoforge.mixin;

import net.minecraftforge.client.ChunkRenderTypeSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.BitSet;

@Mixin(ChunkRenderTypeSet.class)
public interface ChunkRenderTypeSetAccessor {
    @Invoker("<init>")
    static ChunkRenderTypeSet create(BitSet set) {
        return null;
    }

    @Accessor
    BitSet getBits();
}
