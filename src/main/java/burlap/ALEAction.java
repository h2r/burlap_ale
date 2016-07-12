package burlap;

import burlap.mdp.core.action.Action;
import io.Actions;

/**
 * Created by maroderi on 7/11/16.
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
