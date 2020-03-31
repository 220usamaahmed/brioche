package com.oopesp.breadboard;

import com.oopesp.components.passive.LED;
import com.oopesp.components.passive.Wire;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GUI extends JFrame implements ActionListener, MouseListener {

    private int activeControl = -1;
    JButton jbAddVCC, jbAddGND, jbAddWire, jbAddLED, jbAddAND, jbAddOR, jbAddNOT, jbResetBoard;

    private static BreadBoard breadBoard;

    public static void main(String[] args) {

        new GUI(new BreadBoard());

    }

    GUI(BreadBoard breadBoard) {

        this.breadBoard = breadBoard;

        this.setSize(800, 360);
        this.setTitle("Brioche");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeControlPanel();
        initializeBreadBoard();

        this.setVisible(true);
    }

    private void initializeBreadBoard() {
        JPanel jpBreadBoardDisplay = new JPanel();
        jpBreadBoardDisplay.setBorder(new EmptyBorder(32, 32, 32, 32));
        jpBreadBoardDisplay.setLayout(new BorderLayout());

        BreadBoardDisplay breadBoardDisplay = new BreadBoardDisplay(breadBoard);
        breadBoardDisplay.addMouseListener(this);

        jpBreadBoardDisplay.add(breadBoardDisplay, BorderLayout.CENTER);
        this.add(jpBreadBoardDisplay, BorderLayout.CENTER);

        jpBreadBoardDisplay.setVisible(true);
    }

    private void initializeControlPanel() {

        // Initializing control buttons

        JPanel jpControlPanel = new JPanel();
        jpControlPanel.setLayout(new FlowLayout());
        jpControlPanel.setBorder(new EmptyBorder(0, 32, 32,32));

        jbAddWire = new JButton("ADD WIRE");
        jbAddWire.addActionListener(this);
        jpControlPanel.add(jbAddWire);

        jbAddLED = new JButton("ADD LED");
        jbAddLED.addActionListener(this);
        jpControlPanel.add(jbAddLED);

        jbAddVCC = new JButton("ADD VCC");
        jbAddVCC.addActionListener(this);
        jpControlPanel.add(jbAddVCC);

        jbAddGND = new JButton("ADD GND");
        jbAddGND.addActionListener(this);
        jpControlPanel.add(jbAddGND);

        jbAddAND = new JButton("ADD AND");
        jbAddAND.addActionListener(this);
        jpControlPanel.add(jbAddAND);

        jbAddOR = new JButton("ADD OR");
        jbAddOR.addActionListener(this);
        jpControlPanel.add(jbAddOR);

        jbAddNOT = new JButton("ADD NOT");
        jbAddNOT.addActionListener(this);
        jpControlPanel.add(jbAddNOT);

        jbResetBoard = new JButton("RESET BOARD");
        jbResetBoard.addActionListener(this);
        jpControlPanel.add(jbResetBoard);

        jpControlPanel.setVisible(true);
        this.add(jpControlPanel, BorderLayout.SOUTH);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int column = e.getX() / 16;
        int row = e.getY() / 16;

        String displacement;
        int vD, hD;

        switch (activeControl) {
            case 0: // Add Wire
                displacement = JOptionPane.showInputDialog("hD,vD");
                vD = Integer.parseInt(displacement.split(",")[1]);
                hD = Integer.parseInt(displacement.split(",")[0]);

                breadBoard.addWire(new Wire(hD, vD), new int[] {row, column});
                break;
            case 1: // Add LED
                displacement = JOptionPane.showInputDialog("hD,vD");
                vD = Integer.parseInt(displacement.split(",")[1]);
                hD = Integer.parseInt(displacement.split(",")[0]);

                breadBoard.addLED(new LED(hD, vD), new int[] {row, column});
                break;
            case 2: // Add VCC
                breadBoard.addVCC(column, row);
                break;
            case 3: // Add GND
                breadBoard.addGND(column, row);
                break;
            case 4: // Add AND
                breadBoard.addAND_IC(new int[] {row, column});
                break;
            case 5: // Add OR
                breadBoard.addOR_IC(new int[] {row, column});
                break;
            case 6: // Add NOT
                breadBoard.addNOT_IC(new int[] {row, column});
                break;

        }

        resetControls();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Set control state to perform next action on mouse click.

        if (e.getSource() == jbAddWire) {
            resetControls();
            jbAddWire.setBackground(Color.ORANGE);
            activeControl = 0;
        } else if (e.getSource() == jbAddLED) {
            resetControls();
            jbAddLED.setBackground(Color.ORANGE);
            activeControl = 1;
        } else if (e.getSource() == jbAddVCC) {
            resetControls();
            jbAddVCC.setBackground(Color.ORANGE);
            activeControl = 2;
        } else if (e.getSource() == jbAddGND) {
            resetControls();
            jbAddGND.setBackground(Color.ORANGE);
            activeControl = 3;
        } else if (e.getSource() == jbAddAND) {
            resetControls();
            jbAddAND.setBackground(Color.ORANGE);
            activeControl = 4;
        } else if (e.getSource() == jbAddOR) {
            resetControls();
            jbAddOR.setBackground(Color.ORANGE);
            activeControl = 5;
        } else if (e.getSource() == jbAddNOT) {
            resetControls();
            jbAddNOT.setBackground(Color.ORANGE);
            activeControl = 6;
        } else if (e.getSource() == jbResetBoard) {
            breadBoard.reset();
        }
    }

    private void resetControls() {
        jbAddWire.setBackground(Color.WHITE);
        jbAddLED.setBackground(Color.WHITE);
        jbAddVCC.setBackground(Color.WHITE);
        jbAddGND.setBackground(Color.WHITE);
        jbAddAND.setBackground(Color.WHITE);
        jbAddOR.setBackground(Color.WHITE);
        jbAddNOT.setBackground(Color.WHITE);
        activeControl = -1;
    }

    // This class draws the board by using a custom JComponent
    private class BreadBoardDisplay extends JComponent implements Runnable {

        BreadBoard breadBoard;

        BreadBoardDisplay (BreadBoard breadBoard) {

            this.breadBoard = breadBoard;

            Thread thread = new Thread(this);
            thread.start();
        }

        public void paint(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics;

            breadBoard.display(graphics2D);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    this.repaint();
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
