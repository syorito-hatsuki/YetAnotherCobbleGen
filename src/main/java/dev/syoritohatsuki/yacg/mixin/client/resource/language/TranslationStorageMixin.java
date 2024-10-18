package dev.syoritohatsuki.yacg.mixin.client.resource.language;

import dev.syoritohatsuki.yacg.util.PathUtil;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.resource.Resource;
import net.minecraft.util.Language;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Mixin(TranslationStorage.class)
public class TranslationStorageMixin {
    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "load(Ljava/lang/String;Ljava/util/List;Ljava/util/Map;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Language;load(Ljava/io/InputStream;Ljava/util/function/BiConsumer;)V"))
    private static void putGeneratorsTranslates(String langCode, List<Resource> resourceRefs, Map<String, String> translations, CallbackInfo ci) {
        PathUtil.INSTANCE.getLangFilesPaths(langCode).forEach(path -> {
            try(InputStream inputStream = new FileInputStream(path.toFile())) {
                Language.load(inputStream, translations::put);
            } catch (Throwable ignore) {}
        });
    }
}
