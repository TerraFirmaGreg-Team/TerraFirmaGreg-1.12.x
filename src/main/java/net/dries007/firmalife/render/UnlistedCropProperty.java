package net.dries007.firmalife.render;

import net.minecraftforge.common.property.IUnlistedProperty;

import net.dries007.tfc.objects.recipes.PlanterRecipe;

public class UnlistedCropProperty implements IUnlistedProperty<PlanterRecipe.PlantInfo> {

  public int ordinal;

  public UnlistedCropProperty(int ordinal) {
    this.ordinal = ordinal;
  }

  @Override
  public String getName() {
    return "UnlistedCropProperty" + ordinal;
  }

  @Override
  public boolean isValid(PlanterRecipe.PlantInfo value) {
    return true;
  }

  @Override
  public Class<PlanterRecipe.PlantInfo> getType() {
    return PlanterRecipe.PlantInfo.class;
  }

  @Override
  public String valueToString(PlanterRecipe.PlantInfo value) {
    if (value.getRecipe().getRegistryName() == null) {return "null";}
    return value.getRecipe().getRegistryName().toString() + "_" + value.getStage();
  }
}
