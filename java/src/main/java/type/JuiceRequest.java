package type;

public record JuiceRequest(String username, int apple, int orange) {

    public JuiceRequest(int apple, int orange) {
        this("default", apple, orange);
    }
}
