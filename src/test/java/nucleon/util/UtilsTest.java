package nucleon.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class UtilsTest {

    @Test
    public void testGetSimpleMethodSignature() {
        class Case {
            final Method method;
            final String expected;

            Case(Method method, String expected) {
                this.method = method;
                this.expected = expected;
            }
        }

        Collection<Case> cases = new ArrayList<>();

        try {
            cases.add(new Case(
                    Integer.class.getMethod("sum", int.class, int.class),
                    "Integer.sum(int,int)"
            ));
            cases.add(new Case(
                    Comparable.class.getMethod("compareTo", Object.class),
                    "Comparable.compareTo(T)"
            ));
            cases.add(new Case(
                    Arrays.class.getMethod("asList", Object[].class),
                    "Arrays.asList(T[])"
            ));
            cases.add(new Case(
                    Map.class.getMethod("put", Object.class, Object.class),
                    "Map.put(K,V)"
            ));
        } catch (NoSuchMethodException e) {
            throw new InternalError(e);
        }

        cases.forEach(case_ -> Assertions.assertEquals(
                case_.expected,
                Utils.getSimpleMethodSignature(case_.method)
        ));
    }
}
