/**
 * Created by Dmitry Tishchenko on 06.05.15.
 */
public class Sentence {
    private final String content;
    private final int index;
    private final int length;
    private int startTiming;
    private int finishTiming;

    public Sentence(String content, int index, int length) {
        this.content = content;
        this.index = index;
        this.length = length;
        startTiming = 0;
        finishTiming = 0;
    }
}
