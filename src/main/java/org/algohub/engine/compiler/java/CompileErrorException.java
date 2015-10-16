package org.algohub.engine.compiler.java;

/** Error compiling. */
public class CompileErrorException extends Exception {
  /** Constructor. */
  public CompileErrorException(final String message) {
    super(message);
  }
}
