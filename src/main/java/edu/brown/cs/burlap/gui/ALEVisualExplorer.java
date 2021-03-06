package edu.brown.cs.burlap.gui;

import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.Environment;
import burlap.shell.visual.VisualExplorer;
import burlap.visualizer.Visualizer;
import edu.brown.cs.burlap.ALEAction;
import edu.brown.cs.burlap.ALEDomainConstants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A BURLAP visual explorer for the ALE.
 *
 * @author Melrose Roderick.
 */
public class ALEVisualExplorer extends VisualExplorer {

    protected final int humanFPS = 60;
    protected static final float widthRatio = 6f;
    protected static final float heightRatio = 3f;

    protected KeyboardControl keyboardControl;

    public ALEVisualExplorer(SADomain domain, Environment env, Visualizer painter) {
        this(domain, env, painter, false);
    }

    public ALEVisualExplorer(SADomain domain, Environment env, Visualizer painter, boolean human) {
        super(domain, env, painter,
                (int)(ALEDomainConstants.ALEScreenWidth * widthRatio),
                (int)(ALEDomainConstants.ALEScreenHeight * heightRatio));

        if (human) {
            enableHumanInput();
        }
    }

    protected void enableHumanInput() {
        keyboardControl = new KeyboardControl();
        this.addKeyListener(keyboardControl);
        this.painter.addKeyListener(keyboardControl);
        this.propViewer.addKeyListener(keyboardControl);

        ActionListener execute = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                executeAction(new ALEAction(keyboardControl.toALEAction()));
            }
        };
        Timer timer = new Timer(1000/humanFPS, execute);
        timer.start();

        startLiveStatePolling(1000/humanFPS);
    }
}
