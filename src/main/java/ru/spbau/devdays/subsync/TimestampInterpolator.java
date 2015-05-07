package ru.spbau.devdays.subsync;

import edu.cmu.sphinx.result.WordResult;
import edu.cmu.sphinx.util.TimeFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Dmitry Tishchenko on 06.05.15.
 */
public class TimestampInterpolator {
    private ArrayList<TimeFrame> timestamps;
    private int missing;
    private long currentTimestamp;

    public TimestampInterpolator(List<String> words, List<WordResult> results) {
        currentTimestamp = 0;
        missing = 0;
        timestamps = new ArrayList<TimeFrame>();
        ArrayList<TimeFrame> interpolated = new ArrayList<TimeFrame>();
        ListIterator<String> wordsIterator = words.listIterator();
        ListIterator<WordResult> resultsIterator = results.listIterator();
        do {
            TimeFrame syncPoint = sync(wordsIterator, resultsIterator);
            addMissing(syncPoint);
            currentTimestamp = syncPoint.getEnd(); //switch these lines
            timestamps.add(syncPoint);
        }
        while (resultsIterator.hasNext());
        if (wordsIterator.hasNext()) {
            addTrailingMissing(wordsIterator);
        }
    }

    private void addMissing(TimeFrame syncPoint) {
        long missingInterval = syncPoint.getStart() - currentTimestamp;
        missingInterval /= missing + 1;
        long currentTime = currentTimestamp;
        for (int i = 0; i < missing; i++) {
            currentTime += missingInterval;
            timestamps.add(new TimeFrame(currentTime, currentTime));
        }

    }

    private TimeFrame sync(ListIterator<String> words, ListIterator<WordResult> results) {
        WordResult currentResult = results.next();
        String currentWord;
        missing = 0;
        do {
            currentWord = words.next();
            if (!currentWord.equals(currentResult.getWord().getSpelling())) {
                missing++;
            }
        } while (!currentWord.equals(currentResult.getWord().getSpelling()));

        return currentResult.getTimeFrame();
    }

    private void addTrailingMissing(ListIterator<String> wordsIterator) {
        TimeFrame lastStamp = timestamps.get(timestamps.size()-1);
        long lastInterval = lastStamp.getEnd() - lastStamp.getStart();
        do {
            timestamps.add(new TimeFrame(currentTimestamp, currentTimestamp+lastInterval));
            currentTimestamp+=lastInterval;
            wordsIterator.next();
        } while(wordsIterator.hasNext());
    }

    public List<TimeFrame> getTnterpolatedTimestamps() {
        return timestamps;
    }
}
