package mod.beethoven92.betterendforge.common.world.biome;

import mod.beethoven92.betterendforge.common.init.ModConfiguredFeatures;
import mod.beethoven92.betterendforge.common.init.ModConfiguredSurfaceBuilders;
import mod.beethoven92.betterendforge.common.init.ModParticleTypes;
import mod.beethoven92.betterendforge.common.init.ModSoundEvents;
import net.minecraft.world.gen.GenerationStage;

public class UmbraValleyBiome extends BetterEndBiome
{

	public UmbraValleyBiome() {
		super(new BiomeTemplate("umbra_valley")
			.setFogColor(100, 100, 100)
			.setFoliageColor(172, 189, 190)
			.setWaterFogColor(69, 104, 134)
				.setFogDensity(1.5F)
			.setSurface(ModConfiguredSurfaceBuilders.get(ModConfiguredSurfaceBuilders.UMBRA_SURFACE))
			.setParticles(ModParticleTypes.AMBER_SPHERE.get(), 0.0001F)
			.setMusic(ModSoundEvents.AMBIENT_UMBRA_VALLEY.get())
				.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ModConfiguredFeatures.INFLEXIA)
				.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ModConfiguredFeatures.FLAMMALIX)
				.addFeature(GenerationStage.Decoration.RAW_GENERATION, ModConfiguredFeatures.THIN_ARCH)
				.addFeature(GenerationStage.Decoration.RAW_GENERATION, ModConfiguredFeatures.UMBRALITH_ARCH)


		);
	}
}
