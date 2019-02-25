import annotations.Controller;
import annotations.RequestMapping;
import controllers.HelloController;
import controllers.PokemonTypeController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@WebServlet(urlPatterns = "/*", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private Map<RequestMapping, Method> uriMappings = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Getting request for " + req.getRequestURI());

        String uri = req.getRequestURI();

        if(uri.equals("/favicon.ico")) {
            return;
        }

        Method method = getMethod(req, uri);

        if (method == null) {
            resp.sendError(404, "no mapping found for request uri /test");
            return;
        }

        try {
            Object controller = method.getDeclaringClass().getDeclaredConstructor().newInstance();

            System.out.println(req.getParameterMap().toString());

            String value;

            if (req.getParameterMap().isEmpty()) {
                value = String.valueOf(method.invoke(controller));
            } else {
                value = String.valueOf(method.invoke(controller, req.getParameterMap()));
            }

            resp.getWriter().print(value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException | RuntimeException e) {
            resp.sendError(500, "exception when calling method someThrowingMethod : some exception message");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Posting request for " + req.getRequestURI());

        String uri = req.getRequestURI();

        Method method = getMethod(req, uri);

        if (method == null) {
            resp.sendError(404, "no mapping found for request uri "+uri);
            return;
        }

        try {
            Object controller = method.getDeclaringClass().getDeclaredConstructor().newInstance();

            BufferedReader bufferedReader = req.getReader();

            String value;

            if(bufferedReader != null && bufferedReader.ready()) {
                StringBuilder parameters = new StringBuilder();
                bufferedReader.lines().forEach(parameter -> parameters.append(parameter.strip()));

                value = String.valueOf(method.invoke(controller, parameters.toString()));
            } else {
                throw new IllegalArgumentException("Post need body parameters");
            }

            resp.getWriter().print(value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException | RuntimeException e) {
            resp.sendError(500, "exception when calling method someThrowingMethod : some exception message");
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.registerController(HelloController.class);
        this.registerController(PokemonTypeController.class);
    }

    private Method getMethod(HttpServletRequest req, String uri) throws IOException {
        String methodType = req.getMethod();
        Method method = null;

        for (Map.Entry<RequestMapping, Method> entryMapping : this.uriMappings.entrySet()) {
            if (entryMapping.getKey().uri().equals(uri) && entryMapping.getKey().method().equals(methodType)) {
                method = entryMapping.getValue();
            }
        }

        return method;
    }

    protected void registerController(Class controllerClass){
        System.out.println("Analysing class " + controllerClass.getName());

        if (!controllerClass.isAnnotationPresent(Controller.class)) {
            System.out.println("ERROR : "+controllerClass.getName());
            throw new IllegalArgumentException();
        } else {
            Arrays.stream(controllerClass.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(this::registerMethod);
        }
    }

    protected void registerMethod(Method method) {
        System.out.println("Registering method " + method.getName());

        if (!method.getReturnType().equals(Void.TYPE)) {
            this.uriMappings.put(method.getAnnotation(RequestMapping.class), method);
        }
    }

    protected Map<RequestMapping, Method> getMappings(){
        return this.uriMappings;
    }

    protected Method getMappingForUri(String uri){
        return this.uriMappings
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().uri().equals(uri))
                .findFirst()
                .orElseThrow(IllegalAccessError::new)
                .getValue();
    }
}