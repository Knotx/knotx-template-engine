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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import io.knotx.commons.json.JsonConverter;
import io.knotx.fragments.api.Fragment;
import io.knotx.te.api.TemplateEngine;
import io.knotx.te.pebble.options.PebbleEngineOptions;
import io.knotx.te.pebble.options.PebbleEngineSyntaxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * This class is registered with Service Provider Interface (see META-INF/services)
 */
class PebbleTemplateEngine implements TemplateEngine {

  private static final Logger LOGGER = LoggerFactory.getLogger(PebbleTemplateEngine.class);

  private final PebbleEngine pebbleEngine;
  private final Cache<String, PebbleTemplate> cache;
  private final MessageDigest digest;

  PebbleTemplateEngine(PebbleEngineOptions options) {
    LOGGER.info("<{}> instance created", this.getClass().getSimpleName());
    this.pebbleEngine = createPebbleEngine(options.getSyntax());
    this.cache = createCache(options);
    this.digest = tryToCreateDigest(options);
  }

  @Override
  public String process(Fragment fragment) {
    PebbleTemplate template = getTemplate(fragment);
    traceProcessingFragment(fragment);
    return tryToProcessOnEngine(template, fragment);
  }

  private PebbleTemplate getTemplate(Fragment fragment) {
    try {
      String cacheKey = getCacheKey(fragment);
      return cache.get(cacheKey, () -> {
        traceCompilingFragment(fragment);
        return pebbleEngine.getTemplate(fragment.getBody());
      });
    } catch (ExecutionException e) {
      LOGGER.error("Could not compile fragment [{}]", fragment.abbreviate(), e);
      throw new IllegalStateException(e);
    }
  }

  private String tryToProcessOnEngine(PebbleTemplate template, Fragment fragment) {
    try {
      Map<String, Object> context = JsonConverter.plainMapFrom(fragment.getPayload());
      StringWriter writer = new StringWriter();
      template.evaluate(writer, context);
      return writer.toString();
    } catch (IOException e) {
      LOGGER.error("Could not apply context to fragment [{}]", fragment.abbreviate(), e);
      throw new IllegalStateException(e);
    }
  }

  private String getCacheKey(Fragment fragment) {
    byte[] cacheKeyBytes = digest.digest(fragment.getBody().getBytes(StandardCharsets.UTF_8));
    return new String(cacheKeyBytes);
  }

  private PebbleEngine createPebbleEngine(PebbleEngineSyntaxOptions syntaxOptions) {
    return new PebbleEngine.Builder()
        .loader(new StringLoader())
        .cacheActive(false)
        .strictVariables(syntaxOptions.isStrictVariables())
        .newLineTrimming(syntaxOptions.isNewLineTrimming())
        .syntax(PebbleEngineSyntaxComposer.compose(syntaxOptions))
        .literalDecimalTreatedAsInteger(syntaxOptions.isLiteralDecimalTreatedAsInteger())
        .build();
  }

  private Cache<String, PebbleTemplate> createCache(PebbleEngineOptions options) {
    return CacheBuilder.newBuilder()
        .maximumSize(options.getCacheSize())
        .removalListener(listener -> LOGGER.warn(
            "Cache limit exceeded. Revisit 'cacheSize' setting"))
        .build();
  }

  private MessageDigest tryToCreateDigest(PebbleEngineOptions options) {
    try {
      return MessageDigest.getInstance(options.getCacheKeyAlgorithm());
    } catch (NoSuchAlgorithmException e) {
      LOGGER.error("No such algorithm available {}.", options.getCacheKeyAlgorithm(), e);
      throw new IllegalArgumentException(e);
    }
  }

  private static void traceProcessingFragment(Fragment fragment) {
    if (LOGGER.isTraceEnabled()) {
      LOGGER.trace("Processing with Pebble: {}!", fragment);
    }
  }

  private static void traceCompilingFragment(Fragment fragment) {
    if (LOGGER.isTraceEnabled()) {
      LOGGER.trace("Compiled Pebble fragment [{}]", fragment);
    }
  }

}
