package dev.syoritohatsuki.yacg.mixin.registry.tag;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator;
import dev.syoritohatsuki.yacg.util.BuildInGenerators;
import dev.syoritohatsuki.yacg.config.GeneratorsManager;
import net.minecraft.registry.tag.TagGroupLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

import static net.minecraft.registry.tag.BlockTags.PICKAXE_MINEABLE;

@Mixin(TagGroupLoader.class)
public class TagGroupLoaderMixin {
    @Inject(method = "loadTags", at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"))
    private void addGeneratorsToPickaxeMineableTag(ResourceManager resourceManager, CallbackInfoReturnable<Map<Identifier, List<TagGroupLoader.TrackedEntry>>> cir, @Local(ordinal = 1) Identifier identifier, @Local JsonElement jsonElement) {
        if (identifier.equals(PICKAXE_MINEABLE.id())) {
            GeneratorsManager.INSTANCE.getDedicatedGenerators().forEach(id -> jsonElement.getAsJsonObject().get("values").getAsJsonArray().add("yacg:" + id.getNamespace() + "_" + id.getPath()));
            BuildInGenerators.INSTANCE.getBuildInGenerators().forEach((id, settings) -> jsonElement.getAsJsonObject().get("values").getAsJsonArray().add("yacg:" + YetAnotherCobblestoneGenerator.MOD_ID + "_" + id));
        }
    }
}