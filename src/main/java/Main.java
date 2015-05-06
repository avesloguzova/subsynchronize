import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechAligner;
import edu.cmu.sphinx.result.WordResult;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Dmitry Tishchenko on 05.05.15.
 */
public class Main {
    public static void main(String[] args) throws IOException {

        Configuration configuration = new Configuration();

        String audioPath = args[0];
        String scriptPath = args[1];

        // Set path to acoustic model.
        String amPath = "resource:/edu/cmu/sphinx/models/en-us/en-us";
        // Set path to dictionary.
        String dictPath = "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
        // Set language model.
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.dmp");

        String transcript = String.join(System.lineSeparator(), Files.readAllLines(Paths.get(scriptPath)));

        try {
            SpeechAligner aligner = new SpeechAligner(amPath, dictPath, null);
            URL audioUrl = new File(audioPath).toURI().toURL();
            List<WordResult> wordResult = aligner.align(audioUrl, transcript);
        } catch(IOException e) {
            System.out.print(e.getMessage());
        }
    }
}
