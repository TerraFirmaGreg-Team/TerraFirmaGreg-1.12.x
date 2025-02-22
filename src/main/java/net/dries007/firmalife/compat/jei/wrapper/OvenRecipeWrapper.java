package net.dries007.firmalife.compat.jei.wrapper;

import su.terrafirmagreg.modules.core.feature.calendar.ICalendar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import net.dries007.tfc.compat.jei.wrappers.SimpleRecipeWrapper;
import net.dries007.tfc.objects.recipes.OvenRecipe;

public class OvenRecipeWrapper extends SimpleRecipeWrapper {

  private final OvenRecipe recipe;

  public OvenRecipeWrapper(OvenRecipe recipeWrapper) {
    super(recipeWrapper);
    this.recipe = recipeWrapper;
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    float x = 60f;
    float y = 4f;
    String text = OvenRecipe.getDuration(recipe) / ICalendar.TICKS_IN_HOUR + " " + I18n.format("tooltip.firmalife.hours");
    x = x - minecraft.fontRenderer.getStringWidth(text) / 2.0f;
    minecraft.fontRenderer.drawString(text, x, y, 0xFFFFFF, false);
  }
}
