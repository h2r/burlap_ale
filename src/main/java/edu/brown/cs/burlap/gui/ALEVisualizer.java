package edu.brown.cs.burlap.gui;

import burlap.mdp.core.state.State;
import burlap.visualizer.StatePainter;
import burlap.visualizer.Visualizer;
import edu.brown.cs.burlap.ALEState;
import edu.brown.cs.burlap.screen.ScreenConverter;

import java.awt.*;

/**
 * Created by maroderi on 7/11/16.
 */
public class ALEVisualizer {

    private ALEVisualizer() {}

    public static Visualizer create() {
        Visualizer vis = new Visualizer();
        vis.addStatePainter(new ALEStatePainter());
        return vis;
    }

    static class ALEStatePainter implements StatePainter {

        ScreenConverter screenConverter = new ScreenConverter();

        @Override
        public void paint(Graphics2D g2, State s, float cWidth, float cHeight) {
            g2.drawImage(screenConverter.convert(((ALEState) s).getScreen()), 0, 0, (int)cWidth, (int)cHeight, null);
        }
    }
}
