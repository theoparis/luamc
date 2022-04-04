package com.theoparis.luamc.client.util;

import com.theoparis.luamc.LuaMod;
import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.internal.ImGuiContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class MinecraftRenderContext {
    private final ImGuiImplGl3 imGuiImplGl3;
    private final ImGuiImplGlfw imGuiImplGlfw;
    private final String glslVersion = "#version 330 core";
    private final RenderCallback renderCallback;

    public interface RenderCallback {
        void render(MatrixStack matrixStack);
    }

    public MinecraftRenderContext(RenderCallback renderCallback) {
        imGuiImplGlfw = new ImGuiImplGlfw();
        imGuiImplGl3 = new ImGuiImplGl3();
        this.renderCallback = renderCallback;
    }


    public void init() {
        Window window = MinecraftClient.getInstance().getWindow();
        ImGuiContext ctx = ImGui.createContext();
        ImGui.setCurrentContext(ctx);
        ImGui.styleColorsDark();
        imGuiImplGlfw.init(window.getHandle(), true);
        imGuiImplGl3.init(glslVersion);

        LuaMod.LOGGER.info("ImGui {} initialized", ImGui.getVersion());
    }

    public void render(MatrixStack matrixStack) {
        imGuiImplGlfw.newFrame();
        ImGui.newFrame();

        renderCallback.render(matrixStack);

        ImGui.render();
        imGuiImplGl3.renderDrawData(ImGui.getDrawData());
    }

    public void destroy() {
        imGuiImplGl3.dispose();
        imGuiImplGlfw.dispose();
        ImGui.destroyContext();
    }

    public LuaTable toLuaValue(MatrixStack matrixStack) {
        // create a lua table containing a createWindow method,
        // that returns a window object containing a draw method, a text method
        LuaTable table = new LuaTable();

        table.set("stack", LuaMatrixStack.toLuaValue(matrixStack));

        table.set("createWindow", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                LuaTable window = new LuaTable();

                ImGui.begin(arg.checkjstring());

                window.set("draw", new OneArgFunction() {
                    @Override
                    public LuaValue call(LuaValue arg) {
                        ImGui.end();
                        return NIL;
                    }
                });

                window.set("text", new OneArgFunction() {
                    @Override
                    public LuaValue call(LuaValue arg) {
                        ImGui.text(arg.checkjstring());
                        return NIL;
                    }
                });

                window.set("button", new OneArgFunction() {
                    @Override
                    public LuaValue call(LuaValue arg) {
                        return LuaValue.valueOf(ImGui.button(arg.checkjstring()));
                    }
                });

                return window;
            }
        });

        return table;
    }
}
