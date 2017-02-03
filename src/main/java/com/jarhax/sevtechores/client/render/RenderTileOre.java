package com.jarhax.sevtechores.client.render;

import org.lwjgl.opengl.GL11;

import com.jarhax.sevtechores.tileentities.TileEntityOre;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;

public class RenderTileOre extends TileEntitySpecialRenderer<TileEntityOre> {

	@Override
	public void renderTileEntityAt (TileEntityOre te, double x, double y, double z, float partialTicks, int destroyStage) {

		super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GL11.glTranslated(x, y, z);
		GL11.glDisable(GL11.GL_LIGHTING);

		GlStateManager.color(1, 1, 1, 1);
		GL11.glTranslated(0.5, 0, 0.5);
		renderBlockModel(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()), true);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(-x, -y, -z);
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();

	}

	public static void renderBlockModel (World world, BlockPos pos, IBlockState state, boolean translateToOrigin) {

		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		final VertexBuffer wr = Tessellator.getInstance().getBuffer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		if (translateToOrigin) {
			wr.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
		}
		final BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		final BlockModelShapes modelShapes = blockrendererdispatcher.getBlockModelShapes();
		final IBakedModel ibakedmodel = modelShapes.getModelForState(state);
		final IBlockAccess worldWrapper = world;
		for (final BlockRenderLayer layer : BlockRenderLayer.values()) {
			if (state.getBlock().canRenderInLayer(state, layer)) {
				ForgeHooksClient.setRenderLayer(layer);
				blockrendererdispatcher.getBlockModelRenderer().renderModel(worldWrapper, ibakedmodel, state, pos, wr, false);
			}
		}
		ForgeHooksClient.setRenderLayer(null);
		if (translateToOrigin) {
			wr.setTranslation(0, 0, 0);
		}
		Tessellator.getInstance().draw();
	}
}
