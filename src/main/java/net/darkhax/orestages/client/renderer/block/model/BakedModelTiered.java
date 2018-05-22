package net.darkhax.orestages.client.renderer.block.model;

import java.util.List;

import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.bookshelf.util.RenderUtils;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

/**
 * This model is used to wrap existing/vanilla models with stage aware models.
 */
public class BakedModelTiered implements IBakedModel {
    
    /**
     * The stage to check for.
     */
    private final String stage;
    
    /**
     * The original model that is being wrapped.
     */
    private final IBakedModel originalModel;
    
    /**
     * The model that should be displayed.
     */
    private final IBakedModel replacementModel;
    
    public BakedModelTiered (String stage, IBlockState originalState, IBlockState replacementState) {
        
        this(stage, RenderUtils.getModelForState(originalState), RenderUtils.getModelForState(replacementState));
    }
    
    public BakedModelTiered (String stage, IBakedModel originalModel, IBakedModel replacementModel) {
        
        this.stage = stage;
        this.originalModel = originalModel;
        this.replacementModel = replacementModel;
    }
    
    /**
     * Used internally to grab the correct model to use. Replacement or original.
     *
     * @return The correct model to use.
     */
    private IBakedModel getCorrectModel () {
        
        return PlayerUtils.getClientPlayer() != null && GameStageHelper.clientHasStage(PlayerUtils.getClientPlayer(), this.stage) ? this.originalModel : this.replacementModel;
    }
    
    public IBakedModel getOriginal () {
        
        return this.originalModel;
    }
    
    @Override
    public List<BakedQuad> getQuads (IBlockState state, EnumFacing side, long rand) {
        
        return this.getCorrectModel().getQuads(state, side, rand);
    }
    
    @Override
    public boolean isAmbientOcclusion () {
        
        return this.getCorrectModel().isAmbientOcclusion();
    }
    
    @Override
    public boolean isGui3d () {
        
        return this.getCorrectModel().isGui3d();
    }
    
    @Override
    public boolean isBuiltInRenderer () {
        
        return this.getCorrectModel().isBuiltInRenderer();
    }
    
    @Override
    public TextureAtlasSprite getParticleTexture () {
        
        return this.getCorrectModel().getParticleTexture();
    }
    
    @Override
    public ItemCameraTransforms getItemCameraTransforms () {
        
        return this.getCorrectModel().getItemCameraTransforms();
    }
    
    @Override
    public ItemOverrideList getOverrides () {
        
        return this.getCorrectModel().getOverrides();
    }
    
    @Override
    public String toString () {
        
        return this.stage + " - " + this.originalModel.toString() + " - " + this.replacementModel.toString();
    }
}
