package tfcflorae.objects.items.rock;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;


import net.dries007.tfc.api.capability.heat.ItemHeatHandler;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.api.types.Rock;
import net.dries007.tfc.api.types.RockCategory;
import net.dries007.tfc.api.util.IRockObject;
import tfcflorae.objects.items.ItemTFCF;
import tfcflorae.util.OreDictionaryHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ItemUnfiredMudBrick extends ItemTFCF implements IRockObject {

    private static final Map<ItemMud, ItemUnfiredMudBrick> MAP = new HashMap<>();
    private final Rock rock;

    public ItemUnfiredMudBrick(ItemMud mud, Rock rock) {
        this.rock = rock;
        if (MAP.put(mud, this) != null) throw new IllegalStateException("There can only be one.");
        setMaxDamage(0);
        OreDictionaryHelper.register(this, "mud", "unfired_brick");
        OreDictionaryHelper.register(this, "mud", "unfired_brick", rock);
        OreDictionaryHelper.register(this, "mud", "unfired_brick", rock.getRockCategory());
    }

    public static ItemUnfiredMudBrick get(Rock rock) {
        return MAP.get(ItemMud.get(rock));
    }

    public static ItemUnfiredMudBrick get(ItemMud mud) {
        return MAP.get(mud);
    }

    public static ItemStack get(ItemMud mud, int amount) {
        return new ItemStack(MAP.get(mud), amount);
    }

    public Rock getRock() {
        return rock;
    }

    @Override
    public @NotNull Size getSize(ItemStack stack) {
        return Size.SMALL; // Stored everywhere
    }

    @Override
    public @NotNull Weight getWeight(ItemStack stack) {
        return Weight.LIGHT; // Stacksize = 32
    }

    @NotNull
    @Override
    public Rock getRock(ItemStack stack) {
        return rock;
    }

    @NotNull
    @Override
    public RockCategory getRockCategory(ItemStack stack) {
        return rock.getRockCategory();
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        // Heat capability, as pottery needs to be able to be fired, or survive despite not having a heat capability
        return new ItemHeatHandler(nbt, 1.0f, 1599f);
    }
}
