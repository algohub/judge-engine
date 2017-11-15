package org.algohub.engine.judge;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.type.LanguageType;
import org.algohub.engine.util.ObjectMapperInstance;
import org.junit.Test;


// for debug only
public class JudgeOneTest {

  // only for debug
  @Test
  public void judgeOne() {
    // judgeOne("partition-list", LanguageType.JAVA);
  }

  private static void judgeOne(final String problemId, LanguageType languageType) {
    final File solutionDir = new File("src/test/resources/solutions/");
    final File problemDir = new File("src/test/resources/problems/");

    try {
      final String problemJson = new String(java.nio.file.Files.readAllBytes(
          Paths.get(problemDir.getAbsolutePath(), problemId + ".json")),
          StandardCharsets.UTF_8);
      final Problem problem = ObjectMapperInstance.INSTANCE.readValue(problemJson, Problem.class);
      final File solutionFile = Paths.get(solutionDir.getAbsolutePath(), problemId).toFile().listFiles(
          (dir, name) -> name.toLowerCase().endsWith("." + languageType.getFileSuffix()))[0];
      final String userCode = new String(java.nio.file.Files.readAllBytes(solutionFile.toPath()),
              StandardCharsets.UTF_8);
      JudgeEngineTestUtil.judgeOne(problem, userCode, languageType);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
