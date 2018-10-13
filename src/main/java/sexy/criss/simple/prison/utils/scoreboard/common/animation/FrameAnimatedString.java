/*******************************************************
 * Copyright (C) 2017-2018 d3ever <d3ever@rewforce.cf>
 *
 * This file is part of sexy.
 *
 * sexy can not be copied and/or distributed without the express
 * permission of d3ever
 *
 * Date: 8/27/2018 - 20:15 Monday
 *
 *******************************************************/
package sexy.criss.simple.prison.utils.scoreboard.common.animation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FrameAnimatedString implements AnimatableString {

    protected List<String> frames;
    protected int currentFrame = -1;

    public FrameAnimatedString(String... frames) {
        this.frames = Arrays.asList(frames);
    }

    public FrameAnimatedString(List<String> frames) {
        this.frames = frames;
    }

    public void addFrame(String string) {
        frames.add(string);
    }

    public void setFrame(int frame, String string) {
        frames.set(frame, string);
    }

    public void removeFrame(String string) {
        frames.remove(string);
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public int getTotalLength() {
        return frames.size();
    }

    public String getString(int frame) {
        return frames.get(frame);
    }

    @Override
    public String current() {
        if (currentFrame == -1) return null;
        return frames.get(currentFrame);
    }

    @Override
    public String next() {
        currentFrame++;
        if (currentFrame == frames.size()) currentFrame = 0;
        return frames.get(currentFrame);
    }

    @Override
    public String previous() {
        currentFrame--;
        if (currentFrame == -1) currentFrame = frames.size() - 1;
        return frames.get(currentFrame);
    }

}
