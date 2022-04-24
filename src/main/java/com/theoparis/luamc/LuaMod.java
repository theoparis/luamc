package com.theoparis.luamc;

import com.google.gson.JsonObject;
import com.theoparis.luamc.client.util.LuaGson;
import dev.throwouterror.eventbus.SimpleEventBus;
import dev.throwouterror.eventbus.event.Event;
import io.reactivex.rxjava3.functions.Consumer;
import net.fabricmc.api.ModInitializer;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Objects;

public class LuaMod implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final HashMap<String, LuaValue> mods = new HashMap<>();
    public static final SimpleEventBus eventBus = new SimpleEventBus();

    @Override
    public void onInitialize() {
        // Load all mods from the mods folder
        File modsFolder = new File("config/luamc");
        if (!modsFolder.exists())
            modsFolder.mkdirs();

        eventBus.observeEvent("modInit").subscribe(event -> {
            String modName = event.getParameters().get("id").getAsString();

            LOGGER.info("Loaded mod {}", modName);
        });

        for (File mod : Objects.requireNonNull(modsFolder.listFiles())) {
            if (mod.getName().endsWith(".lua")) {
                Globals globals = JsePlatform.standardGlobals();

                try {
                    LuaTable luaEvents = new LuaTable();
                    luaEvents.set("fire", new TwoArgFunction() {
                        @Override
                        public LuaValue call(LuaValue arg1, LuaValue arg2) {
                            LuaTable jsonTable = arg2.checktable();

                            eventBus.fireEvent(new Event(arg1.checkjstring(), LuaGson.toJson(jsonTable)));

                            return LuaValue.NIL;
                        }
                    });

                    globals.set("events", luaEvents);

                    globals.load(
                            FileUtils.readFileToString(
                                    mod, Charset.defaultCharset()
                            )
                    ).call();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
