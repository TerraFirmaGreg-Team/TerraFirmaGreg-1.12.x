package su.terrafirmagreg.modules.device.object.tile;

import su.terrafirmagreg.api.base.tile.BaseTileHorse;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import com.google.common.collect.Lists;
import net.dries007.horsepower.Configs;
import net.dries007.horsepower.recipes.HPRecipeBase;
import net.dries007.horsepower.recipes.HPRecipes;
import net.dries007.horsepower.util.Localization;

import org.jetbrains.annotations.Nullable;

import lombok.Getter;

public class TileChopperHorse extends BaseTileHorse {

  private int currentWindup;
  private int currentItemChopTime;
  private int totalItemChopTime;
  @Getter
  private float visualWindup = 0;

  public TileChopperHorse() {
    super(2);
  }

  public void onBreakBlock(World world, BlockPos pos, IBlockState state) {

  }

  @Override
  public boolean validateArea() {
    if (searchPos == null) {
      searchPos = Lists.newArrayList();

      for (int x = -3; x <= 3; x++) {
        for (int z = -3; z <= 3; z++) {
          if ((x <= 1 && x >= -1) && (z <= 1 && z >= -1)) {
            continue;
          }
          searchPos.add(getPos().add(x, 0, z));
          searchPos.add(getPos().add(x, 1, z));
        }
      }
    }

    for (BlockPos pos : searchPos) {
      if (!getWorld().getBlockState(pos).getBlock().isReplaceable(world, pos)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean targetReached() {
    currentWindup++;

    if (currentWindup >= Configs.general.pointsForWindup) {
      currentWindup = 0;
      currentItemChopTime++;

      if (currentItemChopTime >= totalItemChopTime) {
        currentItemChopTime = 0;

        totalItemChopTime = HPRecipes.instance().getChoppingTime(getStackInSlot(0), false);
        chopItem();
        return true;
      }
    }
    markDirty();
    return false;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    currentWindup = compound.getInteger("currentWindup");

    if (getStackInSlot(0).getCount() > 0) {
      currentItemChopTime = compound.getInteger("chopTime");
      totalItemChopTime = compound.getInteger("totalChopTime");
    } else {
      currentItemChopTime = 0;
      totalItemChopTime = 1;
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger("currentWindup", currentWindup);
    compound.setInteger("chopTime", currentItemChopTime);
    compound.setInteger("totalChopTime", totalItemChopTime);

    return super.writeToNBT(compound);
  }

  @Override
  public int getPositionOffset() {
    return 0;
  }

  @Override
  public void update() {
    super.update();

    float windup = Configs.general.pointsForWindup > 0 ? Configs.general.pointsForWindup : 1;
    visualWindup = -0.74F + (0.74F * (((float) currentWindup) / (windup - 1)));
  }

  private void chopItem() {
    if (canWork()) {
      ItemStack input = getStackInSlot(0);
      ItemStack result = getRecipeItemStack();
      ItemStack output = getStackInSlot(1);

      if (output.isEmpty()) {
        setInventorySlotContents(1, result.copy());
      } else if (output.getItem() == result.getItem()) {
        output.grow(result.getCount());
      }

      input.shrink(1);
      markDirty();
    }
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    ItemStack itemstack = getStackInSlot(index);
    super.setInventorySlotContents(index, stack);

    if (index == 1 && getStackInSlot(1).isEmpty()) {
      markDirty();
    }

    boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
    if (index == 0 && !flag) {
      totalItemChopTime = HPRecipes.instance().getChoppingTime(stack, false);
      currentItemChopTime = 0;
      currentWindup = 0;
      markDirty();
    }
  }

  @Override
  public int getInventoryStackLimit() {
    return 1;
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return index != 1 && index == 0 && HPRecipes.instance()
                                                .hasChopperRecipe(stack, false) && getStackInSlot(1).isEmpty() && getStackInSlot(0).isEmpty();
  }

  @Override
  public int getField(int id) {
    return switch (id) {
      case 0 -> totalItemChopTime;
      case 1 -> currentItemChopTime;
      case 2 -> currentWindup;
      default -> 0;
    };
  }

  @Override
  public void setField(int id, int value) {
    switch (id) {
      case 0:
        totalItemChopTime = value;
        break;
      case 1:
        currentItemChopTime = value;
      case 2:
        currentWindup = value;
    }
  }

  @Override
  public int getFieldCount() {
    return 3;
  }

  @Override
  public String getName() {
    return "container.chopper";
  }

  @Override
  public int getOutputSlot() {
    return 1;
  }

  @Override
  public ItemStack getRecipeItemStack() {
    return HPRecipes.instance().getChopperResult(getStackInSlot(0), false);
  }

  @Override
  public HPRecipeBase getRecipe() {
    return HPRecipes.instance().getChoppingBlockRecipe(getStackInSlot(0), false);
  }

  @Override
  public boolean canBeRotated() {
    return true;
  }

  @Nullable
  @Override
  public ITextComponent getDisplayName() {
    if (valid) {
      return super.getDisplayName();
    } else {
      return new TextComponentTranslation(Localization.INFO.CHOPPING_INVALID.key()).setStyle(new Style().setColor(TextFormatting.RED));
    }
  }
}
