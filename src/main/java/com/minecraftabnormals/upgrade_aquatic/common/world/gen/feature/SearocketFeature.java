package com.minecraftabnormals.upgrade_aquatic.common.world.gen.feature;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.minecraftabnormals.upgrade_aquatic.common.world.gen.UAFeatures;
import com.minecraftabnormals.upgrade_aquatic.core.registry.UABlocks;
import com.mojang.serialization.Codec;
import com.teamabnormals.abnormals_core.core.library.api.IAddToBiomes;
import com.teamabnormals.abnormals_core.core.utils.MathUtils;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biome.TempCategory;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

public class SearocketFeature extends Feature<NoFeatureConfig> implements IAddToBiomes {
	
	private static final Supplier<BlockState> SEAROCKET(boolean pink) {
		return pink ? () -> UABlocks.PINK_SEAROCKET.get().getDefaultState() : () -> UABlocks.WHITE_SEAROCKET.get().getDefaultState();
	}
	
	public SearocketFeature(Codec<NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}

	@Override
	public boolean func_230362_a_(ISeedReader worldIn, StructureManager manager, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		boolean colorType;
		if(worldIn.getBiome(pos).getTempCategory() == TempCategory.COLD) {
			colorType = rand.nextFloat() <= 0.25F;
			if(SEAROCKET(colorType).get().isValidPosition(worldIn, pos)) {
				this.generateSearocketPatch(worldIn, pos, colorType, rand.nextInt(8));
				return true;
			}
		} else {
			colorType = rand.nextFloat() <= 0.75F;
			if(SEAROCKET(colorType).get().isValidPosition(worldIn, pos)) {
				this.generateSearocketPatch(worldIn, pos, colorType, rand.nextInt(8));
				return true;
			}
		}
		return false;
	}
	
	public void generateSearocketPatch(IWorld world, BlockPos pos, boolean pink, int randomDesign) {
		// 0 - a, 1 - b, 2 - c
		int[] patterns = new int[3];
		switch(randomDesign) {
			default:
			case 0:
				patterns[0] = 3;
				patterns[1] = 4;
				patterns[2] = 9;
			case 1:
				patterns[0] = 2;
				patterns[1] = 8;
				patterns[2] = 5;
			case 2:
				patterns[0] = 3;
				patterns[1] = 4;
				patterns[2] = 7;
			case 3:
				patterns[0] = 3;
				patterns[1] = 13;
				patterns[2] = 12;
			case 4:
				patterns[0] = 3;
				patterns[1] = 12;
				patterns[2] = 12;
			case 5:
				patterns[0] = 2;
				patterns[1] = 12;
				patterns[2] = 12;
			case 6:
				patterns[0] = 3;
				patterns[1] = 5;
				patterns[2] = 3;
			case 7:
				patterns[0] = 3;
				patterns[1] = 3;
				patterns[2] = 6;
		}
		BlockPos startPos = pos;
		MathUtils.Equation r = (theta) -> {
			return (Math.cos(patterns[1] * theta) / patterns[2] + 1) * patterns[0];
		};
		if(!world.isAirBlock(startPos.down()) && !world.isAirBlock(startPos.down(2)) && !world.isAirBlock(startPos.down(3))) {
			int repeatsDown = world.getRandom().nextInt(2) + 2;
			for(int repeats = 0; repeats < repeatsDown; repeats++) {
				pos = pos.add(0, -repeats, 0);
				for (int i = -(patterns[0] / patterns[2] + patterns[0]); i < patterns[0] / patterns[2] + patterns[0]; i++) {
					for (int j = -(patterns[0] / patterns[2] + patterns[0]); j < patterns[0] / patterns[2] + patterns[0]; j++) {
						double radius = r.compute(Math.atan2(j, i));
						BlockPos placingPos = pos.add(i, 0, j);
						if (world.getBlockState(placingPos).getMaterial().isReplaceable() && (i * i + j * j) < radius * radius) {
							if(SEAROCKET(pink).get().isValidPosition(world, placingPos) && world.getFluidState(placingPos).isEmpty()) {
								world.setBlockState(placingPos, SEAROCKET(pink).get(), 2);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public Consumer<Biome> processBiomeAddition() {
		return biome -> {
			if(biome.getCategory() == Category.BEACH) biome.getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(UAFeatures.SEAROCKET.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(15))));
		};
	}
	
}
