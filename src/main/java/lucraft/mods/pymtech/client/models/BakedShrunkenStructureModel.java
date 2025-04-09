package lucraft.mods.pymtech.client.models;

import lucraft.mods.pymtech.items.ItemShrunkenStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

public class BakedShrunkenStructureModel implements IBakedModel {

    public ItemShrunkenStructure.ShrunkenStructure shrunkenStructure;
    protected List<BakedQuad> quadList;
    protected ItemOverrideList overrides;

    public BakedShrunkenStructureModel(ItemShrunkenStructure.ShrunkenStructure shrunkenStructure) {
        this.shrunkenStructure = shrunkenStructure;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        return quadList;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return overrides;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        if (cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND) {
            return Pair.of(this, new TRSRTransformation(new Vector3f(0.25f, 0.35f, -0.3f), new Quat4f(0, 1, 0, 1), null, null).getMatrix());
        }
        if (cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND) {
            return Pair.of(this, new TRSRTransformation(new Vector3f(-0.25f, 0.35f, -0.3f), new Quat4f(0, 1, 0, -1), null, null).getMatrix());
        }
        if (cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
            return Pair.of(this, new TRSRTransformation(new Vector3f(-0.25f, 0.35f, -0.3f), new Quat4f(0, 1, 0, -1), null, null).getMatrix());
        }
        if (cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
            return Pair.of(this, new TRSRTransformation(new Vector3f(0.25f, 0.35f, -0.3f), new Quat4f(0, 1, 0, 1), null, null).getMatrix());
        }
        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI) {
            return Pair.of(this, new TRSRTransformation(new Vector3f(0.2f, 0.35f, -0.3f), new Quat4f(0.0f, 0.0f, -0.5f, 1.0f), null, null).getMatrix());
        }
        if (cameraTransformType == ItemCameraTransforms.TransformType.GROUND) {
            return Pair.of(this, new TRSRTransformation(new Vector3f(0.0f, 0.35f, 0.0f), new Quat4f(0, 0, 0, 1), null, null).getMatrix());
        }
        return net.minecraftforge.client.ForgeHooksClient.handlePerspective(this, cameraTransformType);
    }

    public static List<BakedQuad> createQuads(ItemShrunkenStructure.ShrunkenStructure shrunkenStructure) {
        List<BakedQuad> list = new ArrayList<>();
        Minecraft mc = Minecraft.getMinecraft();
        BlockRendererDispatcher blockrendererdispatcher = mc.getBlockRendererDispatcher();

        for (int x = 0; x < shrunkenStructure.getSize().getX(); x++) {
            for (int y = 0; y < shrunkenStructure.getSize().getY(); y++) {
                for (int z = 0; z < shrunkenStructure.getSize().getZ(); z++) {
                    ItemShrunkenStructure.BlockData block = shrunkenStructure.getData()[x][y][z];

                    if(block != null) {
                        IBlockState state = block.getBlock();

                        if(state.getRenderType() == EnumBlockRenderType.MODEL) {
                            for(EnumFacing facing : EnumFacing.VALUES) {
                                for(BakedQuad quad : blockrendererdispatcher.getModelForState(state).getQuads(state, facing, 0)) {
                                    list.add(new BakedQuad(quad.getVertexData(), quad.getTintIndex(), quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat()));
                                }
                            }
                        }
                    }
                }
            }
        }

        return list;
    }


}
