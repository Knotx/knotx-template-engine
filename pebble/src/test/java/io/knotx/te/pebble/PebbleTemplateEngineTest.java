/*
 * Copyright (C) 2019 Knot.x Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.knotx.te.pebble;

import static io.knotx.junit5.assertions.KnotxAssertions.assertEqualsIgnoreWhitespace;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.util.concurrent.UncheckedExecutionException;
import com.mitchellbosecke.pebble.error.AttributeNotFoundException;
import com.mitchellbosecke.pebble.error.ParserException;
import com.mitchellbosecke.pebble.error.RootAttributeNotFoundException;
import io.knotx.fragments.api.Fragment;
import io.knotx.junit5.util.FileReader;
import io.knotx.te.pebble.options.PebbleEngineOptions;
import io.knotx.te.pebble.options.PebbleEngineSyntaxOptions;
import io.vertx.core.json.JsonObject;
import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PebbleTemplateEngineTest {

  private static final String TEMPLATE_EMPTY = "templates/empty.peb";
  private static final String TEMPLATE_SAMPLE = "templates/sample.peb";
  private static final String TEMPLATE_SERVICE = "templates/service.peb";
  private static final String TEMPLATE_SERVICE_CUSTOM_SYNTAX = "templates/serviceCustomSyntax.peb";
  private static final String TEMPLATE_UNDEFINED_HELPER = "templates/undefinedHelper.peb";

  private static final String CONTEXT_EMPTY = "data/emptyContext.json";
  private static final String CONTEXT_SAMPLE = "data/sampleContext.json";
  private static final String CONTEXT_SAMPLE_MISSING_FIELD = "data/sampleContextMissingField.json";
  private static final String CONTEXT_SERVICE = "data/serviceContext.json";

  private static final String RESULT_EMPTY_CONTENT = "results/emptyContent";
  private static final String RESULT_EMPTY_CONTEXT = "results/emptyContext";
  private static final String RESULT_SAMPLE = "results/sample";
  private static final String RESULT_SAMPLE_MISSING_FIELD = "results/sampleMissingField";
  private static final String RESULT_SERVICE = "results/service";

  private PebbleEngineOptions options;

  private static Stream<Arguments> passingInDefaultMode() {
    return Stream.of( // template, context, expectedResult
        Arguments.of(TEMPLATE_EMPTY, CONTEXT_SAMPLE, RESULT_EMPTY_CONTENT),
        Arguments.of(TEMPLATE_SAMPLE, CONTEXT_EMPTY, RESULT_EMPTY_CONTEXT),
        Arguments.of(TEMPLATE_SAMPLE, CONTEXT_SAMPLE, RESULT_SAMPLE),
        Arguments.of(TEMPLATE_SERVICE, CONTEXT_SERVICE, RESULT_SERVICE),
        Arguments.of(TEMPLATE_SAMPLE, CONTEXT_SAMPLE_MISSING_FIELD, RESULT_SAMPLE_MISSING_FIELD)
    );
  }

  private static Stream<Arguments> passingInStrictMode() {
    return Stream.of( // template, context, expectedResult
        Arguments.of(TEMPLATE_EMPTY, CONTEXT_SAMPLE, RESULT_EMPTY_CONTENT),
        Arguments.of(TEMPLATE_SAMPLE, CONTEXT_SAMPLE, RESULT_SAMPLE),
        Arguments.of(TEMPLATE_SERVICE, CONTEXT_SERVICE, RESULT_SERVICE)
    );
  }

  private static Stream<Arguments> failingInDefaultMode() {
    return Stream.of( // template, context, exception
        Arguments.of(TEMPLATE_UNDEFINED_HELPER, CONTEXT_EMPTY, UncheckedExecutionException.class,
            ParserException.class)
    );
  }

  private static Stream<Arguments> failingInStrictMode() {
    return Stream.of( // template, context, exception
        Arguments.of(TEMPLATE_SAMPLE, CONTEXT_SAMPLE_MISSING_FIELD, AttributeNotFoundException.class, null),
        Arguments.of(TEMPLATE_SAMPLE, CONTEXT_EMPTY, RootAttributeNotFoundException.class, null),
        Arguments.of(TEMPLATE_UNDEFINED_HELPER, CONTEXT_EMPTY, UncheckedExecutionException.class,
            ParserException.class)
    );
  }

  @BeforeEach
  void setUp() {
    options = new PebbleEngineOptions().setCacheSize(100L);
  }

  @ParameterizedTest
  @MethodSource("passingInDefaultMode")
  @DisplayName("Expect successful template processing in default mode")
  void renderTemplateInDefaultMode(String template, String context, String expectedResult)
      throws IOException {
    final PebbleTemplateEngine templateEngine = new PebbleTemplateEngine(options);

    final Fragment fragment = mockFragmentFromFile(template, context);
    final String result = templateEngine.process(fragment).trim();
    final String expected = FileReader.readText(expectedResult).trim();

    assertEqualsIgnoreWhitespace(expected, result);
  }

  @ParameterizedTest
  @MethodSource("failingInDefaultMode")
  @DisplayName("Expect exception to be thrown when template/context input is invalid in default mode")
  void renderTemplateInDefaultMode(String template, String context,
      Class<Throwable> expectedRootException, Class<Throwable> expectedNestedException)
      throws IOException {
    final PebbleTemplateEngine templateEngine = new PebbleTemplateEngine(options);

    final Fragment fragment = mockFragmentFromFile(template, context);

    Throwable exception = assertThrows(expectedRootException,
        () -> templateEngine.process(fragment));

    if (expectedNestedException != null) {
      assertEquals(expectedNestedException, exception.getCause().getClass());
    }
  }

  @ParameterizedTest
  @MethodSource("passingInStrictMode")
  @DisplayName("Expect successful template processing in strict mode")
  void renderTemplateInStrictMode(String template, String context, String expectedResult)
      throws IOException {
    options.getSyntax().setStrictVariables(true);
    final PebbleTemplateEngine templateEngine = new PebbleTemplateEngine(options);

    final Fragment fragment = mockFragmentFromFile(template, context);
    final String result = templateEngine.process(fragment).trim();
    final String expected = FileReader.readText(expectedResult).trim();

    assertEqualsIgnoreWhitespace(expected, result);
  }

  @ParameterizedTest
  @MethodSource("failingInStrictMode")
  @DisplayName("Expect exception to be thrown when template/context input is invalid in strict mode")
  void renderTemplateInStrictMode(String template, String context,
      Class<Throwable> expectedRootException, Class<Throwable> expectedNestedException)
      throws IOException {
    options.getSyntax().setStrictVariables(true);
    final PebbleTemplateEngine templateEngine = new PebbleTemplateEngine(options);

    final Fragment fragment = mockFragmentFromFile(template, context);

    Throwable exception = assertThrows(expectedRootException,
        () -> templateEngine.process(fragment));

    if (expectedNestedException != null) {
      assertEquals(expectedNestedException, exception.getCause().getClass());
    }
  }

  @Test
  @DisplayName("Expect template with custom syntax to be filled properly in default mode")
  void renderTemplateWithCustomDelimitersInDefaultMode() throws IOException {
    options.setSyntax(getCustomSyntaxOptions());
    final PebbleTemplateEngine templateEngine = new PebbleTemplateEngine(options);

    final Fragment fragment = mockFragmentFromFile(TEMPLATE_SERVICE_CUSTOM_SYNTAX, CONTEXT_SERVICE);
    final String result = templateEngine.process(fragment).trim();
    final String expected = FileReader.readText(RESULT_SERVICE).trim();

    assertEqualsIgnoreWhitespace(expected, result);
  }

  @Test
  @DisplayName("Expect template with custom syntax to be filled properly in strict mode")
  void renderTemplateWithCustomDelimitersInStrictMode() throws IOException {
    PebbleEngineSyntaxOptions syntaxOptions = getCustomSyntaxOptions();
    syntaxOptions.setStrictVariables(true);
    options.setSyntax(syntaxOptions);
    final PebbleTemplateEngine templateEngine = new PebbleTemplateEngine(options);

    final Fragment fragment = mockFragmentFromFile(TEMPLATE_SERVICE_CUSTOM_SYNTAX, CONTEXT_SERVICE);
    final String result = templateEngine.process(fragment).trim();
    final String expected = FileReader.readText(RESULT_SERVICE).trim();

    assertEqualsIgnoreWhitespace(expected, result);
  }

  private Fragment mockFragmentFromFile(String bodyFilePath, String contextFilePath)
      throws IOException {
    final String body = FileReader.readText(bodyFilePath).trim();
    final String context = FileReader.readText(contextFilePath).trim();

    Fragment fragment = new Fragment("snippet", new JsonObject(), body);
    fragment.mergeInPayload(new JsonObject(context));
    return fragment;
  }

  private PebbleEngineSyntaxOptions getCustomSyntaxOptions() {
    PebbleEngineSyntaxOptions customSyntaxOptions = new PebbleEngineSyntaxOptions();
    customSyntaxOptions.setDelimiterCommentOpen("/*");
    customSyntaxOptions.setDelimiterCommentClose("*/");
    customSyntaxOptions.setDelimiterExecuteOpen("<<");
    customSyntaxOptions.setDelimiterExecuteClose(">>");
    customSyntaxOptions.setDelimiterPrintOpen("<|");
    customSyntaxOptions.setDelimiterPrintClose("|>");
    return customSyntaxOptions;
  }

}
