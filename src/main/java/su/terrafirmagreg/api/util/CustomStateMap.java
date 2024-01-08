package su.terrafirmagreg.api.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import su.terrafirmagreg.Tags;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class CustomStateMap extends StateMapperBase {

	public static File rootFolder = Launch.minecraftHome == null ? new File(".") : Launch.minecraftHome;

	private final IProperty<?> name;
	private final String suffix;
	private final List<IProperty<?>> ignored;
	private final ResourceLocation resourceLocation;
	private final String variant;

	private CustomStateMap(Builder builder) {
		this.name = builder.name;
		this.suffix = builder.suffix;
		this.ignored = builder.ignored;
		this.resourceLocation = builder.resourceLocation;
		this.variant = builder.variant;
	}

	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
		Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap(state.getProperties());
		String s;

		if (this.name == null) {
			s = Block.REGISTRY.getNameForObject(state.getBlock()).toString();
		} else {
			s = String.format("%s:%s", Block.REGISTRY.getNameForObject(state.getBlock())
			                                         .getNamespace(), this.removeName(this.name, map));
		}

		if (this.suffix != null) {
			s = s + this.suffix;
		}

		for (IProperty<?> iproperty : this.ignored) {
			map.remove(iproperty);
		}


		var variantIn = this.getPropertyString(map, variant);
//        if (!map.isEmpty()) {
//            if (this.variant != null) {
//                variantIn = variantIn + variant;
//            }
//            return new ModelResourceLocation(resourceLocation, variantIn);
//        }

		return new ModelResourceLocation(resourceLocation, variantIn);


	}

	public String getPropertyString(Map<IProperty<?>, Comparable<?>> values, String variant) {
		StringBuilder stringbuilder = new StringBuilder();

		for (Map.Entry<IProperty<?>, Comparable<?>> entry : values.entrySet()) {
			if (stringbuilder.length() != 0) {
				stringbuilder.append(",");
			}

			IProperty<?> iproperty = entry.getKey();
			stringbuilder.append(iproperty.getName());
			stringbuilder.append("=");
			stringbuilder.append(this.getPropertyName(iproperty, entry.getValue()));
		}

		if (variant != null) {
			if (stringbuilder.length() != 0) {
				stringbuilder.append(",");
			}
			stringbuilder.append(variant);
		}

		if (stringbuilder.length() == 0) {
			stringbuilder.append("normal");
		}

		return stringbuilder.toString();
	}

	private <T extends Comparable<T>> String getPropertyName(IProperty<T> property, Comparable<?> value) {
		return property.getName((T) value);
	}

	private <T extends Comparable<T>> String removeName(IProperty<T> property, Map<IProperty<?>, Comparable<?>> values) {
		return property.getName((T) values.remove(this.name));
	}

	@SideOnly(Side.CLIENT)
	public static class Builder {

		private final List<IProperty<?>> ignored = Lists.newArrayList();
		private IProperty<?> name;
		private String suffix;
		private ResourceLocation resourceLocation;
		private String variant;

		private String replaceResourceLocation;

		public Builder withName(IProperty<?> builderPropertyIn) {
			this.name = builderPropertyIn;
			return this;
		}

		public Builder withSuffix(String builderSuffixIn) {
			this.suffix = builderSuffixIn;
			return this;
		}

		public Builder ignore(IProperty<?>... ignores) {
			Collections.addAll(this.ignored, ignores);
			return this;
		}

		public Builder customPath(ResourceLocation resourceLocation) {
			this.resourceLocation = resourceLocation;
			return this;
		}

		public Builder customVariant(String variant) {
			this.variant = variant;
			return this;
		}


		public Builder customPathReplace(ResourceLocation resourceLocation, CharSequence target, CharSequence replacement) {
			File blockstateFilePath = new File(rootFolder + "/resources/" + Tags.MOD_ID + "/blockstates/" + resourceLocation.getPath() + ".json");
			try {
				this.replaceResourceLocation = new String(Files.readAllBytes(blockstateFilePath.toPath())).replace(target, replacement);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.resourceLocation = resourceLocation;
			return this;
		}

		public CustomStateMap build() {
			return new CustomStateMap(this);
		}
	}
}
