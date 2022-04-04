package com.theoparis.luamc.client.util;

import net.minecraft.client.util.math.MatrixStack;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class LuaMatrixStack {
    public static LuaTable toLuaValue(MatrixStack matrixStack) {
        LuaTable table = new LuaTable();

        table.set("translate", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                matrixStack.translate(arg.tofloat(1), arg.tofloat(2), arg.tofloat(3));
                return LuaValue.NIL;
            }
        });
        table.set("scale", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                matrixStack.scale(arg.tofloat(1), arg.tofloat(2), arg.tofloat(3));
                return LuaValue.NIL;
            }
        });
        table.set("push", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                matrixStack.push();
                return LuaValue.NIL;
            }
        });
        table.set("pop", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                matrixStack.pop();
                return LuaValue.NIL;
            }
        });
        
        return table;
    }
}
