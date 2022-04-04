package com.theoparis.luamc.client;

import com.theoparis.luamc.LuaMod;
import com.theoparis.luamc.client.util.MinecraftRenderContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import org.luaj.vm2.LuaValue;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class LuaModClient implements ClientModInitializer {
    private MinecraftRenderContext imGui;

    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            imGui = new MinecraftRenderContext((stack) -> {
                // loop over mods
                LuaMod.mods.values().forEach(mod -> {
                    // Check if the render method is present in the mod luatable
                    LuaValue render = mod.get("render");
                    if (render != null) {
                        mod.get("render").call(imGui.toLuaValue(stack));
                    }
                });
            });
            imGui.init();
        });

        WorldRenderEvents.LAST.register((worldRenderContext) ->
                imGui.render(worldRenderContext.matrixStack()));
    }
}
