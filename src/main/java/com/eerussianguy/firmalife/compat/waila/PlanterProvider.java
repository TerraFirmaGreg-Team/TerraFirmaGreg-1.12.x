package com.eerussianguy.firmalife.compat.waila;

import su.terrafirmagreg.api.util.TileUtils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import com.eerussianguy.firmalife.recipe.PlanterRecipe;
import net.dries007.tfc.compat.waila.interfaces.IWailaBlock;
import net.dries007.tfc.objects.blocks.BlockLargePlanter;
import net.dries007.tfc.objects.blocks.BlockQuadPlanter;
import net.dries007.tfc.objects.te.TEPlanter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlanterProvider implements IWailaBlock {

  @NotNull
  @Override
  public List<String> getTooltip(World world, @NotNull BlockPos pos, @NotNull NBTTagCompound nbt) {
    List<String> currentTooltip = new ArrayList<>();
    IBlockState state = world.getBlockState(pos);
    TileUtils.getTile(world, pos, TEPlanter.class).ifPresent(tile -> {
      Block block = state.getBlock();
      if (block instanceof BlockQuadPlanter) {
        PlanterRecipe.PlantInfo[] info = ((BlockQuadPlanter) block).getCrops(world, pos);
        for (int i = 0; i < 4; i++) {
          PlanterRecipe recipe = info[i].getRecipe();
          if (recipe != null) {
            int maxStage = PlanterRecipe.getMaxStage(recipe);
            int curStage = info[i].getStage();
            if (maxStage == curStage) {
              currentTooltip.add(new TextComponentTranslation(recipe.getOutputItem()
                                                                    .getTranslationKey() + ".name").getFormattedText() + ": Mature");
            } else {
              float curStagePercent = (float) curStage * 100 / maxStage;
              String growth = String.format("%d%%", Math.round(curStagePercent));
              currentTooltip.add(new TextComponentTranslation(recipe.getOutputItem()
                                                                    .getTranslationKey() + ".name").getFormattedText() + ": " + growth);
            }
          } else {
            currentTooltip.add("Empty");
          }
        }
      } else if (block instanceof BlockLargePlanter) {
        PlanterRecipe.PlantInfo info = ((BlockLargePlanter) block).getCrop(world, pos);
        PlanterRecipe recipe = info != null ? info.getRecipe() : null;
        if (recipe != null) {
          int maxStage = PlanterRecipe.getMaxStage(recipe);
          int curStage = info.getStage();
          if (maxStage == curStage) {
            currentTooltip.add(new TextComponentTranslation(recipe.getOutputItem()
                                                                  .getTranslationKey() + ".name").getFormattedText() + ": Mature");
          } else {
            float curStagePercent = (float) curStage * 100 / maxStage;
            String growth = String.format("%d%%", Math.round(curStagePercent));
            currentTooltip.add(new TextComponentTranslation(recipe.getOutputItem()
                                                                  .getTranslationKey() + ".name").getFormattedText() + ": " + growth);
          }
        } else {
          currentTooltip.add("Empty");
        }
      }
      currentTooltip.add(tile.isClimateValid ? "Climate Valid" : "Climate Invalid");
    });
    return currentTooltip;
  }

  @NotNull
  @Override
  public List<Class<?>> getLookupClass() {
    return Collections.singletonList(TEPlanter.class);
  }
}
