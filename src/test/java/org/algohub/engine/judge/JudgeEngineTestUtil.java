package org.algohub.engine.judge;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.google.common.io.Resources;

import org.algohub.engine.JudgeEngine;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Question;
import org.algohub.engine.type.LanguageType;
import org.algohub.engine.util.ObjectMapperInstance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public final class JudgeEngineTestUtil {
  private static final JudgeEngine JUDGE_ENGINE = new JudgeEngine();

  private static final ImmutableMap<LanguageType, String> LANGUAGE_TO_EXTENSION =
      ImmutableMap.<LanguageType, String>builder().put(LanguageType.JAVA, ".java")
          .put(LanguageType.JAVASCRIPT, ".js").put(LanguageType.CPLUSPLUS, ".cpp")
          .put(LanguageType.PYTHON, ".py").put(LanguageType.RUBY, ".rb").build();

  public static void batchJudge(final LanguageType languageType) {
    File rootDir = new File("src/test/resources/questions/");
    Pattern pattern = Pattern.compile("\\w+\\" + LANGUAGE_TO_EXTENSION.get(languageType));

    for (final File file : Files.fileTreeTraverser().preOrderTraversal(rootDir)) {

      final Path relativePath = rootDir.toPath().relativize(file.toPath());
      final String fileName = relativePath.getFileName().toString();
      final Matcher matcher = pattern.matcher(fileName);
      if (matcher.matches() && relativePath.getParent().endsWith("solutions")) {
        final String solutionPath = "questions/" + relativePath.toString();
        final String questionId = relativePath.getParent().getParent().toString();
        final String questionPath = "questions/" + questionId + "/" + questionId + ".json";

        judgeOne(questionPath, solutionPath, languageType);
      }
    }
  }

  private static void judgeOne(final String questionPath, final String solutionPath,
      LanguageType languageType) {
    try {
      final String questionStr =
          Resources.toString(Resources.getResource(questionPath), Charsets.UTF_8);
      final Question question =
          ObjectMapperInstance.INSTANCE.readValue(questionStr, Question.class);
      final String pythonCode =
          Resources.toString(Resources.getResource(solutionPath), Charsets.UTF_8);

      final JudgeResult result = JUDGE_ENGINE.judge(question, pythonCode, languageType);
      assertEquals(StatusCode.ACCEPTED.toInt(), result.getStatusCode());
    } catch (InterruptedException | IOException e) {
      fail(e.getMessage());
    }
  }
}
