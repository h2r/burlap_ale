package edu.brown.cs.burlap;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.Environment;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import edu.brown.cs.burlap.io.ALEDriver;
import edu.brown.cs.burlap.io.Actions;
import edu.brown.cs.burlap.io.RLData;
import org.bytedeco.javacpp.opencv_core.Mat;

import edu.brown.cs.burlap.io.PoolingMethod;

import java.io.IOException;

import static edu.brown.cs.burlap.io.PoolingMethod.*;

/**
 * A BURLAP Environment for interfacing with ALE.
 *
 * @author Melrose Roderick.
 */
public class ALEEnvironment implements Environment {

    /** The I/O object used to communicate with ALE */
    protected ALEDriver io;

    /** State data **/
    protected ALEState currentState;
    protected double lastReward;
    protected boolean isTerminal;
    protected int currentLives = 0;

    /** If true, mark every time a life is lost as a terminal state */
    public boolean terminateOnEndLife = false;

    public ALEEnvironment(String alePath, String romPath) {
        this(alePath, romPath, 1, POOLING_METHOD_NONE);
    }

    public ALEEnvironment(String alePath, String romPath, int frameSkip, PoolingMethod poolingMethod) {
        this(alePath, romPath, frameSkip, poolingMethod, null);
    }

    public ALEEnvironment(String alePath, String romPath, int frameSkip, PoolingMethod poolingMethod, String recordScreenDir) {
        // Create the relevant I/O objects
        initIO(alePath, romPath, frameSkip, poolingMethod, recordScreenDir);
    }

    @Override
    public State currentObservation() {
        return currentState;
    }

    @Override
    public EnvironmentOutcome executeAction(Action a) {
        return executeAction((ALEAction) a);
    }

    public EnvironmentOutcome executeAction(ALEAction a) {
        // save start state
        State startState = currentState;

        // perform action
        boolean closed = io.act(a.aleCode);
        if (closed) {
            // the FIFO stream was closed
            throw new RuntimeException("ALE FIFO stream closed");
        }

        // Obtain the edu.brown.cs.burlap.screen matrix
        Mat screen = io.getScreen();

        // Get RLData
        RLData rlData = io.getRLData();

        // Update Environment State
        lastReward = rlData.reward;
        isTerminal = rlData.isTerminal;
        currentState = new ALEState(screen);

        if (terminateOnEndLife) {
            if (rlData.isTerminal) {
                isTerminal = true;
                currentLives = 0;
            } else if (rlData.lives != currentLives) {
                isTerminal = true;
                currentLives = rlData.lives;
            }
        } else {
            isTerminal = rlData.isTerminal;
        }

        return new EnvironmentOutcome(startState, a, currentState, lastReward, isInTerminalState());
    }

    @Override
    public double lastReward() {
        return lastReward;
    }

    @Override
    public boolean isInTerminalState() {
        return isTerminal;
    }

    @Override
    public void resetEnvironment() {
        if (terminateOnEndLife) {
            // Only reset ALE if the player has lost all of their lives
            if (currentLives <= 0) {
                // perform reset action
                io.act(Actions.map("system_reset"));
                RLData rlData = io.getRLData();
                currentLives = rlData.lives;

                // check if the number of lives was correctly recorded
                if (currentLives == -1) {
                    throw new RuntimeException("Cannot get the lives from this version of ALE. " +
                            "If you want to enable 'terminateOnEndLife', you need to download our ALE fork: " +
                            "https://github.com/h2r/Arcade-Learning-Environment");
                }
            }
        } else {
            // perform reset action
            io.act(Actions.map("system_reset"));
            isTerminal = false;
        }

        // reset initialState
        currentState = new ALEState(io.getScreen());
    }

	/**
     * Initialize the I/O object.
     * @param alePath path to ale executable directory.
     * @param romPath path to rom.
     * @param frameSkip number of frames that are skipped between action executions.
     * @param poolingMethod the pooling method consecutive frames.
     * @param recordScreenDir the directory in which to save the screens.
     */
    protected void initIO(String alePath, String romPath, int frameSkip, PoolingMethod poolingMethod, String recordScreenDir) {
        io = null;

        try {
            // Initialize the pipes; use named pipes if requested
            io = new ALEDriver(alePath, romPath);

            // Determine which information to request from ALE
            io.setUpdateScreen(true);
            io.setUpdateRL(true);
            io.setUpdateRam(false);
            io.setFrameskip(frameSkip);
            io.setPoolingMethod(poolingMethod);
            io.setRecordScreenDir(recordScreenDir);
            io.initALE();
        }
        catch (IOException e) {
            System.err.println ("Could not initialize pipes: "+e.getMessage());
            System.exit(-1);
        }
    }
}
