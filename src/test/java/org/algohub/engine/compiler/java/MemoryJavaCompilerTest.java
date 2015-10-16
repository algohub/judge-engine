package org.algohub.engine.compiler.java;


import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"PMD.CommentRequired"})
public class MemoryJavaCompilerTest {

  @Test public void compileStaticMethodTest() throws Throwable {
    final String source =
        "public final class Solution {\n" + "public static String greeting(String name) {\n"
            + "\treturn \"Hello \" + name;\n" + "}\n}\n";
    final Method greeting = MemoryJavaCompiler.INSTANCE.compileStaticMethod("greeting", "Solution", source);
    final Object result = greeting.invoke(null, "soulmachine");
    assertEquals("Hello soulmachine", result);

    final MethodHandle methodHandle = MemoryJavaCompiler.INSTANCE.compileStaticMethod("Solution", "greeting",
        MethodType.methodType(String.class, new Class[] {String.class}), source);
    final String result1 = (String) methodHandle.invokeExact("soulmachine");
    assertEquals("Hello soulmachine", result1);
  }
}
