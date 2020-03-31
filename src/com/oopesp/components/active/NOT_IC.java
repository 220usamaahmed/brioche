package com.oopesp.components.active;

import com.oopesp.breadboard.BreadBoard;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class NOT_IC extends IC {

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

            if (states[1] == 5) states[2] = 0;
            else states[2] = 5;

            if (states[3] == 5) states[4] = 0;
            else states[4] = 5;

            if (states[5] == 5) states[6] = 0;
            else states[6] = 5;

            if (states[7] == 5) states[8] = 0;
            else states[8] = 5;

            if (states[9] == 5) states[10] = 0;
            else states[10] = 5;

            if (states[11] == 5) states[12] = 0;
            else states[12] = 5;


            attachedComponent.handleStateChange(2, states[2]);
            attachedComponent.handleStateChange(4, states[4]);
            attachedComponent.handleStateChange(6, states[6]);
            attachedComponent.handleStateChange(8, states[8]);
            attachedComponent.handleStateChange(10, states[10]);
            attachedComponent.handleStateChange(12, states[12]);


        } else {
            setAllToLOW();
        }

    }

    private void setAllToLOW() {
        states[2] = 0;
        states[4] = 0;
        states[6] = 0;
        states[8] = 0;
        states[10] = 0;
        states[11] = 0;
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
        graphics2D.drawString("NOT", topLeftX + 40, topLeftY + 16);
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
