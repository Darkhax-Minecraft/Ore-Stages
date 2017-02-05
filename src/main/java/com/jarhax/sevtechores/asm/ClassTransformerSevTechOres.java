package com.jarhax.sevtechores.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.jarhax.sevtechores.STO;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformerSevTechOres implements IClassTransformer {

	@Override
	public byte[] transform (String name, String transformedName, byte[] classBytes) {

		if (transformedName.equals("net.minecraft.world.World")) {

			STO.LOGGER.info("Modifying net.minecraft.world.World#setBlockState");
			final ClassNode clazz = ASMUtils.createClassFromByteArray(classBytes);
			transformSetBlockState(ASMUtils.getMethodFromClass(clazz, ASMUtils.isSrg ? "func_180501_a" : "setBlockState", "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z"));
			STO.LOGGER.info("Modifications to net.minecraft.world.World#setBlockState complete!");
			return ASMUtils.createByteArrayFromClass(clazz, ClassWriter.COMPUTE_MAXS);
		}

		return classBytes;
	}

	/**
	 * Modifies the instructions for World#setBlockState to allow us to manipulate the
	 * placement of the block to a greater extent. The new instructions are added to the
	 * beginning of the method, and no other instructions are modified. The new code looks
	 * something like the following.
	 *
	 * if (WorldGenHandler.preBlockSet(this, pos, newState, flags)) return false;
	 *
	 * @param method A MethodNode representation of the setBlockState instructions.
	 */
	private static void transformSetBlockState (MethodNode method) {

		final InsnList instructions = new InsnList();

		final LabelNode l0 = new LabelNode();
		instructions.add(l0);
		instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
		instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
		instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
		instructions.add(new VarInsnNode(Opcodes.ILOAD, 3));
		instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/jarhax/sevtechores/handler/WorldGenHandler", "preBlockSet", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z", false));

		final LabelNode l1 = new LabelNode();
		instructions.add(new JumpInsnNode(Opcodes.IFEQ, l1));

		final LabelNode l2 = new LabelNode();
		instructions.add(l2);
		instructions.add(new InsnNode(Opcodes.ICONST_0));
		instructions.add(new InsnNode(Opcodes.IRETURN));

		instructions.add(l1);

		method.instructions.insertBefore(method.instructions.getFirst(), instructions);
	}
}