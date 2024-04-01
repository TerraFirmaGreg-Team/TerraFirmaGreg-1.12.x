package lyeoj.tfcthings.init;

import lyeoj.tfcthings.entity.living.EntityPigvil;
import lyeoj.tfcthings.entity.projectile.EntityRopeBridgeThrown;
import lyeoj.tfcthings.entity.projectile.EntityThrownHookJavelin;
import lyeoj.tfcthings.entity.projectile.EntityThrownRopeJavelin;
import lyeoj.tfcthings.main.TFCThings;
import lyeoj.tfcthings.renderer.RenderPigvil;
import lyeoj.tfcthings.renderer.RenderThrownRopeBridge;
import lyeoj.tfcthings.renderer.RenderThrownRopeJavelin;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import static su.terrafirmagreg.api.lib.Constants.MODID_TFCTHINGS;

public class TFCThingsEntities {

	public static final MobInfo[] MOB_ENTITY_INFOS = {
			new MobInfo("pigvil", EntityPigvil.class, 1, 64, 3, true, 0XF1AAAA, 0X555555)
	};

	public static final NonMobEntityInfo[] NON_MOB_ENTITY_INFOS = {
			new NonMobEntityInfo("ropejavelinthrown", EntityThrownRopeJavelin.class, 3, 64, 2, true),
			new NonMobEntityInfo("hookjavelinthrown", EntityThrownHookJavelin.class, 4, 64, 2, true),
			new NonMobEntityInfo("ropebridgethrown", EntityRopeBridgeThrown.class, 5, 64, 2, true)
	};

	public static void registerEntities() {
		for (MobInfo info : MOB_ENTITY_INFOS) {
			EntityRegistry.registerModEntity(new ResourceLocation(MODID_TFCTHINGS, info.name),
					info.entityClass, info.name, info.id, TFCThings.instance, info.trackingRange,
					info.updateFrequency, info.sendsVelocityUpdates, info.eggP, info.eggS);
		}
		for (NonMobEntityInfo info : NON_MOB_ENTITY_INFOS) {
			EntityRegistry.registerModEntity(new ResourceLocation(MODID_TFCTHINGS, info.name),
					info.entityClass, info.name, info.id, TFCThings.instance, info.trackingRange,
					info.updateFrequency, info.sendsVelocityUpdates);
		}
	}

	public static void registerEntityModels() {
		RenderingRegistry.registerEntityRenderingHandler(EntityPigvil.class, RenderPigvil::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityThrownRopeJavelin.class, RenderThrownRopeJavelin::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityThrownHookJavelin.class, RenderThrownRopeJavelin::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityRopeBridgeThrown.class, RenderThrownRopeBridge::new);

	}

	public static class MobInfo {
		public String name;
		public Class entityClass;
		public int id;
		public int trackingRange;
		public int updateFrequency;
		public boolean sendsVelocityUpdates;
		public int eggP;
		public int eggS;

		public MobInfo(String name, Class entityClass, int id, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int eggP, int eggS) {
			this.name = name;
			this.entityClass = entityClass;
			this.id = id;
			this.trackingRange = trackingRange;
			this.updateFrequency = updateFrequency;
			this.sendsVelocityUpdates = sendsVelocityUpdates;
			this.eggP = eggP;
			this.eggS = eggS;
		}
	}

	public static class NonMobEntityInfo {
		public String name;
		@SuppressWarnings("rawtypes")
		public Class entityClass;
		public int id;
		public int trackingRange;
		public int updateFrequency;
		public boolean sendsVelocityUpdates;

		@SuppressWarnings("rawtypes")
		public NonMobEntityInfo(String name, Class entityClass, int id, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
			this.name = name;
			this.entityClass = entityClass;
			this.id = id;
			this.trackingRange = trackingRange;
			this.updateFrequency = updateFrequency;
			this.sendsVelocityUpdates = sendsVelocityUpdates;
		}
	}

}
