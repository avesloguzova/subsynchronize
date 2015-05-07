package ru.spbau.devdays.subsync;

import edu.cmu.sphinx.api.SpeechAligner;
import edu.cmu.sphinx.result.WordResult;
import edu.cmu.sphinx.util.TimeFrame;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by avesloguzova on 5/6/15.
 */
public class SrtGenerator {
    private final SpeechAligner aligner;

    SrtGenerator() throws IOException {
        // Set path to acoustic model.
        String amPath = "resource:/edu/cmu/sphinx/models/en-us/en-us";
        // Set path to dictionary.
        String dictPath = "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
        aligner = new SpeechAligner(amPath, dictPath, null);
    }

    private static String getTime(long millis) {
        long second = TimeUnit.MILLISECONDS.toSeconds(millis);
        long minute = TimeUnit.MILLISECONDS.toMinutes(millis);
        long hour = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.SECONDS.toMillis(second);
        return String.format("%02d:%02d:%02d:%d", hour, minute, second, millis);
    }

    public void generateSrt(String audioPath, String scriptPath, String resultPath) throws IOException {
        SentenceTimestamping sentenceTimestamping = new SentenceTimestamping(Files.readAllLines(Paths.get(scriptPath)));
        String audioPath = AudioEncoder.getAudioFromResource(resourcePath);
        URL audioUrl = new File(audioPath).toURI().toURL();
        List<WordResult> wordResult = aligner.align(audioUrl, sentenceTimestamping.getWords());
        List<TimeFrame> timeFramesWords = interpolateMissing(sentenceTimestamping.getWords(), wordResult);
        setSentenseTiming(timeFramesWords, sentenceTimestamping);
        writeSrt(sentenceTimestamping, resultPath);
    }

    private List<TimeFrame> interpolateMissing(List<String> words, List<WordResult> results) {
        TimestampInterpolator ts = new TimestampInterpolator(words, results);
        return ts.getTnterpolatedTimestamps();
    }

    private void setSentenseTiming(List<TimeFrame> timeFramesWords,
                                   SentenceTimestamping timestamping) {
        for (Sentence sentence : timestamping.getSentence()) {
            sentence.setStartTiming(timeFramesWords.get(sentence.getIndex()).getStart());
            sentence.setFinishTiming(timeFramesWords.get(sentence.getIndex() + sentence.getLength() - 1).getEnd());

        }
    }

    private void writeSrt(SentenceTimestamping timestamping, String resultPath) {

        BufferedWriter out;
        try {
            out = new BufferedWriter(new FileWriter(resultPath, false));
            for (Sentence sentence : timestamping.getSentence()) {
                out.write(sentence.getContent());
                out.newLine();
                out.write(String.format("%s --> %s", getTime(sentence.getStartTiming()), getTime(sentence.getFinishTiming())));
                out.newLine();
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

}
