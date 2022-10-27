package gameEngine;

import gameEngine.abstracts.Scene;
import imgui.*;
import imgui.callbacks.ImStrConsumer;
import imgui.callbacks.ImStrSupplier;
import imgui.enums.ImGuiBackendFlags;
import imgui.enums.ImGuiConfigFlags;
import imgui.enums.ImGuiKey;
import imgui.enums.ImGuiMouseCursor;
import imgui.gl3.ImGuiImplGl3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class ImGuiLayer {

    /*
        Basic ImGui demo layer copied directly from GitHub: https://github.com/spair/imgui-java/blob/v1.76-0.9/imgui-lwjgl3/src/test/java/ImGuiGlfwExample.java#L279
     */

    private long window;

    // Mouse cursors provided by GLFW
    private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];

    // LWJGL3 renderer (SHOULD be initialized)
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    public static HashMap<String, ImFont> fonts = new HashMap<>();

    public ImGuiLayer(long window){
        this.window = window;
    }

    // Initialize Dear ImGui.
    public void initImGui() {
        // IMPORTANT!!
        // This line is critical for Dear ImGui to work.
        ImGui.createContext();


        //Initialize ImGuiIO config
        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename("imgui.ini");
        io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Navigation with keyboard
        io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Mouse cursors to display while resizing windows etc.
        io.setBackendPlatformName("imgui_java_impl_glfw");


        //Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.
        final int[] keyMap = new int[ImGuiKey.COUNT];
        keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
        keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
        keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
        keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
        keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
        keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
        keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
        keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
        keyMap[ImGuiKey.End] = GLFW_KEY_END;
        keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
        keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
        keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
        keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
        keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
        keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
        keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
        keyMap[ImGuiKey.A] = GLFW_KEY_A;
        keyMap[ImGuiKey.C] = GLFW_KEY_C;
        keyMap[ImGuiKey.V] = GLFW_KEY_V;
        keyMap[ImGuiKey.X] = GLFW_KEY_X;
        keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
        keyMap[ImGuiKey.Z] = GLFW_KEY_Z;
        io.setKeyMap(keyMap);

        //Mouse cursors mapping
        mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);

        //Handle keyboard input
        glfwSetKeyCallback(window, (w, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                io.setKeysDown(key, true);
            } else if (action == GLFW_RELEASE) {
                io.setKeysDown(key, false);
            }

            io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
            io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
            io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
            io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));
        });

        //Handle special character input
        glfwSetCharCallback(window, (w, c) -> {
            if (c != GLFW_KEY_DELETE) {
                io.addInputCharacter(c);
            }
        });

        //Handle mouse button input
        glfwSetMouseButtonCallback(window, (w, button, action, mods) -> {
            final boolean[] mouseDown = new boolean[5];

            mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
            mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
            mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
            mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
            mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

            io.setMouseDown(mouseDown);

            if (!io.getWantCaptureMouse() && mouseDown[1]) {
                ImGui.setWindowFocus(null);
            }
        });

        //Handle mouse scroll
        glfwSetScrollCallback(window, (w, xOffset, yOffset) -> {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
        });

        io.setSetClipboardTextFn(new ImStrConsumer() {
            @Override
            public void accept(final String s) {
                glfwSetClipboardString(window, s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier() {
            @Override
            public String get() {
                final String clipboardString = glfwGetClipboardString(window);
                if (clipboardString != null) {
                    return clipboardString;
                } else {
                    return "";
                }
            }
        });

        //"Spritesheet" with all chars
        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        //Use standard US ASCII glyph
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());

        //Set font to custom font specified in fonts folder
        boolean usedFont = false;
        File[] assetsFonts = getFonts();
        for (int i = assetsFonts.length-1; i >= 0; i--){
            if (!(assetsFonts[i].isFile() && assetsFonts[i].getPath().toLowerCase().contains(".ttf"))) continue;

            fonts.put(assetsFonts[i].getPath().replace('\\', '/'), fontAtlas.addFontFromFileTTF(assetsFonts[i].getPath(), 15, fontConfig));
            usedFont = true;

        }

        if (!usedFont) assert false : "No fonts of filetype '.ttf' were found in fonts folder";

        fontConfig.destroy(); // After all fonts were added we don't need this config mor


        // ------------------------------------------------------------
        // Use freetype instead of stb_truetype to build a fonts texture
        ImGuiFreeType.buildFontAtlas(fontAtlas, ImGuiFreeType.RasterizerFlags.LightHinting);


        // Method initializes LWJGL3 renderer.
        // This method SHOULD be called after you've initialized your ImGui configuration (fonts and so on).
        // ImGui context should be created as well.
        imGuiGl3.init("#version 330 core");
    }

    public void update(float dt, Scene currentScene){
        startFrame(dt);

        ImGui.newFrame();
        currentScene.sceneImgui();
        ImGui.showDemoWindow();
        ImGui.render();

        endFrame();
    }

    private void startFrame(final float deltaTime) {
        double[] mousePosX = {0};
        double[] mousePosY = {0};
        float[] winWidth = {Window.getWidth()};
        float[] winHeight = {Window.getHeight()};

        glfwGetCursorPos(window, mousePosX, mousePosY);

        // We SHOULD call those methods to update Dear ImGui state for the current frame
        final ImGuiIO io = ImGui.getIO();
        io.setDisplaySize(winWidth[0], winHeight[0]);
        io.setDisplayFramebufferScale(1f, 1f);
        io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
        io.setDeltaTime(deltaTime);

        // Update the mouse cursor
        final int imguiCursor = ImGui.getMouseCursor();
        glfwSetCursor(window, mouseCursors[imguiCursor]);
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    private void endFrame() {
        // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
        imGuiGl3.render(ImGui.getDrawData());
    }

    // If you want to clean a room after yourself - do it by yourself
    private void destroyImGui() {
        imGuiGl3.dispose();
        ImGui.destroyContext();
    }

    //Gets the font specified in assets/fonts
    private File[] getFonts(){
        File fontFolder = new File("assets/fonts");
        if (!fontFolder.exists() || !fontFolder.isDirectory()) assert false : "Expected sub-folder 'fonts' in folder 'assets'";

        File[] subFiles = fontFolder.listFiles();
        if (subFiles == null || subFiles.length == 0) assert false : "No font specified in folder '" + fontFolder.getAbsolutePath() + "'";

        return subFiles;
    }

}
