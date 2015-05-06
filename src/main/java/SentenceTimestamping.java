import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dmitry Tishchenko on 06.05.15.
 */
public class SentenceTimestamping {
    private ArrayList<Sentence> sentences;
    private ArrayList<String> plainWords;

    public SentenceTimestamping(List<String> lines) {
        sentences = new ArrayList<Sentence>();
        plainWords = new ArrayList<String>();
        int currentIndex = 0;
        for (String line: lines) {
            String[] sentences = line.split("(?<=[\\.\\?!])");
            for (int sent = 0; sent < sentences.length; sent++) {
                currentIndex = addSentence(sentences[sent].trim(), currentIndex);
            }
        }
    }

    public List<String> getWords() {
        return Collections.unmodifiableList(plainWords);
    }
    public List<Sentence> getSentence(){ return Collections.unmodifiableList(sentences);}

    private int addSentence(String sentence, int startIndex)
    {
        String[] lineWords = sentence.split("\\s+");
        for (int i = 0; i < lineWords.length; i++) {
            lineWords[i] = lineWords[i].replaceAll("[^\\w]", "").toLowerCase();
        }
        Sentence result = new Sentence(sentence, startIndex, lineWords.length);
        sentences.add(result);
        Collections.addAll(plainWords, lineWords);
        return startIndex+lineWords.length;
    }
}
