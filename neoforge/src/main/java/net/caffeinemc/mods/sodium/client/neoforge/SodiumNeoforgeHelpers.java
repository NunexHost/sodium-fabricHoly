package net.caffeinemc.mods.sodium.client.neoforge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.caffeinemc.mods.sodium.client.model.color.ColorProviderRegistry;
import net.caffeinemc.mods.sodium.client.model.light.LightPipelineProvider;
import net.caffeinemc.mods.sodium.client.neoforge.iecompat.ImmersiveEngineeringCompat;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.FluidRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.neoforge.FluidRendererImpl;
import net.caffeinemc.mods.sodium.client.services.SodiumPlatformHelpers;
import net.caffeinemc.mods.sodium.client.util.DirectionUtil;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelDataManager;
import org.joml.Matrix4f;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SodiumNeoforgeHelpers implements SodiumPlatformHelpers {
    @Override
    public boolean isBlockTransparent(BlockState block, BlockAndTintGetter level, BlockPos pos, FluidState fluidState) {
        return block.shouldDisplayFluidOverlay(level, pos, fluidState);
    }

    @Override
    public Path getGameDir() {
        return FMLPaths.GAMEDIR.get();
    }

    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public Object getRenderData(Level level, ChunkPos pos, BlockEntity value) {
        return level.getModelDataManager().getAt(pos);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public boolean isFlawlessFramesActive() {
        return false;
    }

    @Override
    public Iterable<RenderType> getMaterials(BlockAndTintGetter level, BakedModel model, BlockState state, BlockPos pos, RandomSource random, Object modelData) {
        return model.getRenderTypes(state, random, (ModelData) modelData);
    }

    @Override
    public List<BakedQuad> getQuads(BlockAndTintGetter level, BlockPos pos, BakedModel model, BlockState state, Direction face, RandomSource random, RenderType renderType, Object modelData) {
        return model.getQuads(state, face, random, (ModelData) modelData, renderType);
    }

    @Override
    public Object getModelData(Object o, BlockPos pos) {
        if ((o instanceof Map)) {
            return ((Map<BlockPos, ModelData>) o).getOrDefault(pos, ModelData.EMPTY);
        } else {
            return ModelData.EMPTY;
        }
    }

    @Override
    public Object getEmptyModelData() {
        return ModelData.EMPTY;
    }

    @Override
    public boolean shouldSkipRender(BlockGetter level, BlockState selfState, BlockState otherState, BlockPos selfPos, Direction facing) {
        return selfState.supportsExternalFaceHiding() && (otherState.hidesNeighborFace(level, selfPos, selfState, DirectionUtil.getOpposite(facing)));
    }

    @Override
    public int getLightEmission(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        return state.getLightEmission(level, pos);
    }

    @Override
    public boolean shouldCopyRenderData() {
        return false;
    }

    @Override
    public boolean renderFluidFromVanilla() {
        return false;
    }

    @Override
    public void runChunkLayerEvents(RenderType renderType, LevelRenderer levelRenderer, PoseStack poseStack, Matrix4f projectionMatrix, int renderTick, Camera camera, Frustum frustum) {
        ForgeHooksClient.dispatchRenderStage(renderType, levelRenderer, poseStack, projectionMatrix, renderTick, camera, frustum);
    }

    @Override
    public FluidRenderer createPlatformFluidRenderer(ColorProviderRegistry colorRegistry, LightPipelineProvider lightPipelineProvider) {
        return new FluidRendererImpl(colorRegistry, lightPipelineProvider);
    }

    @Override
    public TextureAtlasSprite findInBlockAtlas(float u, float v) {
        return SpriteFinderCache.forBlockAtlas().find(u, v);
    }

    @Override
    public boolean isEarlyLoadingScreenActive() {
        return FMLConfig.getBoolConfigValue(FMLConfig.ConfigValue.EARLY_WINDOW_CONTROL);
    }

    @Override
    public Object getProperModelData(BakedModel model, BlockState state, BlockPos pos, LevelSlice slice, Object modelData) {
        return model.getModelData(slice, pos, state, (ModelData) modelData);
    }

    @Override
    public void renderConnectionsInSection(ChunkBuildBuffers buffers, LevelSlice worldSlice, SectionPos position) {
        ImmersiveEngineeringCompat.renderConnectionsInSection(buffers, worldSlice, position);
    }

    @Override
    public boolean shouldRenderIE(SectionPos position) {
        return ImmersiveEngineeringCompat.isLoaded && ImmersiveEngineeringCompat.sectionNeedsRendering(position);
    }
}
