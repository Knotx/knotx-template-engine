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
package io.knotx.te.core;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import org.apache.commons.lang3.StringUtils;

/**
 * Describes a details of template engine.
 */
@DataObject(generateConverter = true, publicConverter = false)
public class TemplateEngineEntry {

  private String name;
  private JsonObject config;

  /**
   * Create settings from JSON
   *
   * @param json the JSON
   */
  public TemplateEngineEntry(JsonObject json) {
    init();
    TemplateEngineEntryConverter.fromJson(json, this);
    if (StringUtils.isBlank(name)) {
      throw new IllegalStateException("Engine name in engines configuration can not be null!");
    }
  }

  private void init() {
    this.config = new JsonObject();
  }

  /**
   * @return {@link io.knotx.te.api.TemplateEngineFactory} name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the template engine name that identifies this strategy. This name must be exactly
   * the same as the TemplateEngineFactory returns. The name is later used in the Fragment Content
   * to define which template engine should process it. Must be unique.
   *
   * @param name handler factory name
   * @return reference to this, so the API can be used fluently
   */
  public TemplateEngineEntry setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * @return JSON configuration used during {@link io.knotx.te.api.TemplateEngineFactory#create(Vertx,
   * JsonObject)} initialization
   */
  public JsonObject getConfig() {
    return config == null ? new JsonObject() : config;
  }

  /**
   * Sets {@code io.knotx.te.api.TemplateEngine} implementation configuration.
   *
   * @param config handler JSON configuration
   * @return reference to this, so the API can be used fluently
   */
  public TemplateEngineEntry setConfig(JsonObject config) {
    this.config = config;
    return this;
  }
}
