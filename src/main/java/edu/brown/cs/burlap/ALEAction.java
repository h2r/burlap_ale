package edu.brown.cs.burlap;

import burlap.mdp.core.action.Action;
import edu.brown.cs.burlap.io.Actions;

/**
 * A BURLAP action for ALE actions.
 *
 * @author Melrose Roderick.
 */
public class ALEAction implements Action {

    public int aleCode;
    public String actionName;

    public ALEAction(String actionName) {
        this.actionName = actionName;
        this.aleCode = Actions.map(actionName);
    }

    public ALEAction(int aleCode, String actionName) {
        this.aleCode = aleCode;
        this.actionName = actionName;
    }

    public ALEAction(int aleCode) {
        this.aleCode = aleCode;
        this.actionName = Actions.actionNames[aleCode];
    }

    @Override
    public String actionName() {
        return actionName;
    }

    @Override
    public Action copy() {
        return new ALEAction(aleCode, actionName);
    }

    @Override
    public String toString() {
        return actionName;
    }
}
