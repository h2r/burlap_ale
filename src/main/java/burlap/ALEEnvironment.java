package burlap;

import burlap.mdp.core.Domain;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.Environment;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import io.ALEDriver;
import io.Actions;
import io.RLData;
import movie.MovieGenerator;
import org.bytedeco.javacpp.opencv_core.Mat;
import screen.ScreenConverter;

import java.io.IOException;

/**
 * @author Melrose Roderick
 */
public class ALEEnvironment implements Environment {

    MovieGenerator movieGenerator;

    /** The I/O object used to communicate with ALE */
    private ALEDriver io;

    protected ScreenConverter screenConverter;

    /** State data **/
    Domain domain;
    protected ALEState currentState;
    protected double lastReward;
    protected boolean isTerminal;

    public ALEEnvironment(Domain domain, String alePath, String romPath) {
        this(domain, alePath, romPath, 1);
    }

    public ALEEnvironment(Domain domain, String alePath, String romPath, int frameSkip) {
        // Create the relevant I/O objects
        initIO(alePath, romPath, frameSkip);

        screenConverter = new ScreenConverter();

        this.domain = domain;
    }

    public void startRecording(String movieOutputFile) {
        movieGenerator = new MovieGenerator(movieOutputFile);
    }

    protected void recordScreen(Mat screen) {
        // Save screen capture
        if (movieGenerator != null) {
            movieGenerator.record(screenConverter.convert(screen));
        }
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

        // Obtain the screen matrix
        Mat screen = io.getScreen();

        // Get RLData
        RLData rlData = io.getRLData();

        // Update Environment State
        lastReward = rlData.reward;
        isTerminal = rlData.isTerminal;
        currentState = new ALEState(screen);

        // Record Screen for movie
        recordScreen(screen);

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
        // perform reset action
        io.act(Actions.map("system_reset"));
        isTerminal = false;

        // reset initialState
        currentState = new ALEState(io.getScreen());
    }

    /** Initialize the I/O object.
     *
     */
    protected void initIO(String alePath, String romPath, int frameSkip) {
        io = null;

        try {
            // Initialize the pipes; use named pipes if requested
            io = new ALEDriver(alePath, romPath, frameSkip);

            // Determine which information to request from ALE
            io.setUpdateScreen(true);
            io.setUpdateRL(true);
            io.setUpdateRam(false);
            io.initPipes();
        }
        catch (IOException e) {
            System.err.println ("Could not initialize pipes: "+e.getMessage());
            System.exit(-1);
        }
    }
}
