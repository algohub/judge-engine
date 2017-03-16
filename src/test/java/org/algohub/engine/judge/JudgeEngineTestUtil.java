package org.algohub.engine.judge;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.algohub.engine.JudgeEngine;
import org.algohub.engine.bo.StatusCode;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.type.LanguageType;
import org.algohub.engine.util.ObjectMapperInstance;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

final class JudgeEngineTestUtil {
  private static final JudgeEngine JUDGE_ENGINE = new JudgeEngine();

  static void batchJudge(final LanguageType languageType) {
    final File rootDir = new File("src/test/resources/solutions/");
    final File problemDir = new File("src/test/resources/problems/");
    final Pattern pattern = Pattern.compile("\\w+/\\." + languageType.getFileSuffix());

    try {
      for (final File solutionDir : rootDir.listFiles()) {
        final String problemJson = new String(java.nio.file.Files.readAllBytes(
            Paths.get(problemDir.getAbsolutePath(), solutionDir.getName() + ".json")),
            StandardCharsets.UTF_8);
        final Problem problem = ObjectMapperInstance.INSTANCE.readValue(problemJson,
            Problem.class);

        for (final File solutionFile : solutionDir.listFiles()) {
          final Matcher matcher = pattern.matcher(solutionFile.getName());
          if (!matcher.matches()) continue;

          final String userCode =
              new String(java.nio.file.Files.readAllBytes(solutionFile.toPath()),
                  StandardCharsets.UTF_8);

          judgeOne(problem, userCode, languageType);
        }
      }
    } catch (IOException ex) {
      fail(ex.getMessage());
    }
  }

  private static void judgeOne(final Problem problem, final String userCode,
      LanguageType languageType) {
    final JudgeResult result = JUDGE_ENGINE.judge(problem, userCode, languageType);
    if(StatusCode.ACCEPTED != result.getStatusCode()) {
      try {
        System.err.println(ObjectMapperInstance.INSTANCE.writeValueAsString(result));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }
    assertEquals(StatusCode.ACCEPTED, result.getStatusCode());
  }
}
