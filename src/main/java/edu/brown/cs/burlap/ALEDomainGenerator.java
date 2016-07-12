package edu.brown.cs.burlap;

import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.singleagent.SADomain;
import edu.brown.cs.burlap.io.Actions;

import java.util.Arrays;

/**
 * Created by MelRod on 3/18/16.
 */
public class ALEDomainGenerator implements DomainGenerator {

    String[] actionNames;

    public ALEDomainGenerator() {
        super();

        this.actionNames = saActionSet();
    }

    public ALEDomainGenerator(String[] actionNames) {
        super();

        this.actionNames = actionNames;
    }

    @Override
    public SADomain generateDomain() {
        SADomain domain = new SADomain();

        // add in NullActions for Domain
        for (String actionName : actionNames) {
            domain.addActionType(new UniversalActionType(new ALEAction(actionName)));
        }

        return domain;
    }


    /* Game specific action-subsets */
    // Single Agent ALE Action Set
    public static String[] saActionSet() {
        return Arrays.copyOfRange(Actions.actionNames, 0, Actions.numPlayerActions);
    }
    // Pong Action Set
    public static String[] pongActionSet() {
        return new String[] {"player_a_noop", "player_a_right", "player_a_left"};
    }
    // Space Invaders Action Set
    public static String[] siActionSet() {
        return new String[] {
                "player_a_noop", "player_a_right", "player_a_left",
                "player_a_fire", "player_a_rightfire", "player_a_leftfire"};
    }
}
