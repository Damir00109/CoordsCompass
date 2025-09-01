package com.damir00109;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.ChunkBuilderMode;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

/**
 * Класс, отвечающий за рендер координат игрока и переключение опций при держании компаса.
 * Также является точкой входа для клиентской части мода.
 */
public class CompassRender {

    public static void onEndClientTick(MinecraftClient client) {
        if (client.player == null) return;

        boolean holdingCompass =
                client.player.getMainHandStack().getItem() == Items.COMPASS || client.player.getOffHandStack().getItem() == Items.COMPASS;
        GameOptions opts = client.options;

        // Включаем или отключаем Reduced Debug Info
        opts.getReducedDebugInfo().setValue(!holdingCompass);

        // Переключаем отображение границ чанков: NONE ↔ NEARBY
        opts.getChunkBuilderMode().setValue(
                holdingCompass
                        ? ChunkBuilderMode.NEARBY
                        : ChunkBuilderMode.NONE
        );

        // Отправляем обновлённые настройки на сервер
        opts.sendClientSettings();

        // Если держим компас, показываем координаты в Action Bar
        if (holdingCompass) {
            int x = client.player.getBlockPos().getX();
            int y = client.player.getBlockPos().getY();
            int z = client.player.getBlockPos().getZ();
            Text coordText = Text.literal(x + " | " + y + " | " + z);
            client.inGameHud.setOverlayMessage(coordText, false);
        }
    }
}