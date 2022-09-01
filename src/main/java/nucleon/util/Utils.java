package nucleon.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

@UtilityClass
public class Utils {

    private static final int CLASS_MODIFIERS
            = Modifier.classModifiers();

    private static final int CONCRETE_METHOD_MODIFIERS
            = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE
            | Modifier.FINAL | Modifier.SYNCHRONIZED | Modifier.NATIVE | Modifier.STRICT;

    public static boolean isClassReflection(Class<?> class_) {
        return (class_.getModifiers() & ~CLASS_MODIFIERS) == 0;
    }

    public static boolean isConcreteMethod(Method method) {
        return (method.getModifiers() & ~CONCRETE_METHOD_MODIFIERS) == 0;
    }

    public static String getSimpleMethodSignature(Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(method.getDeclaringClass().getSimpleName());
        builder.append(".");
        builder.append(method.getName());
        builder.append("(");
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) builder.append(",");
            Type type = parameters[i].getParameterizedType();
            if (type instanceof Class) {
                Class<?> clazz = (Class<?>) type;
                builder.append(clazz.getSimpleName());
            } else {
                builder.append(type.getTypeName());
            }
        }
        builder.append(")");
        return builder.toString();
    }
}
