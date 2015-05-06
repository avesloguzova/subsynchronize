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
    private JTextField audioField;
    private JFileChooser addAudioDialog = new JFileChooser();
    private JTextField subscriptField;
    private JFileChooser addScriptDialog = new JFileChooser();;
    private JTextField resultField;
    private JFileChooser addResultDialog = new JFileChooser();;

    private JButton runButton;
    private JButton addAudioButton;
    private JButton addSubscriptButton;
    private JButton addPathToResultButton;

    private JPanel myPanel;
    public UserInterface(){
        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    SrtGenerator generathor = new SrtGenerator();
                    if(!audioField.getText().isEmpty()&&!subscriptField.getText().isEmpty()&&!resultField.getText().isEmpty())
                        generathor.generateSrt(audioField.getText(), subscriptField.getText(), resultField.getText());

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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
                }
            }
        });
        addPathToResultButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = addResultDialog.showOpenDialog(myPanel);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = addScriptDialog.getSelectedFile();
                    resultField.setText(file.getAbsolutePath());
                }
            }
        });
    }

    public Component getPanel() {
        return myPanel;
    }
}
