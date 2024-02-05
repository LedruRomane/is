package tiw.is.server.service;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import org.picocontainer.MutablePicoContainer;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;
import tiw.is.vols.livraison.infrastructure.commandBus.IMiddleware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ComponentLoader {

    private ComponentLoader() {}

    /**
     * WOw ~
     * Register components in container.
     * Charge les composants déclarés dans le fichier de config en
     * fonction de leur forme dans le container de l'app.
     * @param component Un jsonObject issu d'un array de appConfiguration.json
     * @param picoContainer gestionnaire.
     */
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

            if (component.containsKey("type") && component.get("type").toString().equals("\"handler-locator\"")) {
                // Instantiate the locator:
                Map<Class, ICommandHandler<Object, ICommand>> handlerLocator = new HashMap<>();
                JsonArray handlers =  component.getJsonArray("arguments");

                for (JsonValue handler : handlers) {
                    String rawHandlerClass = handler.asJsonObject().getString("class-name");
                    Class<ICommandHandler> handlerClass = (Class<ICommandHandler>) Class.forName(rawHandlerClass);
                    // Register the handler as component in the container:
                    picoContainer.addComponent(handlerClass);
                    // Register the handler component in the locator:
                    handlerLocator.put(getCommandFromHandler(handlerClass), picoContainer.getComponent(handlerClass));
                }
                picoContainer.addComponent(handlerLocator);

                return;
            }

            if (component.containsKey("middleware-components")) {
                // Instantiate the collection
                Collection<IMiddleware> middlware = new ArrayList<>();
                JsonArray middlewares = component.getJsonArray("middleware-components");

                for (JsonValue middleware : middlewares) {
                    String rawMiddlewareClass = middleware.asJsonObject().getString("class-name");
                    Class<IMiddleware> middlewareClass = (Class<IMiddleware>) Class.forName(rawMiddlewareClass);
                    // Register the middleware in the container:
                    picoContainer.addComponent(middlewareClass);
                    // Register all middleware for commandBus:
                    middlware.add(picoContainer.getComponent(middlewareClass));
                }
                // Register the CommandBus with all middleware in the container:
                picoContainer.addComponent(clazz.getConstructor(Collection.class).newInstance(middlware));

                return;
            }

            picoContainer.addComponent(clazz);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Récupère la classe Command C associé au CommandHandler (ICommandHandler<R,C>).
     * @param handlerClass Classe CommandHandler.
     * @return Class Command.
     * @throws ClassNotFoundException
     */
    static Class getCommandFromHandler(Class<ICommandHandler> handlerClass) throws ClassNotFoundException {
        return Class.forName(getHandlerInterface(handlerClass).getActualTypeArguments()[1].getTypeName());
    }

    /**
     * Récupère le type CommandHandler parsé en ParameterizedType afin
     * de le manipuler et récupérer ses arguments. cf. getCommandFromHandler.
     * @param myClass CommandHandler class.
     * @return ParameterizedType of CommandHandler class.
     */
    static ParameterizedType getHandlerInterface(Class<?> myClass) {
        Type[] interfaces =  myClass.getGenericInterfaces();
        for (Type type : interfaces) {
            if (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType().getTypeName().equals(ICommandHandler.class.getTypeName())) {
                return (ParameterizedType) type;
            }
        }

        return null;
    }
}
