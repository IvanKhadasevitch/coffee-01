package utils;

import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonBuilder {
    private static Logger log = Logger.getLogger(SingletonBuilder.class);
    private static volatile ConcurrentHashMap<Class<?>, Object> INSTANCES_MAP = new ConcurrentHashMap<>();

    /**
     *
     * Returns the implementation of the interface in the form of a singleton
     * with a private constructor without arguments from the package: [interface package].impl
     * named: [interface simple name]Impl
     *
     *for example:
     * for interface: dao.CoffeeTypeDao
     * Will be returned implementation: dao.impl.CoffeeTypeDaoImpl as singleton.
     *
     * @param interfaceType value<T> to be associated with the interface implementation
     * @return Returns the implementation of the interface in the form of a singleton
     * with a private constructor without arguments from the package: [interface package].impl
     * named: [interface simple name]Impl
     * @throws SingletonException if interfaceType is null or no implementation for interface
     */
    public static <T> T getInstanceImpl(Class<T> interfaceType) {
        T instanceImpl;

        if (interfaceType == null) {
            log.error("Can't create singleton for interface. Interface type is [null]");
            throw new SingletonException("Interface type is null");
        }

        if (!INSTANCES_MAP.containsKey(interfaceType)) {
            synchronized (SingletonBuilder.class) {
                if (!INSTANCES_MAP.containsKey(interfaceType)) {
                    String implClassName = interfaceType.getPackage().getName()
                                                        .concat(".impl.")
                                                        .concat(interfaceType.getSimpleName())
                                                        .concat("Impl");
                    Constructor constructorImpl = null;
                    try {
                        constructorImpl = Class.forName(implClassName).getDeclaredConstructor();
                        constructorImpl.setAccessible(true);
                        instanceImpl = interfaceType.cast(constructorImpl.newInstance());
                    } catch (NoSuchMethodException | ClassNotFoundException |
                            IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        String errorMessage =  String.format("\n" +
                                        "There is no implementation %s with a private constructor" +
                                        " without arguments for the interface %s \n", implClassName,
                                interfaceType.getCanonicalName());
                        log.error(errorMessage + e.getMessage());
                        throw new SingletonException(errorMessage, e);
                    }
                    INSTANCES_MAP.put(interfaceType, instanceImpl);
                }
            }
        }
        instanceImpl = interfaceType.cast(INSTANCES_MAP.get(interfaceType));

        return instanceImpl;
    }
}

