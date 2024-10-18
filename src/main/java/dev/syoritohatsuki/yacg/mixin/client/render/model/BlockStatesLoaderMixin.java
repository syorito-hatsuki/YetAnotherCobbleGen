package dev.syoritohatsuki.yacg.mixin.client.render.model;

import com.google.gson.JsonParser;
import dev.syoritohatsuki.yacg.util.BuildInGenerators;
import dev.syoritohatsuki.yacg.config.GeneratorsManager;
import dev.syoritohatsuki.yacg.util.JsonTemplates;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.BlockStatesLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator.MOD_ID;

@Mixin(BlockStatesLoader.class)
public class BlockStatesLoaderMixin {
    @Mutable
    @Shadow
    @Final
    private Map<Identifier, List<BlockStatesLoader.SourceTrackedData>> blockStates;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void appendNonExistGenerators(Map<Identifier, List<BlockStatesLoader.SourceTrackedData>> blockStates, Profiler profiler, UnbakedModel missingModel, BlockColors blockColors, BiConsumer<ModelIdentifier, UnbakedModel> onLoad, CallbackInfo ci) {
        Map<Identifier, List<BlockStatesLoader.SourceTrackedData>> _blockStates = new HashMap<>(blockStates);
        GeneratorsManager.INSTANCE.getDedicatedGenerators().forEach(identifier -> _blockStates.put(
                Identifier.of(MOD_ID, "blockstates/" + identifier.getNamespace() + "_" + identifier.getPath() + ".json"),
                List.of(
                        new BlockStatesLoader.SourceTrackedData(
                                MOD_ID,
                                JsonParser.parseString(JsonTemplates.INSTANCE.getBlockState(identifier))
                        )
                )
        ));
        BuildInGenerators.INSTANCE.getBuildInGenerators().keySet().forEach(identifier -> _blockStates.put(
                Identifier.of(MOD_ID, "blockstates/" + MOD_ID + "_" + identifier + ".json"),
                List.of(
                        new BlockStatesLoader.SourceTrackedData(
                                MOD_ID,
                                JsonParser.parseString(JsonTemplates.INSTANCE.getBlockState(Identifier.of(MOD_ID, identifier)))
                        )
                )
        ));
        this.blockStates = _blockStates;
    }
}
