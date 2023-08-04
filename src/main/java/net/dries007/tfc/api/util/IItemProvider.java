package net.dries007.tfc.api.util;

import net.minecraft.item.ItemBlock;

import javax.annotation.Nullable;

public interface IItemProvider {
    @Nullable
    ItemBlock getItemBlock();
}
