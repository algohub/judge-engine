package org.algohub.engine.codegenerator;

/** An interface to hold a function. */
interface Indentation {
  /**
   * Append with indentation.
   */
  @SuppressWarnings("PMD.ShortVariable")
  static void append(final StringBuilder sb, final String content,
      final int indent) {
    for (int i = 0; i < indent; ++i) {
      sb.append("    ");
    }
    sb.append(content);
  }
}
