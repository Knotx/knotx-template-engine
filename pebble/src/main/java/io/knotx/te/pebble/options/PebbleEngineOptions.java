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
package io.knotx.te.pebble.options;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Describes Pebble Knot configuration. Contains cache settings and Pebble Engine settings.
 */
@DataObject(generateConverter = true, publicConverter = false)
public class PebbleEngineOptions {

  private String cacheKeyAlgorithm = "MD5";
  private Long cacheSize;
  private PebbleEngineSyntaxOptions syntax = new PebbleEngineSyntaxOptions();

  public PebbleEngineOptions() {
  }

  public PebbleEngineOptions(JsonObject json) {
    PebbleEngineOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    PebbleEngineOptionsConverter.toJson(this, json);
    return json;
  }

  /**
   * @return size of the cache
   */
  public Long getCacheSize() {
    return cacheSize;
  }

  /**
   * Sets the size of the cache. After reaching the max size, new elements will replace the oldest
   * one.
   *
   * @param cacheSize size of the cache
   * @return a reference to this, so the API can be used fluently
   */
  public PebbleEngineOptions setCacheSize(Long cacheSize) {
    this.cacheSize = cacheSize;
    return this;
  }

  /**
   * @return name of the algorithm used to generate hash from the Pebble snippet
   */
  public String getCacheKeyAlgorithm() {
    return cacheKeyAlgorithm;
  }

  /**
   * Sets the algorithm used to build a hash from the Pebble snippet. The hash is to be used as a
   * cache key.
   *
   * The name should be a standard Java Security name (such as "SHA", "MD5", and so on).
   *
   * @param cacheKeyAlgorithm algorithm name
   * @return a reference to this, so the API can be used fluently
   */
  public PebbleEngineOptions setCacheKeyAlgorithm(String cacheKeyAlgorithm) {
    this.cacheKeyAlgorithm = cacheKeyAlgorithm;
    return this;
  }

  /**
   * @return syntax options with custom delimiters
   */
  public PebbleEngineSyntaxOptions getSyntax() {
    return syntax;
  }

  /**
   * Sets syntax options including custom Pebble markers' delimiters.
   *
   * @param syntax the syntax options to be passed to the Pebble Engine
   */
  public void setSyntax(
      PebbleEngineSyntaxOptions syntax) {
    this.syntax = syntax;
  }

  @Override
  public String toString() {
    return "PebbleEngineOptions{" +
        "cacheKeyAlgorithm='" + cacheKeyAlgorithm + '\'' +
        ", cacheSize=" + cacheSize +
        ", syntax=" + syntax +
        '}';
  }
}
