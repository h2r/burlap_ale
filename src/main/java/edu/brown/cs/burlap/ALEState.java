package edu.brown.cs.burlap;

import burlap.mdp.core.state.State;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.util.Collections;
import java.util.List;

/**
 * Created by MelRod on 3/18/16.
 */
public class ALEState implements State {

    protected Mat screen;

    public ALEState(Mat screen) {
        this.screen = screen;
    }

    public Mat getScreen() {
        return screen;
    }

    @Override
    public List<Object> variableKeys() {
        return Collections.singletonList((Object) "edu/brown/cs/burlap/screen");
    }

    @Override
    public Object get(Object variableKey) {
        return screen;
    }

    @Override
    public State copy() {
        Mat newScreen = new Mat(screen.rows(), screen.cols(), screen.type());
        screen.copyTo(newScreen);
        return new ALEState(newScreen);
    }
}
