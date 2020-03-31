package com.oopesp.components.active;

import com.oopesp.breadboard.BreadBoard;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class OR_IC extends IC {

    private BreadBoard.AttachedComponent attachedComponent;
    private int[] states = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    @Override
    public int[][] getAttachmentConfig() {
        return new int[][] {{
                1, 1, 1, 1, 1, 1, 1
        },{
                1, 1, 1, 1, 1, 1, 1
        }};
    }

    @Override
    public int getAttachmentPointsCount() {
        return 14;
    }

    @Override
    public void updateState(int attachment, int state) {

        states[attachment] = state;

        if (states[0] == 5 && states[13] == 0) {

            if (states[1] == 5 || states[2] == 5) states[3] = 5;
            else states[3] = 0;

            if (states[4] == 5 || states[5] == 5) states[6] = 5;
            else states[6] = 0;

            if (states[7] == 5 || states[8] == 5) states[9] = 5;
            else states[9] = 0;

            if (states[10] == 5 || states[11] == 5) states[12] = 5;
            else states[12] = 0;

            attachedComponent.handleStateChange(3, states[3]);
            attachedComponent.handleStateChange(6, states[6]);
            attachedComponent.handleStateChange(9, states[9]);
            attachedComponent.handleStateChange(12, states[12]);

        } else {
            setAllToLOW();
        }

    }

    private void setAllToLOW() {
        states[3] = 0;
        states[6] = 0;
        states[9] = 0;
        states[12] = 0;
    }

    @Override
    public void setEventListener(BreadBoard.AttachedComponent attachedComponent) {
        this.attachedComponent = attachedComponent;
    }

    @Override
    public void display(Graphics2D graphics2D, int topLeftX, int topLeftY) {
        graphics2D.setColor(Color.DARK_GRAY);
        Shape rect = new Rectangle2D.Float(topLeftX - 4, topLeftY - 4, 16 * 7, 16 * 2);
        graphics2D.fill(rect);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString("OR", topLeftX + 42, topLeftY + 16);
    }

    @Override
    public String toString() {
        StringBuilder sbStates = new StringBuilder();
        for (int state: states) {
            sbStates.append(state);
            sbStates.append(", ");
        }

        return sbStates.toString();
    }
}
