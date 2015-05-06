import javax.swing.*;
import java.io.IOException;

/**
 * Created by Dmitry Tishchenko on 05.05.15.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("SubSynchronize");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UserInterface userInterface = new UserInterface();


        frame.getContentPane().add(userInterface.getPanel());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}

