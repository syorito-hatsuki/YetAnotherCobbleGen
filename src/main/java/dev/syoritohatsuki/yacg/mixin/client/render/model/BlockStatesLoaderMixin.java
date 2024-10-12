package dev.syoritohatsuki.yacg.mixin.client.render.model;

import com.google.gson.JsonParser;
import dev.syoritohatsuki.yacg.config.GeneratorsConfig;
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
        GeneratorsConfig.INSTANCE.getTypes().forEach(s -> _blockStates.put(Identifier.of(MOD_ID, "blockstates/" + s + ".json"), List.of(
                new BlockStatesLoader.SourceTrackedData("yacg", JsonParser.parseString(String.format(
                        "{\"variants\":{" +
                        "\"facing=north\":{\"model\":\"yacg:block/%s\"}," +
                        "\"facing=east\":{\"model\":\"yacg:block/%s\",\"y\":90}," +
                        "\"facing=south\":{\"model\":\"yacg:block/%s\",\"y\":180}," +
                        "\"facing=west\":{\"model\":\"yacg:block/%s\",\"y\":270}" +
                        "}}", s, s, s, s
                )))
        )));
        this.blockStates = _blockStates;
    }
}
