package org.algohub.engine.compiler.java;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;

/**
 * JavaFileManager that keeps compiled .class bytes in memory.
 */
@SuppressWarnings("unchecked") final class MemoryJavaFileManager extends ForwardingJavaFileManager {

  /**
   * Java source file extension.
   */
  private static final String EXT = ".java";

  private Map<String, byte[]> classBytes;

  public MemoryJavaFileManager(JavaFileManager fileManager) {
    super(fileManager);
    classBytes = new HashMap<>();
  }

  static JavaFileObject makeStringSource(String fileName, String code) {
    return new StringInputBuffer(fileName, code);
  }

  static URI toUri(String name) {
    File file = new File(name);
    if (file.exists()) {
      return file.toURI();
    } else {
      try {
        final StringBuilder newUri = new StringBuilder();
        newUri.append("mfm:///");
        newUri.append(name.replace('.', '/'));
        if (name.endsWith(EXT)) {
          newUri.replace(newUri.length() - EXT.length(), newUri.length(), EXT);
        }
        return URI.create(newUri.toString());
      } catch (Exception exp) {
        return URI.create("mfm:///com/sun/script/java/java_source");
      }
    }
  }

  public Map<String, byte[]> getClassBytes() {
    return classBytes;
  }

  public void close() throws IOException {
    classBytes = null;
  }

  public void flush() throws IOException {
  }

  public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className,
      Kind kind, FileObject sibling) throws IOException {
    if (kind == Kind.CLASS) {
      return new ClassOutputBuffer(className);
    } else {
      return super.getJavaFileForOutput(location, className, kind, sibling);
    }
  }

  /**
   * A file object used to represent Java source coming from a string.
   */
  private static class StringInputBuffer extends SimpleJavaFileObject {
    final String code;

    StringInputBuffer(String fileName, String code) {
      super(MemoryJavaFileManager.toUri(fileName), Kind.SOURCE);
      this.code = code;
    }

    public CharBuffer getCharContent(boolean ignoreEncodingErrors) {
      return CharBuffer.wrap(code);
    }
  }


  /**
   * A file object that stores Java bytecode into the classBytes map.
   */
  private class ClassOutputBuffer extends SimpleJavaFileObject {
    private String name;

    ClassOutputBuffer(String name) {
      super(MemoryJavaFileManager.toUri(name), Kind.CLASS);
      this.name = name;
    }

    public OutputStream openOutputStream() {
      return new FilterOutputStream(new ByteArrayOutputStream()) {
        public void close() throws IOException {
          out.close();
          ByteArrayOutputStream bos = (ByteArrayOutputStream) out;
          classBytes.put(name, bos.toByteArray());
        }
      };
    }
  }
}

