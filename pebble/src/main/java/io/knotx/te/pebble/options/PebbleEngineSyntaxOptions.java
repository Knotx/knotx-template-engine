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
import org.apache.commons.lang3.StringUtils;

/**
 * Describes custom syntax for the Pebble Engine.
 *
 * @see <a href="https://pebbletemplates.io/wiki/guide/basic-usage/">Pebble Basic Usage</a>
 * @see <a href="https://pebbletemplates.io/wiki/guide/installation/">Pebble Engine Settings in
 * Installation guide</a>
 */
@DataObject(generateConverter = true, publicConverter = false)
public class PebbleEngineSyntaxOptions {

  private boolean strictVariables = false;
  private boolean newLineTrimming = true;
  private boolean literalDecimalTreatedAsInteger = false;

  private String delimiterCommentOpen = "{#";
  private String delimiterCommentClose = "#}";
  private String delimiterExecuteOpen = "{%";
  private String delimiterExecuteClose = "%}";
  private String delimiterPrintOpen = "{{";
  private String delimiterPrintClose = "}}";
  private String whitespaceTrim = "-";

  private String wrappingRootNodeName = StringUtils.EMPTY;

  public PebbleEngineSyntaxOptions() {
  }

  public PebbleEngineSyntaxOptions(JsonObject json) {
    PebbleEngineSyntaxOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    PebbleEngineSyntaxOptionsConverter.toJson(this, json);
    return json;
  }

  /**
   * @return flag indicating whether strict mode should be enabled
   */
  public boolean isStrictVariables() {
    return strictVariables;
  }

  /**
   * Sets the Strict Mode of the Pebble Engine.<br> In Strict Mode, referencing a null or
   * non-existent variable or attribute will result in an exception. <br> When Strict Mode is
   * disabled, such variables/attributes will be rendered as empty.
   *
   * Strict mode is disabled by default.
   *
   * @param strictVariables flag indicating whether strict mode should be enabled
   */
  public void setStrictVariables(boolean strictVariables) {
    this.strictVariables = strictVariables;
  }

  /**
   * @return flag indicating whether a new line character occurring after a closing Pebble tag
   * should be trimmed
   */
  public boolean isNewLineTrimming() {
    return newLineTrimming;
  }

  /**
   * Sets whether a new line after a closing Pebble tag should be trimmed. Defaults to true.
   *
   * @param newLineTrimming flag indicating whether a new line character occurring after a closing
   * Pebble tag should be trimmed
   */
  public void setNewLineTrimming(boolean newLineTrimming) {
    this.newLineTrimming = newLineTrimming;
  }

  /**
   * @return flag indicating whether a literal decimal should be treated as Integer or Long
   */
  public boolean isLiteralDecimalTreatedAsInteger() {
    return literalDecimalTreatedAsInteger;
  }

  /**
   * Sets whether a literal decimal should be treated as Integer. <br> If false, all literal
   * decimals will be treated as Long. <br> A literal decimal is considered a numeric string without
   * a fraction separator (a dot).<br> Defaults to false.
   *
   * @param literalDecimalTreatedAsInteger flag indicating whether a literal decimal should be
   * treated as Integer or Long
   */
  public void setLiteralDecimalTreatedAsInteger(boolean literalDecimalTreatedAsInteger) {
    this.literalDecimalTreatedAsInteger = literalDecimalTreatedAsInteger;
  }

  /**
   * @return custom delimiter for comment start
   */
  public String getDelimiterCommentOpen() {
    return delimiterCommentOpen;
  }

  /**
   * Defaults to <code>{#</code>
   *
   * @param delimiterCommentOpen custom delimiter for comment start
   */
  public void setDelimiterCommentOpen(String delimiterCommentOpen) {
    this.delimiterCommentOpen = delimiterCommentOpen;
  }

  /**
   * @return custom delimiter for comment end
   */
  public String getDelimiterCommentClose() {
    return delimiterCommentClose;
  }

  /**
   * Defaults to <code>#}</code>
   *
   * @param delimiterCommentClose custom delimiter for comment end
   */
  public void setDelimiterCommentClose(String delimiterCommentClose) {
    this.delimiterCommentClose = delimiterCommentClose;
  }

  /**
   * @return custom delimiter for execution tag start
   */
  public String getDelimiterExecuteOpen() {
    return delimiterExecuteOpen;
  }

  /**
   * Defaults to <code>{%</code>
   *
   * @param delimiterExecuteOpen custom delimiter for execution tag start
   */
  public void setDelimiterExecuteOpen(String delimiterExecuteOpen) {
    this.delimiterExecuteOpen = delimiterExecuteOpen;
  }

  /**
   * @return custom delimiter for execution tag end
   */
  public String getDelimiterExecuteClose() {
    return delimiterExecuteClose;
  }

  /**
   * Defaults to <code>%}</code>
   *
   * @param delimiterExecuteClose custom delimiter for execution tag end
   */
  public void setDelimiterExecuteClose(String delimiterExecuteClose) {
    this.delimiterExecuteClose = delimiterExecuteClose;
  }

  /**
   * @return custom delimiter for print tag start
   */
  public String getDelimiterPrintOpen() {
    return delimiterPrintOpen;
  }

  /**
   * Defaults to <code>{{</code>
   *
   * @param delimiterPrintOpen custom delimiter for print tag start
   */
  public void setDelimiterPrintOpen(String delimiterPrintOpen) {
    this.delimiterPrintOpen = delimiterPrintOpen;
  }

  /**
   * @return custom delimiter for print tag end
   */
  public String getDelimiterPrintClose() {
    return delimiterPrintClose;
  }

  /**
   * Defaults to <code>}}</code>
   *
   * @param delimiterPrintClose custom delimiter for print tag end
   */
  public void setDelimiterPrintClose(String delimiterPrintClose) {
    this.delimiterPrintClose = delimiterPrintClose;
  }

  /**
   * @return custom character used to enable leading/trailing whitespaces trimming
   */
  public String getWhitespaceTrim() {
    return whitespaceTrim;
  }

  /**
   * Defaults to <code>-</code> Note: this is used for trimming whitespaces adjacent to a Pebble
   * tag, not inside the content rendered by it.
   *
   * @param whitespaceTrim custom character used to enable leading/trailing whitespaces trimming
   */
  public void setWhitespaceTrim(String whitespaceTrim) {
    this.whitespaceTrim = whitespaceTrim;
  }

  public String getWrappingRootNodeName() {
    return wrappingRootNodeName;
  }

  /**
   * Set wrapping root node name. Allows access to variables with dash <code>-</code> symbol. E.g.
   * if root node name is rootNode and field is data-dashed, use <code>{{ rootNode['data-dashed']
   * }}</code> Defaults to empty string = no wrapping is performed.
   *
   * @param wrappingRootNodeName the name of the wrapping node. If empty string, the context will be
   * left unwrapped.
   */
  public void setWrappingRootNodeName(String wrappingRootNodeName) {
    this.wrappingRootNodeName = wrappingRootNodeName;
  }

  @Override
  public String toString() {
    return "PebbleEngineSyntaxOptions{" +
        "strictVariables=" + strictVariables +
        ", newLineTrimming=" + newLineTrimming +
        ", literalDecimalTreatedAsInteger=" + literalDecimalTreatedAsInteger +
        ", delimiterCommentOpen='" + delimiterCommentOpen + '\'' +
        ", delimiterCommentClose='" + delimiterCommentClose + '\'' +
        ", delimiterExecuteOpen='" + delimiterExecuteOpen + '\'' +
        ", delimiterExecuteClose='" + delimiterExecuteClose + '\'' +
        ", delimiterPrintOpen='" + delimiterPrintOpen + '\'' +
        ", delimiterPrintClose='" + delimiterPrintClose + '\'' +
        ", whitespaceTrim='" + whitespaceTrim + '\'' +
        ", wrappingRootNodeName='" + wrappingRootNodeName + '\'' +
        '}';
  }
}
