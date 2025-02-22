package net.dries007.tfc.objects.items.metal;

import su.terrafirmagreg.modules.core.capabilities.damage.spi.DamageType;
import su.terrafirmagreg.modules.core.capabilities.forge.ForgeableHeatableHandler;
import su.terrafirmagreg.modules.core.capabilities.metal.ICapabilityMetal;
import su.terrafirmagreg.modules.core.capabilities.size.ICapabilitySize;
import su.terrafirmagreg.modules.core.capabilities.size.spi.Size;
import su.terrafirmagreg.modules.core.capabilities.size.spi.Weight;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.util.OreDictionaryHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

/*
 * todo in 1.15+ put more thought in weapons + tools, make them extend the vanilla's classes where possible
 *
 * Extend vanilla class to add the sweeping effect
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemMetalSword extends ItemSword implements ICapabilityMetal, ICapabilitySize {

  private static final Map<Metal, ItemMetalSword> TABLE = new HashMap<>();
  public final ToolMaterial material;
  private final Metal metal;
  private final float attackDamage;

  public ItemMetalSword(Metal metal) {
    //noinspection ConstantConditions
    super(metal.getToolMetal());
    this.metal = metal;
    if (metal.getToolMetal() == null) {
      throw new IllegalArgumentException("You can't make weapons out of non tool metals.");
    } else {
      material = metal.getToolMetal();
    }
    if (!TABLE.containsKey(metal)) {TABLE.put(metal, this);}

    setMaxStackSize(1);
    setMaxDamage(material.getMaxUses());

    // Overriding vanilla because it always add 3.0F damage regardless of material.
    attackDamage = material.getAttackDamage();

    OreDictionaryHelper.register(this, Metal.ItemType.SWORD);
    OreDictionaryHelper.registerDamageType(this, DamageType.SLASHING);
  }

  public static ItemMetalSword get(Metal metal) {
    return TABLE.get(metal);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return false;
  }

  public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
    Multimap<String, AttributeModifier> multimap = HashMultimap.create();
    if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
      multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.attackDamage, 0));
      multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
    }

    return multimap;
  }

  @Nullable
  @Override
  public Metal getMetal(ItemStack stack) {
    return metal;
  }

  @Override
  public int getSmeltAmount(ItemStack stack) {
    if (!isDamageable() || !stack.isItemDamaged()) {return Metal.ItemType.SWORD.getSmeltAmount();}
    double d = (stack.getMaxDamage() - stack.getItemDamage()) / (double) stack.getMaxDamage() - .10;
    return d < 0 ? 0 : MathHelper.floor(Metal.ItemType.SWORD.getSmeltAmount() * d);
  }

  @Nullable
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
    return new ForgeableHeatableHandler(nbt, metal.getSpecificHeat(), metal.getMeltTemp());
  }

  @Override
  @Nonnull
  public IRarity getForgeRarity(@Nonnull ItemStack stack) {
    switch (metal.getTier()) {
      case TIER_I:
      case TIER_II:
        return EnumRarity.COMMON;
      case TIER_III:
        return EnumRarity.UNCOMMON;
      case TIER_IV:
        return EnumRarity.RARE;
      case TIER_V:
        return EnumRarity.EPIC;
    }
    return super.getForgeRarity(stack);
  }

  @Nonnull
  @Override
  public Size getSize(@Nonnull ItemStack stack) {
    return Size.LARGE;
  }

  @Nonnull
  @Override
  public Weight getWeight(@Nonnull ItemStack stack) {
    return Weight.MEDIUM;
  }
}
