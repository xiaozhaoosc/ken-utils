package site.kenz.utils;

/**
 * Description: 对象单例模式
 *
 * @author kenzhao
 * @date 2018/8/15 18:10
 */
public abstract class SingletonUtils<T> {

  private T instance;

  protected abstract T newInstance();

  public final T getInstance() {
    if (instance == null) {
      synchronized (SingletonUtils.class) {
        if (instance == null) {
          instance = newInstance();
        }
      }
    }
    return instance;
  }
}