package co.nexus.votingapp.Helpers;

public class Timer {
    public int minutes, seconds;

    public Timer(int minutes, int seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public Timer(){}

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
