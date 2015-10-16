package org.algohub.engine.compiler.java;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * Simple interface to Java compiler using JSR 199 Compiler API.
 */
public final class MemoryJavaCompiler {
  public static final MemoryJavaCompiler INSTANCE = new MemoryJavaCompiler();
  private final javax.tools.JavaCompiler tool;
  private final StandardJavaFileManager stdManager;
  private final MethodHandles.Lookup lookup = MethodHandles.lookup();

  private MemoryJavaCompiler() {
    tool = ToolProvider.getSystemJavaCompiler();
    if (tool == null) {
      throw new IllegalStateException(
          "Could not get Java compiler. Please, ensure " + "that JDK is used instead of JRE.");
    }
    stdManager = tool.getStandardFileManager(null, null, null);
  }

  private static String getClassName(final String qualifiedClassName) {
    final int lastDot = qualifiedClassName.lastIndexOf('.');
    if (lastDot == -1) {
      return qualifiedClassName;
    } else {
      return qualifiedClassName.substring(lastDot + 1);
    }
  }

  /**
   * Compile a single static method.
   */
  public Method compileStaticMethod(final String methodName, final String className,
      final String source) throws ClassNotFoundException, CompileErrorException {
    final Map<String, byte[]> classBytes = compile(className + ".java", source);
    final MemoryClassLoader classLoader = new MemoryClassLoader(classBytes);
    final Class clazz = classLoader.loadClass(className);
    final Method[] methods = clazz.getDeclaredMethods();
    for (final Method method : methods) {
      if (method.getName().equals(methodName)) {
        if (!method.isAccessible()) {
          method.setAccessible(true);
        }
        return method;
      }
    }
    throw new NoSuchMethodError(methodName);
  }

  /**
   * Compile a single static method. <p> Use MethodHandle instead of reflection to gain better
   * performance.</p>
   */
  public MethodHandle compileStaticMethod(final String className, final String methodName,
      final MethodType type, final String source)
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
      CompileErrorException {
    final Map<String, byte[]> classBytes = compile(className + ".java", source);
    final MemoryClassLoader classLoader = new MemoryClassLoader(classBytes);
    //final Class clazz = classLoader.loadClass(className);
    final Class clazz = Class.forName(className, true, classLoader);
    return lookup.findStatic(clazz, methodName, type);
  }

  /**
   * Compile a single normal method.
   *
   * @return [Object, Method], a class instance and the method
   */
  public Object[] compileMethod(final String qualifiedClassName, final String methodName,
      final String source) throws ClassNotFoundException, CompileErrorException {
    final String className = getClassName(qualifiedClassName);
    final Map<String, byte[]> classBytes = compile(className + ".java", source);
    final MemoryClassLoader classLoader = new MemoryClassLoader(classBytes);
    final Class clazz = Class.forName(qualifiedClassName, true, classLoader);
    final Method[] methods = clazz.getDeclaredMethods();

    for (final Method method : methods) {
      if (method.getName().equals(methodName)) {
        try {
          return new Object[] {clazz.newInstance(), method};
        } catch (InstantiationException | IllegalAccessException e) {
          throw new IllegalStateException(e.getMessage());
        }
      }
    }
    throw new NoSuchMethodError(methodName);
  }

  /**
   * Compile a class.
   */
  public Class compileClass(final String qualifiedClassName, final String source)
      throws ClassNotFoundException, CompileErrorException {
    final String className = getClassName(qualifiedClassName);
    final Map<String, byte[]> classBytes = compile(className + ".java", source);
    final MemoryClassLoader classLoader = new MemoryClassLoader(classBytes);
    return classLoader.loadClass(qualifiedClassName);
  }

  public Map<String, byte[]> compile(String fileName, String source) throws CompileErrorException {
    return compile(fileName, source, new PrintWriter(System.err), null, null);
  }

  /**
   * compile given String source and return bytecodes as a Map.
   *
   * @param fileName   source fileName to be used for error messages etc.
   * @param source     Java source as String
   * @param err        error writer where diagnostic messages are written
   * @param sourcePath location of additional .java source files
   * @param classPath  location of additional .class files
   */
  private Map<String, byte[]> compile(String fileName, String source, Writer err, String sourcePath,
      String classPath) throws CompileErrorException {
    // create a new memory JavaFileManager
    MemoryJavaFileManager fileManager = new MemoryJavaFileManager(stdManager);

    // prepare the compilation unit
    List<JavaFileObject> compUnits = new ArrayList<JavaFileObject>(1);
    compUnits.add(fileManager.makeStringSource(fileName, source));

    return compile(compUnits, fileManager, err, sourcePath, classPath);
  }

  private Map<String, byte[]> compile(final List<JavaFileObject> compUnits,
      final MemoryJavaFileManager fileManager, Writer err, String sourcePath, String classPath)
      throws CompileErrorException {
    // javac options
    List<String> options = new ArrayList<String>();
    options.add("-Xlint:all");
    //		options.add("-g:none");
    options.add("-deprecation");
    if (sourcePath != null) {
      options.add("-sourcepath");
      options.add(sourcePath);
    }

    if (classPath != null) {
      options.add("-classpath");
      options.add(classPath);
    }

    // to collect errors, warnings etc.
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
    // create a compilation task
    javax.tools.JavaCompiler.CompilationTask task =
        tool.getTask(err, fileManager, diagnostics, options, null, compUnits);

    if (task.call() == false) {
      final StringBuilder errorMsg = new StringBuilder();
      final List<Diagnostic<? extends JavaFileObject>> diagnostics1 = diagnostics.getDiagnostics();
      for (int i = 1; i < diagnostics1.size(); ++i) { // skipe first one
        errorMsg.append(diagnostics1.get(i));
        errorMsg.append('\n');
      }
      throw new CompileErrorException(errorMsg.toString());
    }

    Map<String, byte[]> classBytes = fileManager.getClassBytes();
    try {
      fileManager.close();
    } catch (IOException exp) {
    }

    return classBytes;
  }
}

