package cn.rookiex.robot;

import cn.rookiex.robot.gamemanager.Manager;
import cn.rookiex.robot.gamemanager.PlayerManager;
import cn.rookiex.robot.gamemanager.BagManager;
import cn.rookiex.robot.gamemanager.SummonManager;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author rookieX 2023/1/16
 */
@Data
public class GamePlayer implements GameRobot {

    private String name;

    private SummonManager summonManager = new SummonManager();

    private BagManager bagManager = new BagManager();

    private Map<String, PlayerManager> managerMap = Maps.newHashMap();

    @SneakyThrows
    public GamePlayer(){
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Class<?> type = field.getType();
            if (type.isAnnotationPresent(Manager.class)){
                managerMap.put(type.getSimpleName(), (PlayerManager) field.get(this));
            }
        }
        fields = this.getClass().getFields();
        for (Field field : fields) {
            Class<?> type = field.getType();
            if (type.isAnnotationPresent(Manager.class)){
                managerMap.put(type.getSimpleName(), (PlayerManager) field.get(this));
            }
        }
    }


    @Override
    public <T extends PlayerManager> T getManager(Class<T> dataClass) {
        return (T) managerMap.get(dataClass.getSimpleName());
    }
}
