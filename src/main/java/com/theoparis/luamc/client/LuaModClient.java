package com.theoparis.luamc.client;

import com.theoparis.luamc.LuaMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class LuaModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
        });

        WorldRenderEvents.LAST.register((worldRenderContext) -> {
            // loop over mods
            LuaMod.mods.values().forEach(mod -> {

            });
        });
    }
}
