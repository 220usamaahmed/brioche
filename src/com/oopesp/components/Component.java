package com.oopesp.components;

import com.oopesp.breadboard.BreadBoard;

import java.awt.*;

public interface Component {

    int[][] getAttachmentConfig();
    int getAttachmentPointsCount();
    void updateState(int attachment, int state);
    void setEventListener(BreadBoard.AttachedComponent attachedComponent);
    void display(Graphics2D graphics2D, int topLeftX, int topLeftY);

}
