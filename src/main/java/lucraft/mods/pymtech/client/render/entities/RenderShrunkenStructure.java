package lucraft.mods.pymtech.client.render.entities;

import lucraft.mods.pymtech.PymTech;
import lucraft.mods.pymtech.client.models.CachedRender;
import lucraft.mods.pymtech.client.render.shrunkenstructure.ShrunkenStructureAccess;
import lucraft.mods.pymtech.entities.EntityShrunkenStructure;
import lucraft.mods.pymtech.items.ItemShrunkenStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = PymTech.MODID, value = Side.CLIENT)
public class RenderShrunkenStructure extends Render<EntityShrunkenStructure> {

    public static Map<Long, CachedRender> renders = new HashMap<>();

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.END && Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.ticksExisted % 600 == 0) {
            renders.clear();
        }
    }

    @SubscribeEvent
    public static void onClientLeaveServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent e) {
        renders.clear();
    }

    @SubscribeEvent
    public static void onClientJoinServer(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        renders.clear();
    }

    public RenderShrunkenStructure(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void doRender(EntityShrunkenStructure entity, double posX, double posY, double posZ, float entityYaw, float partialTicks) {
        if (entity.shrunkenStructure == null)
            return;
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        float scale = entity.prevScale + (entity.getScale() - entity.prevScale) * partialTicks;
        storeAndRender(entity.world, entity.getPosition(), entity.shrunkenStructure, partialTicks, scale, false);
        GlStateManager.popMatrix();
    }

    public static boolean renderStructure(long id, BlockPos size, float scale, World world, float partialTicks, boolean trail) {
        if (!renders.containsKey(id))
            return false;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.translate(-size.getX() / 2D, 0, -size.getZ() / 2D);
        if (!trail) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();

            if (Minecraft.isAmbientOcclusionEnabled()) {
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
            } else {
                GlStateManager.shadeModel(GL11.GL_FLAT);
            }
        }
        renders.get(id).render(world, partialTicks);
        if (!trail) {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.resetColor();
        }
        GlStateManager.popMatrix();
        return true;
    }

    public static void storeAndRender(World world, BlockPos pos, ItemShrunkenStructure.ShrunkenStructure shrunkenStructure, float partialTicks, float scale, boolean trail) {
        if (!renders.containsKey(shrunkenStructure.getId())) {
            CachedRender cachedRender = new CachedRender(DefaultVertexFormats.BLOCK, (VertexFormat format, BufferBuilder buffer, World theWorld, float thePartialTicks) ->
            {
                BlockPos size = shrunkenStructure.getSize();

                for (int x = 0; x < size.getX(); x++) {
                    for (int y = 0; y < size.getY(); y++) {
                        for (int z = 0; z < size.getZ(); z++) {
                            ItemShrunkenStructure.BlockData data = shrunkenStructure.getData()[x][y][z];
                            IBlockState state = data == null ? null : data.getBlock();

                            if (state != null && state.getBlock() != Blocks.AIR && state.getRenderType() != EnumBlockRenderType.INVISIBLE) {
                                final BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                                if (state.getRenderType() == EnumBlockRenderType.MODEL) {
                                    BlockPos p = new BlockPos(x, y, z);
                                    blockrendererdispatcher.getBlockModelRenderer().renderModel(new ShrunkenStructureAccess(world, pos, shrunkenStructure), blockrendererdispatcher.getModelForState(state), state, p, buffer, false);
                                } else if (state.getRenderType() == EnumBlockRenderType.LIQUID) {
                                    BlockPos p = new BlockPos(x, y, z);
                                    blockrendererdispatcher.renderBlock(state, p, theWorld, buffer);
                                }
                            }
                        }
                    }
                }
            });
            renders.put(shrunkenStructure.getId(), cachedRender);
            renderStructure(shrunkenStructure.getId(), shrunkenStructure.getSize(), scale, world, partialTicks, trail);
        } else {
            renderStructure(shrunkenStructure.getId(), shrunkenStructure.getSize(), scale, world, partialTicks, trail);
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityShrunkenStructure entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
