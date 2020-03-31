package com.oopesp.components.passive;

import com.oopesp.breadboard.BreadBoard;
import com.oopesp.components.Component;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class LED implements Component {

    private int verticalDisplacement;
    private int horizontalDisplacement;

    private int positiveTerminal = 0;
    private int negativeTerminal = 0;

    private boolean turnedOn = false;

    private BreadBoard.AttachedComponent attachedComponent;

    public LED(int hD, int vD) {
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
    public void updateState(int attachment, int state) {
        switch (attachment) {
            case 0:
                positiveTerminal = state;
                break;
            case 1:
                negativeTerminal = state;
                break;
        }

        if (positiveTerminal == 5 && negativeTerminal == 0) turnedOn = true;
        else turnedOn = false;
    }

    @Override
    public void display(Graphics2D graphics2D, int topLeftX, int topLeftY) {
        int lineStartX = topLeftX + 4;
        int lineStartY = topLeftY + 4;
        int lineEndX = lineStartX + horizontalDisplacement * 16;
        int lineEndY = lineStartY + verticalDisplacement * 16;

        graphics2D.setPaint(Color.BLACK);
        Shape line = new Line2D.Float(lineStartX, lineStartY, lineEndX , lineEndY);
        graphics2D.draw(line);

        if (turnedOn) graphics2D.setColor(Color.RED);
        else graphics2D.setColor(Color.GREEN);

        Shape circle = new Ellipse2D.Float((lineStartX + lineEndX)/2 - 8, (lineStartY + lineEndY)/2 - 8, 16, 16);
        graphics2D.fill(circle);
    }

    @Override
    public String toString() {
        return turnedOn + " " + positiveTerminal + " " + negativeTerminal;
    }
}
