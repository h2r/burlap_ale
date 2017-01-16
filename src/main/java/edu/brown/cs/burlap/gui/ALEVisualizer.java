package edu.brown.cs.burlap.gui;

import burlap.mdp.core.state.State;
import burlap.visualizer.StatePainter;
import burlap.visualizer.Visualizer;
import edu.brown.cs.burlap.ALEState;
import edu.brown.cs.burlap.screen.ScreenConverter;

import java.awt.*;

/**
 * A BURLAP visualizer for ALE.
 *
 * @author Melrose Roderick.
 */
public class ALEVisualizer {

    protected static ScreenConverter converter = new ScreenConverter();

    private ALEVisualizer() {}

    public static Visualizer create() {
        Visualizer vis = new Visualizer();
        vis.addStatePainter(new ALEStatePainter());
        return vis;
    }

    static class ALEStatePainter implements StatePainter {
        @Override
        public void paint(Graphics2D g2, State s, float cWidth, float cHeight) {
            g2.drawImage(converter.convert(((ALEState) s).getScreen()), 0, 0, (int)cWidth, (int)cHeight, null);
        }
    }
}
