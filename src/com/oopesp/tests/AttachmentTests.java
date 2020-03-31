package com.oopesp.tests;

import com.oopesp.breadboard.BreadBoard;
import com.oopesp.components.passive.LED;
import com.oopesp.components.passive.Wire;

public class AttachmentTests {

    public static void main(String[] args) {

        BreadBoard breadBoard = new BreadBoard();
        breadBoard.addVCC(0, 0);
        breadBoard.addGND(0, 1);

        Wire wire1 = new Wire(3, 5);
        Wire wire2 = new Wire(3, 3);

        int[] wire1PosTopLeft = {0, 0};
        breadBoard.addWire(wire1, wire1PosTopLeft);

        int[] wire2PosTopLeft = {0, 1};
        breadBoard.addWire(wire2, wire2PosTopLeft);

        LED led1 = new LED(1, 2);
        int[] led1PosTopLeft = {0, 0};
        breadBoard.addLED(led1, led1PosTopLeft);

    }

}
