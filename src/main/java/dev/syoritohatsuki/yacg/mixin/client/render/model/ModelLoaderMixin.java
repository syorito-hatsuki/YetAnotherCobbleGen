package dev.syoritohatsuki.yacg.mixin.client.render.model;

import dev.syoritohatsuki.yacg.util.BuildInGenerators;
import dev.syoritohatsuki.yacg.config.GeneratorsManager;
import dev.syoritohatsuki.yacg.util.JsonTemplates;
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
        GeneratorsManager.INSTANCE.getDedicatedGenerators().forEach(identifier -> {
            _jsonUnbakedModels.put(Identifier.of(MOD_ID, "models/block/" + identifier.getNamespace() + "_" + identifier.getPath() + ".json"), JsonUnbakedModel.deserialize(JsonTemplates.INSTANCE.getBlockModel(identifier)));
            _jsonUnbakedModels.put(Identifier.of(MOD_ID, "models/block/" + identifier.getNamespace() + "_" + identifier.getPath() + "_off.json"), JsonUnbakedModel.deserialize(JsonTemplates.INSTANCE.getOffBlockModel(identifier)));
            _jsonUnbakedModels.put(Identifier.of(MOD_ID, "models/item/" + identifier.getNamespace() + "_" + identifier.getPath() + ".json"), JsonUnbakedModel.deserialize(JsonTemplates.INSTANCE.getItemModel(identifier)));
        });
        BuildInGenerators.INSTANCE.getBuildInGenerators().keySet().forEach(buildInIdentifier -> {
            _jsonUnbakedModels.put(Identifier.of(MOD_ID, "models/block/" + MOD_ID + "_" + buildInIdentifier + ".json"), JsonUnbakedModel.deserialize(JsonTemplates.INSTANCE.getBlockModel(Identifier.of(MOD_ID, buildInIdentifier))));
            _jsonUnbakedModels.put(Identifier.of(MOD_ID, "models/block/" + MOD_ID + "_" + buildInIdentifier + "_off.json"), JsonUnbakedModel.deserialize(JsonTemplates.INSTANCE.getOffBlockModel(Identifier.of(MOD_ID, buildInIdentifier))));
            _jsonUnbakedModels.put(Identifier.of(MOD_ID, "models/item/" + MOD_ID + "_" + buildInIdentifier + ".json"), JsonUnbakedModel.deserialize(JsonTemplates.INSTANCE.getItemModel(Identifier.of(MOD_ID, buildInIdentifier))));
        });
        this.jsonUnbakedModels = _jsonUnbakedModels;
    }
}
