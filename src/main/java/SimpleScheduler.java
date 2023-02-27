import java.time.Instant;
import java.time.temporal.TemporalField;
import java.util.Timer;
import java.util.TimerTask;

public class SimpleScheduler {

    public static void main(String[] args) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Schedule task executed at " + Instant.now());

            }
        };

        Timer timer = new Timer();
        timer.schedule(task,0,5000);
    }
}
