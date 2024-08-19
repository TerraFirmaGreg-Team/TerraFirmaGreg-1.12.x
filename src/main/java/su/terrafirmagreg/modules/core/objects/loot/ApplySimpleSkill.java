package su.terrafirmagreg.modules.core.objects.loot;

import su.terrafirmagreg.modules.core.capabilities.player.CapabilityPlayer;
import su.terrafirmagreg.api.util.ModUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import net.dries007.tfc.util.skills.SimpleSkill;
import net.dries007.tfc.util.skills.SkillType;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ApplySimpleSkill extends LootFunction {

    private final SkillType<? extends SimpleSkill> skillType;
    private final RandomValueRange valueRange;
    private final float incrementAmount;

    private ApplySimpleSkill(LootCondition[] conditionsIn, RandomValueRange valueRange, SkillType<? extends SimpleSkill> skillType,
                             float incrementAmount) {
        super(conditionsIn);
        this.valueRange = valueRange;
        this.skillType = skillType;
        this.incrementAmount = incrementAmount;
    }

    @Override
    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        Entity entity = context.getKillerPlayer();
        if (entity instanceof EntityPlayer) {
            var cap = CapabilityPlayer.get((EntityPlayer) entity);
            if (cap != null) {
                SimpleSkill skill = cap.getSkill(this.skillType);
                if (skill != null) {
                    // Minimum of 1, At 0 skill, returns a bonus of an amount between the difference, At max skill, returns the actual range
                    stack.setCount(1 + (int) (valueRange.generateInt(rand) - valueRange.getMin() * (1 - skill.getTotalLevel())));
                    skill.add(incrementAmount);
                }
            }
        }
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<ApplySimpleSkill> {

        public Serializer() {
            super(ModUtils.resource("apply_skill"), ApplySimpleSkill.class);
        }

        @Override
        public void serialize(JsonObject object, ApplySimpleSkill functionClazz, JsonSerializationContext serializationContext) {
            object.add("skill", serializationContext.serialize(functionClazz.skillType.getName()));
            object.add("add", serializationContext.serialize(functionClazz.incrementAmount));
            object.add("count", serializationContext.serialize(functionClazz.valueRange));
        }

        @Override
        @NotNull
        public ApplySimpleSkill deserialize(JsonObject object, JsonDeserializationContext deserializationContext,
                                            LootCondition[] conditionsIn) {
            String skillName = JsonUtils.getString(object, "skill");
            float amount = JsonUtils.getFloat(object, "add");
            SkillType<? extends SimpleSkill> skillType = SkillType.get(skillName, SimpleSkill.class);
            RandomValueRange valueRange = JsonUtils.deserializeClass(object, "count", deserializationContext, RandomValueRange.class);
            if (skillType == null) {
                throw new JsonParseException("Unknown skill type: '" + skillName + "'");
            }
            return new ApplySimpleSkill(conditionsIn, valueRange, skillType, amount);
        }
    }
}
