import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechAligner;
import edu.cmu.sphinx.result.WordResult;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry Tishchenko on 05.05.15.
 */
public class Main {
    public static void main(String[] args) throws IOException {

        String audioPath = args[0];
        String scriptPath = args[1];
        List<WordResult> alignment = getAlignment(audioPath, scriptPath);
        for (WordResult word: alignment) {
            System.out.println(word.getWord().getSpelling());
        }
    }

    private static List<WordResult> getAlignment(String audioPath, String scriptPath) throws IOException {
        Configuration configuration = new Configuration();

        // Set path to acoustic model.
        String amPath = "resource:/edu/cmu/sphinx/models/en-us/en-us";
        // Set path to dictionary.
        String dictPath = "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
        // Set language model.
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.dmp");

        SentenceTimestamping st = new SentenceTimestamping(Files.readAllLines(Paths.get(scriptPath)));

        List<String> transcriptWords = st.getWords();

        SpeechAligner aligner = new SpeechAligner(amPath, dictPath, null);
        URL audioUrl = new File(audioPath).toURI().toURL();
        List<WordResult> wordResult = aligner.align(audioUrl, transcriptWords);
        return wordResult;
    }
}
