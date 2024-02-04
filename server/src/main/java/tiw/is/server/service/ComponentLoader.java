package tiw.is.server.service;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import org.picocontainer.MutablePicoContainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ComponentLoader {

    public static void load(JsonObject component, MutablePicoContainer picoContainer) {
        String className = component.getString("class-name");

        try {
            Class<?> clazz = Class.forName(className);

            if (component.containsKey("factory-type") && component.containsKey("factory-method")) {

                Class<?> factoryClass = Class.forName(component.getString("factory-type"));
                Object factoryInstance = picoContainer.getComponent(Class.forName(component.getString("factory-type")));

                Method method = factoryClass.getMethod(component.getString("factory-method"));
                Object instance = method.invoke(factoryInstance);

                picoContainer.addComponent(instance);
                return;
            }

            if (component.containsKey("params")) {
                JsonArray params = component.getJsonArray("params");
                Object[] constructorArgs = new Object[params.size()];

                for (int i = 0; i < params.size(); i++) {
                    JsonObject param = params.getJsonObject(i);
                    constructorArgs[i] = param.getString("value");
                }

                picoContainer.addComponent(clazz, clazz.getConstructor(String.class, String.class, String.class, String.class).newInstance(constructorArgs));
                return;
            }

            picoContainer.addComponent(clazz);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
