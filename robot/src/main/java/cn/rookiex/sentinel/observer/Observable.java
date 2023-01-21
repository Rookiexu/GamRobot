package cn.rookiex.sentinel.observer;


/**
 * @author rookieX 2022/12/10
 */
public interface Observable {
    void register(Observer o);

    void remove(Observer o);

    void notify(UpdateEvent message);

}
