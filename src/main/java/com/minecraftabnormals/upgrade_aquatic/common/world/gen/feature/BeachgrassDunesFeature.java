package com.minecraftabnormals.upgrade_aquatic.common.world.gen.feature;

import java.util.Random;
import java.util.function.Consumer;

import com.minecraftabnormals.upgrade_aquatic.common.blocks.TallBeachgrassBlock;
import com.minecraftabnormals.upgrade_aquatic.common.world.gen.UAFeatures;
import com.minecraftabnormals.upgrade_aquatic.core.registry.UABlocks;
import com.mojang.serialization.Codec;
import com.teamabnormals.abnormals_core.core.library.api.IAddToBiomes;

import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

public class BeachgrassDunesFeature extends Feature<NoFeatureConfig> implements IAddToBiomes {

	public BeachgrassDunesFeature(Codec<NoFeatureConfig> config) {
		super(config);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean func_230362_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		for(BlockState blockstate = world.getBlockState(pos); (world.isAirBlock(pos) || blockstate.isIn(BlockTags.LEAVES)) && pos.getY() > 0; blockstate = world.getBlockState(pos)) {
			pos = pos.down();
		}
		
		int grassesPlaced = 0;
		
		if(pos.getY() >= world.getSeaLevel() + 6) {
			for(int j = 0; j < 128; ++j) {
				BlockPos blockpos = pos.add(rand.nextInt(4) - rand.nextInt(4), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(4) - rand.nextInt(4));
				if(world.isAirBlock(blockpos) && UABlocks.BEACHGRASS.get().getDefaultState().isValidPosition(world, blockpos)) {
					this.placeBeachgrass(world, blockpos, rand);
					grassesPlaced++;
				}
			}	
		} else {
			return true;
		}

		return grassesPlaced > 0;
	}
	
	private void placeBeachgrass(IWorld world, BlockPos pos, Random rand) {
		if(rand.nextFloat() < 0.30F) {
			TallBeachgrassBlock beachGrass = (TallBeachgrassBlock) UABlocks.TALL_BEACHGRASS.get();
			if(world.isAirBlock(pos) && world.isAirBlock(pos.up())) {
				beachGrass.placeAt(world, pos, 2);
			}
		} else {
			if(world.isAirBlock(pos)) {
				world.setBlockState(pos, UABlocks.BEACHGRASS.get().getDefaultState(), 2);
			}
		}
	}

	@Override
	public Consumer<Biome> processBiomeAddition() {
		return biome -> {
			if(biome.getCategory() == Category.BEACH) {
				biome.getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(UAFeatures.DUNES.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(18))));
			}
		};
	}

}