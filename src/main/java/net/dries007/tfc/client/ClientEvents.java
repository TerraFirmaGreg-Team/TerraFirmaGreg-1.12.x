package net.dries007.tfc.client;

import su.terrafirmagreg.modules.core.capabilities.egg.CapabilityEgg;
import su.terrafirmagreg.modules.core.capabilities.heat.CapabilityHeat;
import su.terrafirmagreg.modules.core.capabilities.metal.CapabilityMetal;
import su.terrafirmagreg.modules.core.capabilities.size.CapabilitySize;
import su.terrafirmagreg.modules.core.capabilities.size.ICapabilitySize;
import su.terrafirmagreg.modules.world.ModuleWorld;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldType;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;


import net.dries007.tfc.ConfigTFC;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.api.capability.food.CapabilityFood;
import net.dries007.tfc.api.capability.food.IFood;
import net.dries007.tfc.api.capability.forge.CapabilityForgeable;
import net.dries007.tfc.api.capability.forge.IForgeable;
import net.dries007.tfc.api.util.IRockObject;
import net.dries007.tfc.client.button.GuiButtonPlayerInventoryTab;
import net.dries007.tfc.client.render.animal.RenderAbstractHorseTFC;
import net.dries007.tfc.client.render.animal.RenderAlpacaTFC;
import net.dries007.tfc.client.render.animal.RenderBlackBearTFC;
import net.dries007.tfc.client.render.animal.RenderBoarTFC;
import net.dries007.tfc.client.render.animal.RenderCamelTFC;
import net.dries007.tfc.client.render.animal.RenderChickenTFC;
import net.dries007.tfc.client.render.animal.RenderCougarTFC;
import net.dries007.tfc.client.render.animal.RenderCowTFC;
import net.dries007.tfc.client.render.animal.RenderCoyoteTFC;
import net.dries007.tfc.client.render.animal.RenderDeerTFC;
import net.dries007.tfc.client.render.animal.RenderDireWolfTFC;
import net.dries007.tfc.client.render.animal.RenderDuckTFC;
import net.dries007.tfc.client.render.animal.RenderGazelleTFC;
import net.dries007.tfc.client.render.animal.RenderGoatTFC;
import net.dries007.tfc.client.render.animal.RenderGrizzlyBearTFC;
import net.dries007.tfc.client.render.animal.RenderGrouseTFC;
import net.dries007.tfc.client.render.animal.RenderHareTFC;
import net.dries007.tfc.client.render.animal.RenderHorseTFC;
import net.dries007.tfc.client.render.animal.RenderHyenaTFC;
import net.dries007.tfc.client.render.animal.RenderJackalTFC;
import net.dries007.tfc.client.render.animal.RenderLionTFC;
import net.dries007.tfc.client.render.animal.RenderLlamaTFC;
import net.dries007.tfc.client.render.animal.RenderMongooseTFC;
import net.dries007.tfc.client.render.animal.RenderMuskOxTFC;
import net.dries007.tfc.client.render.animal.RenderOcelotTFC;
import net.dries007.tfc.client.render.animal.RenderPantherTFC;
import net.dries007.tfc.client.render.animal.RenderParrotTFC;
import net.dries007.tfc.client.render.animal.RenderPheasantTFC;
import net.dries007.tfc.client.render.animal.RenderPigTFC;
import net.dries007.tfc.client.render.animal.RenderPolarBearTFC;
import net.dries007.tfc.client.render.animal.RenderQuailTFC;
import net.dries007.tfc.client.render.animal.RenderRabbitTFC;
import net.dries007.tfc.client.render.animal.RenderSaberToothTFC;
import net.dries007.tfc.client.render.animal.RenderSheepTFC;
import net.dries007.tfc.client.render.animal.RenderTurkeyTFC;
import net.dries007.tfc.client.render.animal.RenderWildebeestTFC;
import net.dries007.tfc.client.render.animal.RenderWolfTFC;
import net.dries007.tfc.client.render.animal.RenderYakTFC;
import net.dries007.tfc.client.render.animal.RenderZebuTFC;
import net.dries007.tfc.client.render.projectile.RenderThrownJavelin;
import net.dries007.tfc.network.PacketSwitchPlayerInventoryTab;
import net.dries007.tfc.objects.entity.EntityFallingBlockTFC;
import net.dries007.tfc.objects.entity.animal.EntityAlpacaTFC;
import net.dries007.tfc.objects.entity.animal.EntityBlackBearTFC;
import net.dries007.tfc.objects.entity.animal.EntityBoarTFC;
import net.dries007.tfc.objects.entity.animal.EntityCamelTFC;
import net.dries007.tfc.objects.entity.animal.EntityChickenTFC;
import net.dries007.tfc.objects.entity.animal.EntityCougarTFC;
import net.dries007.tfc.objects.entity.animal.EntityCowTFC;
import net.dries007.tfc.objects.entity.animal.EntityCoyoteTFC;
import net.dries007.tfc.objects.entity.animal.EntityDeerTFC;
import net.dries007.tfc.objects.entity.animal.EntityDireWolfTFC;
import net.dries007.tfc.objects.entity.animal.EntityDonkeyTFC;
import net.dries007.tfc.objects.entity.animal.EntityDuckTFC;
import net.dries007.tfc.objects.entity.animal.EntityGazelleTFC;
import net.dries007.tfc.objects.entity.animal.EntityGoatTFC;
import net.dries007.tfc.objects.entity.animal.EntityGrizzlyBearTFC;
import net.dries007.tfc.objects.entity.animal.EntityGrouseTFC;
import net.dries007.tfc.objects.entity.animal.EntityHareTFC;
import net.dries007.tfc.objects.entity.animal.EntityHorseTFC;
import net.dries007.tfc.objects.entity.animal.EntityHyenaTFC;
import net.dries007.tfc.objects.entity.animal.EntityJackalTFC;
import net.dries007.tfc.objects.entity.animal.EntityLionTFC;
import net.dries007.tfc.objects.entity.animal.EntityLlamaTFC;
import net.dries007.tfc.objects.entity.animal.EntityMongooseTFC;
import net.dries007.tfc.objects.entity.animal.EntityMuleTFC;
import net.dries007.tfc.objects.entity.animal.EntityMuskOxTFC;
import net.dries007.tfc.objects.entity.animal.EntityOcelotTFC;
import net.dries007.tfc.objects.entity.animal.EntityPantherTFC;
import net.dries007.tfc.objects.entity.animal.EntityParrotTFC;
import net.dries007.tfc.objects.entity.animal.EntityPheasantTFC;
import net.dries007.tfc.objects.entity.animal.EntityPigTFC;
import net.dries007.tfc.objects.entity.animal.EntityPolarBearTFC;
import net.dries007.tfc.objects.entity.animal.EntityQuailTFC;
import net.dries007.tfc.objects.entity.animal.EntityRabbitTFC;
import net.dries007.tfc.objects.entity.animal.EntitySaberToothTFC;
import net.dries007.tfc.objects.entity.animal.EntitySheepTFC;
import net.dries007.tfc.objects.entity.animal.EntityTurkeyTFC;
import net.dries007.tfc.objects.entity.animal.EntityWildebeestTFC;
import net.dries007.tfc.objects.entity.animal.EntityWolfTFC;
import net.dries007.tfc.objects.entity.animal.EntityYakTFC;
import net.dries007.tfc.objects.entity.animal.EntityZebuTFC;
import net.dries007.tfc.objects.entity.projectile.EntityThrownJavelin;
import net.dries007.tfc.util.skills.SmithingSkill;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static su.terrafirmagreg.api.data.Constants.MODID_TFC;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = MODID_TFC)
public class ClientEvents {

    public static void preInit() {
        RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlockTFC.class, RenderFallingBlock::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityThrownJavelin.class, RenderThrownJavelin::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySheepTFC.class, RenderSheepTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCowTFC.class, RenderCowTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityGrizzlyBearTFC.class, RenderGrizzlyBearTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityChickenTFC.class, RenderChickenTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPheasantTFC.class, RenderPheasantTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDeerTFC.class, RenderDeerTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPigTFC.class, RenderPigTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWolfTFC.class, RenderWolfTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRabbitTFC.class, RenderRabbitTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHorseTFC.class, RenderHorseTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDonkeyTFC.class, RenderAbstractHorseTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityMuleTFC.class, RenderAbstractHorseTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPolarBearTFC.class, RenderPolarBearTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityParrotTFC.class, RenderParrotTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLlamaTFC.class, RenderLlamaTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityOcelotTFC.class, RenderOcelotTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPantherTFC.class, RenderPantherTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDuckTFC.class, RenderDuckTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityAlpacaTFC.class, RenderAlpacaTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityGoatTFC.class, RenderGoatTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySaberToothTFC.class, RenderSaberToothTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCamelTFC.class, RenderCamelTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLionTFC.class, RenderLionTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHyenaTFC.class, RenderHyenaTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDireWolfTFC.class, RenderDireWolfTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHareTFC.class, RenderHareTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBoarTFC.class, RenderBoarTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityZebuTFC.class, RenderZebuTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityGazelleTFC.class, RenderGazelleTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWildebeestTFC.class, RenderWildebeestTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityQuailTFC.class, RenderQuailTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityGrouseTFC.class, RenderGrouseTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityMongooseTFC.class, RenderMongooseTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTurkeyTFC.class, RenderTurkeyTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityJackalTFC.class, RenderJackalTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityMuskOxTFC.class, RenderMuskOxTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityYakTFC.class, RenderYakTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBlackBearTFC.class, RenderBlackBearTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCougarTFC.class, RenderCougarTFC::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCoyoteTFC.class, RenderCoyoteTFC::new);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onInitGuiPre(GuiScreenEvent.InitGuiEvent.Pre event) {
        if (ConfigTFC.General.OVERRIDES.forceTFCWorldType && event.getGui() instanceof GuiCreateWorld gui) {
            // Only change if default is selected, because coming back from customisation, this will be set already.
            if (gui.selectedIndex == WorldType.DEFAULT.getId()) {
                gui.selectedIndex = ModuleWorld.WORLD_TYPE_CLASSIC.getId();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onInitGuiPost(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiInventory) {
            int buttonId = event.getButtonList().size();
            int guiLeft = ((GuiInventory) event.getGui()).getGuiLeft();
            int guiTop = ((GuiInventory) event.getGui()).getGuiTop();

            event.getButtonList()
                    .add(new GuiButtonPlayerInventoryTab(TFCGuiHandler.Type.INVENTORY, guiLeft, guiTop, ++buttonId, false));
            event.getButtonList()
                    .add(new GuiButtonPlayerInventoryTab(TFCGuiHandler.Type.SKILLS, guiLeft, guiTop, ++buttonId, true));
            event.getButtonList()
                    .add(new GuiButtonPlayerInventoryTab(TFCGuiHandler.Type.CALENDAR, guiLeft, guiTop, ++buttonId, true));
            event.getButtonList()
                    .add(new GuiButtonPlayerInventoryTab(TFCGuiHandler.Type.NUTRITION, guiLeft, guiTop, ++buttonId, true));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onGuiButtonPressPre(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.getGui() instanceof GuiInventory) {
            if (event.getButton() instanceof GuiButtonPlayerInventoryTab button) {
                // This is to prevent the button from immediately firing after moving (enabled is set to false then)
                if (button.isActive() && button.enabled) {
                    TerraFirmaCraft.getNetwork().sendToServer(new PacketSwitchPlayerInventoryTab(button.getGuiType()));
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onGuiButtonPressPost(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.getGui() instanceof GuiInventory) {
            // This is necessary to catch the resizing of the inventory gui when you open the recipe book
            for (GuiButton button : event.getButtonList()) {
                if (button instanceof GuiButtonPlayerInventoryTab) {
                    ((GuiButtonPlayerInventoryTab) button).updateGuiLeft(((GuiInventory) event.getGui()).getGuiLeft());
                }
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        List<String> tt = event.getToolTip();
        if (!stack.isEmpty()) {
            // Stuff that should always be shown as part of the tooltip
            ICapabilitySize size = CapabilitySize.getIItemSize(stack);
            if (size != null) {
                size.addSizeInfo(stack, tt);
            }
            var cap = CapabilityHeat.get(stack);
            if (cap != null) {
                cap.addHeatInfo(stack, tt);
            }
            IForgeable forging = stack.getCapability(CapabilityForgeable.FORGEABLE_CAPABILITY, null);
            if (forging != null && forging.getWork() > 0) {
                tt.add(I18n.format("tfc.tooltip.forging_in_progress"));
            }
            IFood nutrients = stack.getCapability(CapabilityFood.CAPABILITY, null);
            if (nutrients != null) {
                nutrients.addTooltipInfo(stack, tt, event.getEntityPlayer());
            }
            var egg = CapabilityEgg.get(stack);
            if (egg != null) {
                egg.addEggInfo(stack, tt);
            }
            float skillMod = SmithingSkill.getSkillBonus(stack);
            if (skillMod > 0) {
                String skillValue = String.format("%.2f", skillMod * 100);
                tt.add(I18n.format("tfc.tooltip.smithing_skill", skillValue));
            }

            if (event.getFlags().isAdvanced()) // Only added with advanced tooltip mode
            {
                var metalObject = CapabilityMetal.getMetalItem(stack);
                if (metalObject != null) {
                    metalObject.addMetalInfo(stack, tt);
                }
                if (item instanceof IRockObject) {
                    ((IRockObject) item).addRockInfo(stack, tt);
                } else if (item instanceof ItemBlock) {
                    Block block = ((ItemBlock) item).getBlock();
                    if (block instanceof IRockObject) {
                        ((IRockObject) block).addRockInfo(stack, tt);
                    }
                }

                if (ConfigTFC.Client.TOOLTIP.showToolClassTooltip) {
                    Set<String> toolClasses = item.getToolClasses(stack);
                    if (toolClasses.size() == 1) {
                        tt.add(I18n.format("tfc.tooltip.toolclass", toolClasses.iterator().next()));
                    } else if (toolClasses.size() > 1) {
                        tt.add(I18n.format("tfc.tooltip.toolclasses"));
                        for (String toolClass : toolClasses) {
                            tt.add("+ " + toolClass);
                        }
                    }
                }
                if (ConfigTFC.Client.TOOLTIP.showOreDictionaryTooltip) {
                    int[] ids = OreDictionary.getOreIDs(stack);
                    if (ids.length == 1) {
                        tt.add(I18n.format("tfc.tooltip.oredictionaryentry", OreDictionary.getOreName(ids[0])));
                    } else if (ids.length > 1) {
                        tt.add(I18n.format("tfc.tooltip.oredictionaryentries"));
                        ArrayList<String> names = new ArrayList<>(ids.length);
                        for (int id : ids) {
                            names.add("+ " + OreDictionary.getOreName(id));
                        }
                        names.sort(null); // Natural order (String.compare)
                        tt.addAll(names);
                    }
                }
                if (ConfigTFC.Client.TOOLTIP.showNBTTooltip) {
                    if (stack.hasTagCompound()) {
                        tt.add("NBT: " + stack.getTagCompound());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void textureStitched(TextureStitchEvent.Post event) {
        FluidSpriteCache.clear();
    }
}
