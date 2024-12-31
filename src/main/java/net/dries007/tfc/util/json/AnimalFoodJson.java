/*
 * Work under Copyright. Licensed under the EUPL.
 * See the project README.md and LICENSE.txt for more information.
 */

package net.dries007.tfc.util.json;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.dries007.tfc.objects.entity.animal.AnimalFood;

import java.lang.reflect.Type;

import static su.terrafirmagreg.old.api.data.Reference.TFC;

public class AnimalFoodJson implements JsonDeserializer<AnimalFood> {

  @Override
  public AnimalFood deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject jsonObject = JsonUtils.getJsonObject(json, "entity");
    AnimalFood animalFood = new AnimalFood(jsonObject.get("eat_rotten").getAsBoolean());
    JsonObject food = JsonUtils.getJsonObject(jsonObject, "foods");
    food.entrySet().forEach(entry ->
                            {
                              Ingredient ingredient = CraftingHelper.getIngredient(entry.getValue(), new JsonContext(TFC));
                              animalFood.addFood(ingredient);
                            });
    return animalFood;
  }
}
