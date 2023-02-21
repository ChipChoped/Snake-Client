package states;

public interface State {
    void restart();
    void step();
    void play();
    void pause();
}
