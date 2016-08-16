package corgi.sso.service;

import java.util.List;

/**
 * Created by huanghuanlai on 16/4/28.
 */
public interface IService<T> {

    List<T> getAllObjects();

    void saveObject(T object);

    T getObject(String id);

    void deleteObject(String id);

    void updateObject(T object);

    Long countObject();
}
