package com.teamabnormals.upgrade_aquatic.common;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class UAProperties {

	public static final Block.Properties DEAD_CORAL_BLOCK = Block.Properties.create(Material.ROCK, MaterialColor.GRAY).hardnessAndResistance(1.5F, 6.0F);
	public static final Block.Properties DEAD_CORAL = Block.Properties.create(Material.ROCK, MaterialColor.GRAY).doesNotBlockMovement().hardnessAndResistance(0F, 0F);
	public static Block.Properties CORAL_BASE(MaterialColor color) {
		return Block.Properties.create(Material.CORAL, color).doesNotBlockMovement().sound(SoundType.CORAL);
	}
	public static final Block.Properties SPINES = Block.Properties.create(Material.PISTON);
	public static final Block.Properties ELDER_PRISMARINE = Block.Properties.create(Material.CIRCUITS, MaterialColor.WHITE_TERRACOTTA).hardnessAndResistance(2.0F, 35F);
}
