import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechAligner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Dmitry Tishchenko on 05.05.15.
 */
public class Main {
    public static void main(String[] args) {

        Configuration configuration = new Configuration();

        String audioPath = args[0];
        String scriptPath = args[1];

        // Set path to acoustic model.
        String amPath = "resource:/edu/cmu/sphinx/models/en-us/en-us";
        // Set path to dictionary.
        String dictPath = "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
        // Set language model.
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.dmp");

        try {
            SpeechAligner aligner = new SpeechAligner(amPath, dictPath, null);
            aligner.align(new URL(audioPath), "one oh one four two");
        } catch(IOException e) {
            System.out.print(e.getMessage());
        }
    }
}
