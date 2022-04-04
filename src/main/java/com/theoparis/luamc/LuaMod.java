package com.theoparis.luamc;

import net.fabricmc.api.ModInitializer;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Objects;

public class LuaMod implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final HashMap<String, LuaTable> mods = new HashMap<>();

    @Override
    public void onInitialize() {
        // Load all mods from the mods folder
        File modsFolder = new File("mods");
        if (modsFolder.exists()) {
            for (File mod : Objects.requireNonNull(modsFolder.listFiles())) {
                if (mod.getName().endsWith(".lua")) {
                    Globals globals = JsePlatform.standardGlobals();
                    try {
                        LuaTable modTable = (LuaTable) globals.load(
                                FileUtils.readFileToString(
                                        mod, Charset.defaultCharset()
                                )
                        ).call();
                        LuaValue initFn = modTable.get("init");
                        if (initFn.isfunction()) {
                            initFn.call();
                        } else {
                            LOGGER.warn("Mod " + mod.getName() + " does not have an init function");
                        }
                        mods.put(modTable.get("name").toString(), modTable);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}