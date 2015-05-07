package ru.spbau.devdays.subsync;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by sasha on 5/6/15.
 */
public class UserInterface {
    private final SrtGenerator generator;
    private JTextField audioField;
    private JFileChooser addAudioDialog = new JFileChooser();
    private JTextField subscriptField;
    private JFileChooser addScriptDialog = new JFileChooser();
    private JTextField resultField;
    private JButton runButton;
    private JButton addAudioButton;
    private JButton addSubscriptButton;
    private JButton popUpButton;
    private JPanel myPanel;

    public UserInterface() throws IOException {
        generator = new SrtGenerator();
        popUpButton = new JButton();
        popUpButton.setText("OK");
        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (!audioField.getText().isEmpty() && !subscriptField.getText().isEmpty() && !resultField.getText().isEmpty())
                    new Runnable() {
                        public void run() {
                            try {
                                generator.generateSrt(audioField.getText(), subscriptField.getText(), resultField.getText());
                                JOptionPane.showMessageDialog(myPanel, "Ready!!!");
                            } catch (IOException e1) {
                                JOptionPane.showMessageDialog(myPanel, "Some error is occurs");
                            }
                        }
                    }.run();

            }
        });
        addAudioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = addAudioDialog.showOpenDialog(myPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = addAudioDialog.getSelectedFile();
                    audioField.setText(file.getAbsolutePath());
                }
            }
        });
        addSubscriptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = addScriptDialog.showOpenDialog(myPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = addScriptDialog.getSelectedFile();
                    subscriptField.setText(file.getAbsolutePath());
                    resultField.setText(file.getAbsolutePath() + ".srt");

                }
            }
        });

    }

    public Component getPanel() {
        return myPanel;
    }
}
