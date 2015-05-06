/**
 * Created by Dmitry Tishchenko on 06.05.15.
 */
public class Sentence {
    private final String content;
    private final int index;
    private final int length;

    private long startTiming;
    private long finishTiming;

    public Sentence(String content, int index, int length) {
        this.content = content;
        this.index = index;
        this.length = length;
        startTiming = 0;
        finishTiming = 0;
    }

    public String getContent() {
        return content;
    }

    public int getIndex() {
        return index;
    }

    public int getLength() {
        return length;
    }

    public long getStartTiming() {
        return startTiming;
    }

    public void setStartTiming(long startTiming) {
        this.startTiming = startTiming;
    }

    public long getFinishTiming() {
        return finishTiming;
    }

    public void setFinishTiming(long finishTiming) {
        this.finishTiming = finishTiming;
    }
}
