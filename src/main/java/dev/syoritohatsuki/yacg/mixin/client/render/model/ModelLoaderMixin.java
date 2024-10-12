package dev.syoritohatsuki.yacg.mixin.client.render.model;

import dev.syoritohatsuki.yacg.config.GeneratorsConfig;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.BlockStatesLoader;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
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

import static dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator.MOD_ID;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
    @Mutable
    @Shadow
    @Final
    private Map<Identifier, JsonUnbakedModel> jsonUnbakedModels;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V"))
    private void appendNonExistGenerators(BlockColors blockColors, Profiler profiler, Map<Identifier, JsonUnbakedModel> jsonUnbakedModels, Map<Identifier, List<BlockStatesLoader.SourceTrackedData>> blockStates, CallbackInfo ci) {
        Map<Identifier, JsonUnbakedModel> _jsonUnbakedModels = new HashMap<>(jsonUnbakedModels);
        GeneratorsConfig.INSTANCE.getTypes().forEach(s -> {
            _jsonUnbakedModels.put(
                    Identifier.of(MOD_ID, "models/block/" + s + ".json"), JsonUnbakedModel.deserialize(String.format(
                            "{\"parent\":\"block/cube_all\"," +
                            "\"textures\":{" +
                            "\"down\":\"yacg:block/%s/bottom\"," +
                            "\"east\":\"yacg:block/%s/left\"," +
                            "\"north\":\"yacg:block/%s/front\"," +
                            "\"south\":\"yacg:block/%s/back\"," +
                            "\"up\":\"yacg:block/%s/top\"," +
                            "\"west\":\"yacg:block/%s/right\"," +
                            "\"particle\":\"yacg:block/%s/back\"" +
                            "}}", s, s, s, s, s, s, s
                    )));
            _jsonUnbakedModels.put(
                    Identifier.of(MOD_ID, "models/item/" + s + ".json"), JsonUnbakedModel.deserialize(String.format(
                            "{\"parent\":\"yacg:block/%s\"}", s
                    )));
        });
        this.jsonUnbakedModels = _jsonUnbakedModels;
    }
}
