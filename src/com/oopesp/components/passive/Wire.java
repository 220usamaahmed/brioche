package com.oopesp.components.passive;

import com.oopesp.breadboard.BreadBoard;
import com.oopesp.components.Component;

import java.awt.*;
import java.awt.geom.Line2D;

public class Wire implements Component {

    private int verticalDisplacement;
    private int horizontalDisplacement;

    private int terminal1 = 0;
    private int terminal2 = 0;

    private BreadBoard.AttachedComponent attachedComponent;

    public Wire(int hD, int vD) {
        verticalDisplacement = vD;
        horizontalDisplacement = hD;
    }

    @Override
    public int[][] getAttachmentConfig() {
        int[][] attachmentConfig = new int[verticalDisplacement + 1][horizontalDisplacement + 1];
        for (int i = 0; i < verticalDisplacement; i++) {
            for (int j = 0; j < horizontalDisplacement; j++) {
                attachmentConfig[i][j] = 0;
            }
        }

        attachmentConfig[0][0] = 1;
        attachmentConfig[verticalDisplacement][horizontalDisplacement] = 1;

        return attachmentConfig;
    }

    @Override
    public int getAttachmentPointsCount() {
        return 2;
    }

    @Override
    public void setEventListener(BreadBoard.AttachedComponent attachedComponent) {
        this.attachedComponent = attachedComponent;
    }

    @Override
    public void display(Graphics2D graphics2D, int topLeftX, int topLeftY) {
        int lineStartX = topLeftX + 4;
        int lineStartY = topLeftY + 4;
        int lineEndX = lineStartX + horizontalDisplacement * 16;
        int lineEndY = lineStartY + verticalDisplacement * 16;
        graphics2D.setPaint(Color.BLUE);
        Shape line = new Line2D.Float(lineStartX, lineStartY, lineEndX , lineEndY);
        graphics2D.draw(line);
    }

    @Override
    public void updateState(int attachment, int state) {
        switch (attachment) {
            case 0:
                terminal1 = state;
                if (terminal1 != -1) attachedComponent.handleStateChange(1, terminal1);
                break;
            case 1:
                terminal2 = state;
                if (terminal2 != -1) attachedComponent.handleStateChange(0, terminal2);
                break;
        }
    }

}
