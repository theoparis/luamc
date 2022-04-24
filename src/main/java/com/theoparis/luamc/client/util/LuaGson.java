package com.theoparis.luamc.client.util;

import com.google.gson.JsonObject;
import org.luaj.vm2.*;

public class LuaGson {
    public static JsonObject toJson(LuaTable luaTable) {
        JsonObject jsonObject = new JsonObject();

        for (LuaValue luaKey : luaTable.keys()) {
            String key = luaKey.checkjstring();
            LuaValue value = luaTable.get(luaKey);

            if (value instanceof LuaTable) {
                jsonObject.add(key, toJson((LuaTable) value));
            } else if (value instanceof LuaString) {
                jsonObject.addProperty(key, value.checkjstring());
            } else if (value instanceof LuaNumber) {
                jsonObject.addProperty(key, value.checknumber().tofloat());
            } else if (value instanceof LuaBoolean) {
                jsonObject.addProperty(key, value.checkboolean());
            }
        }

        return jsonObject;
    }
}
