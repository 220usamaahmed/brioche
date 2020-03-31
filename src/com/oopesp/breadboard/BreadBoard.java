package com.oopesp.breadboard;

import com.oopesp.components.Component;
import com.oopesp.components.active.AND_IC;
import com.oopesp.components.active.NOT_IC;
import com.oopesp.components.active.OR_IC;
import com.oopesp.components.passive.LED;
import com.oopesp.components.passive.Wire;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class BreadBoard implements Runnable {

    // Board states
    private final int HIGH_IMPEDANCE = -1;
    private final int LOW = 0;
    private final int HIGH = 5;

    private boolean running = true;

    // Board dimensions
    private final int STRIP_COLUMNS = 50;
    private final int STRIP_ROWS = 10;
    private final int CONNECTED_POS_STRIDE = 5;

    private int[][] boardState;

    private ArrayList<AttachedComponent> attachedComponents = new ArrayList<>();

    public BreadBoard() {
        boardState = new int[STRIP_ROWS][STRIP_COLUMNS];

        // Setting all states to HIGH_IMPEDANCE
        for (int r = 0; r < STRIP_ROWS; r++) {
            for (int c = 0; c < STRIP_COLUMNS; c++) {
                boardState[r][c] = HIGH_IMPEDANCE;
                boardState[r][c] = HIGH_IMPEDANCE;
            }
        }

        Thread thread = new Thread(this);
        thread.start();
    }

    // Resets the bread board with no Components and all HIGH_IMPEDANCE state
    public void reset() {
        attachedComponents = new ArrayList<>();
        for (int r = 0; r < STRIP_ROWS; r++) {
            for (int c = 0; c < STRIP_COLUMNS; c++) {
                boardState[r][c] = HIGH_IMPEDANCE;
                boardState[r][c] = HIGH_IMPEDANCE;
            }
        }
    }

    public void addWire(Wire wire, int[] posTopLeft) {
        attachedComponents.add(new AttachedComponent(wire, posTopLeft));
    }

    public void addLED(LED led, int[] posTopLeft) {
        attachedComponents.add(new AttachedComponent(led, posTopLeft));
    }

    public void addAND_IC(int[] posTopLeft) {
        AND_IC and_ic = new AND_IC();

        // Checking if IC can fit
        if ((posTopLeft[0] + 1) % CONNECTED_POS_STRIDE == 0 && posTopLeft[1] < STRIP_COLUMNS - 8) {
            attachedComponents.add(new AttachedComponent(and_ic, posTopLeft));
        } else {
            System.out.println("Cant add here.");
        }
    }

    public void addOR_IC(int[] posTopLeft) {
        OR_IC or_ic = new OR_IC();

        // Checking if IC can fit
        if ((posTopLeft[0] + 1) % CONNECTED_POS_STRIDE == 0) {
            attachedComponents.add(new AttachedComponent(or_ic, posTopLeft));
        } else {
            System.out.println("Cant add here.");
        }
    }

    public void addNOT_IC(int[] posTopLeft) {
        NOT_IC not_ic = new NOT_IC();

        // Checking if IC can fit
        if ((posTopLeft[0] + 1) % CONNECTED_POS_STRIDE == 0) {
            attachedComponents.add(new AttachedComponent(not_ic, posTopLeft));
        } else {
            System.out.println("Cant add here.");
        }
    }

    public String toString() {
        StringBuilder sbStringToReturn = new StringBuilder();

        for (int r = 0; r < STRIP_ROWS; r++) {
            for (int c = 0; c < STRIP_COLUMNS; c++) {
                sbStringToReturn.append(boardState[r][c]);
                sbStringToReturn.append("\t|\t");
            }
            sbStringToReturn.append(System.getProperty("line.separator"));
        }

        sbStringToReturn.append(System.getProperty("line.separator"));

        for (AttachedComponent c : attachedComponents) {
            sbStringToReturn.append(c.toString());
            sbStringToReturn.append(System.getProperty("line.separator"));
            sbStringToReturn.append(c.getComponent());
            sbStringToReturn.append(System.getProperty("line.separator"));
            sbStringToReturn.append(System.getProperty("line.separator"));
        }

        return sbStringToReturn.toString();
    }

    public void display(Graphics2D graphics2D) {

        // Drawing breadboard connections and displaying states
        for (int r = 0; r < STRIP_ROWS; r++) {
            for (int c = 0; c < STRIP_COLUMNS; c++) {

                if (boardState[r][c] == HIGH) graphics2D.setPaint(Color.RED);
                else if (boardState[r][c] == LOW) graphics2D.setPaint(Color.GREEN);
                else
                    if ((r / CONNECTED_POS_STRIDE) % 2 == 0) graphics2D.setPaint(Color.GRAY);
                    else graphics2D.setPaint(Color.DARK_GRAY);

                Shape circle = new Ellipse2D.Float(c * 16, r * 16, 8, 8);
                graphics2D.draw(circle);
            }
        }

        // Passing graphics2D reference to each component to draw itself
        for (AttachedComponent attachedComponent : attachedComponents) {
            attachedComponent.display(graphics2D);
        }
    }

    // Changes state of connected strip
    private void changeState(int column, int row, int state) {
        int start_row = (int) Math.floor(row / CONNECTED_POS_STRIDE) * CONNECTED_POS_STRIDE;
        for (int i = start_row; i < start_row + CONNECTED_POS_STRIDE; i++) {
            boardState[i][column] = state;
        }
    }

    public void addVCC(int column, int row) {
        changeState(column, row, HIGH);
    }

    public void addGND(int column, int row) {
        changeState(column, row, LOW);
    }

    // Invokes every component with updated states
    private void update() {
        for (AttachedComponent attachedComponent : attachedComponents) {
            int[][] attachments = attachedComponent.getAttachments();
            for (int i = 0; i < attachments.length; i++) {
                attachedComponent.getComponent().updateState(i, boardState[attachments[i][0]][attachments[i][1]]);
            }
        }
    }

    @Override
    public void run() {

        // This loops to simulate state changes

        while (running) {
            try {

                // Adding power to board.
                addVCC(0, 0);
                addGND(1, 0);
                addVCC(0, 6);
                addGND(1, 6);
                update();

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void stopRunning() {
        running = false;
    }

    // This class holds information about added components and their location.
    // It also handle events for each component.
    public class AttachedComponent {
        private Component component;
        private int[][] attachments;

        // TO-DO: This should check if attachment goes out of board and location is empty.

        AttachedComponent(Component c, int[] posTopLeft) {
            component = c;
            // Passing reference of this to use as event handler
            component.setEventListener(this);
            attachments = new int[c.getAttachmentPointsCount()][2];
            int attachmentsAdded = 0;
            int[][] attachmentConfig = c.getAttachmentConfig();
            for (int i = 0; i < attachmentConfig.length; i++) {
                for (int j = 0; j < attachmentConfig[i].length; j++) {
                    if (attachmentConfig[i][j] == 1) {
                        attachments[attachmentsAdded][0] = posTopLeft[0] + i;
                        attachments[attachmentsAdded][1] = posTopLeft[1] + j;
                        attachmentsAdded++;
                    }
                }
            }
        }

        public Component getComponent() {
            return component;
        }

        public int[][] getAttachments() {
            return attachments;
        }

        // A component would invoke this method
        public void handleStateChange(int attachment, int state) {
            changeState(attachments[attachment][1], attachments[attachment][0], state);
        }

        // Displaying the component attached here.
        public void display(Graphics2D graphics2D) {
            this.component.display(graphics2D, attachments[0][1] * 16, attachments[0][0] * 16);
        }

        public String toString() {
            StringBuilder sbAttachedComponent = new StringBuilder();
            for (int[] attachment : attachments) {
                sbAttachedComponent.append(attachment[0]);
                sbAttachedComponent.append(", ");
                sbAttachedComponent.append(attachment[1]);
                sbAttachedComponent.append("\n");
            }

            return sbAttachedComponent.toString();
        }
    }

}
