/*******************************************************
 * Copyright (C) 2017-2018 d3ever <d3ever@rewforce.cf>
 *
 * This file is part of sexy.
 *
 * sexy can not be copied and/or distributed without the express
 * permission of d3ever
 *
 * Date: 8/27/2018 - 20:16 Monday
 *
 *******************************************************/
package sexy.criss.simple.prison.utils.scoreboard.common.animation;

public class HighlightedString extends FrameAnimatedString {

    protected String context;
    protected String normalFormat;
    protected String highlightFormat;
    protected String prefix = "";
    protected String suffix = "";

    public HighlightedString(String context, String normalFormat, String highlightFormat) {
        super();
        this.context = context;
        this.normalFormat = normalFormat;
        this.highlightFormat = highlightFormat;
        generateFrames();
    }

    public HighlightedString(String context, String normalFormat, String highlightFormat, String prefix, String suffix) {
        super();
        this.context = context;
        this.normalFormat = normalFormat;
        this.highlightFormat = highlightFormat;
        this.prefix = prefix;
        this.suffix = suffix;
        generateFrames();
    }

    protected void generateFrames() {
        int index = 0;
        while (index < context.length()) {
            if (context.charAt(index) != ' ') {
                String highlighted = normalFormat + context.substring(0, index) + highlightFormat + context.charAt(index) + normalFormat + context.substring(index + 1);
                String whole = prefix + highlighted + suffix;
                addFrame(whole);
            } else addFrame(prefix + normalFormat + context + suffix);
            index++;
        }
    }

    public String getContext() {
        return context;
    }

    public String getNormalColor() {
        return normalFormat;
    }

    public String getHighlightColor() {
        return highlightFormat;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

}
