package com.fundraiser.animation;

import static org.lwjgl.nanovg.NanoVG.nvgBeginFrame;
import static org.lwjgl.nanovg.NanoVG.nvgEndFrame;

import java.nio.file.Paths;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.system.MemoryStack;

public class TextObject {
    private long vg;
    private String fontName;
    private int fontSize;
    private float[] textColor;
    private String text;
    public TextObject(long vg, String text) {
        this.vg = vg;
        this.text = text;
        
        setFont("roboto", "C:\\Users\\Isaia\\Documents\\Projects\\GIC_number_reveal\\number_reveal\\assets\\fonts\\Roboto\\static\\Roboto-Regular.ttf");
        setTextColor(1f, 1f, 1f);
    }

    public void setFont(String fontName, String fontPath) {
        Paths.get(fontPath);
        NanoVG.nvgCreateFont(vg, fontName, fontPath);
        if (vg == 0)
            throw new RuntimeException("Failed to create NanoVG context");
        this.fontName = fontName;
    }

    public void setFontSize(int fontSize) {this.fontSize = fontSize;}
    public int getFontSize() {return fontSize;}

    public void setTextColor(float r, float g, float b) {textColor = new float[]{r, g, b};}
    public float getR() {return textColor[0];}
    public float getG() {return textColor[1];}
    public float getB() {return textColor[2];}

    public void setText(String text) {this.text = text;}
    public String getText() {return text;}

    public void render(float width, float height) {
        nvgBeginFrame(vg, width, height, 1f);

        NanoVG.nvgFontSize(vg, fontSize);
        NanoVG.nvgFontFace(vg, fontName);
        NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_MIDDLE);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            NVGColor color = NVGColor.malloc(stack);
            color.r(textColor[0]).g(textColor[1]).b(textColor[2]).a(1f);
            NanoVG.nvgFillColor(vg, color);
        }

        NanoVG.nvgText(vg, width / 2f, height / 2f, text);

        nvgEndFrame(vg);
    }
}