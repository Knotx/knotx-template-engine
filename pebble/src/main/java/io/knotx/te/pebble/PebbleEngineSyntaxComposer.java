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

import io.pebbletemplates.pebble.lexer.Syntax;
import io.knotx.te.pebble.options.PebbleEngineSyntaxOptions;

final class PebbleEngineSyntaxComposer {

  private PebbleEngineSyntaxComposer() {
    // utility class
  }

  static Syntax compose(PebbleEngineSyntaxOptions options) {
    return new Syntax.Builder()
        .setCommentOpenDelimiter(options.getDelimiterCommentOpen())
        .setCommentCloseDelimiter(options.getDelimiterCommentClose())
        .setExecuteOpenDelimiter(options.getDelimiterExecuteOpen())
        .setExecuteCloseDelimiter(options.getDelimiterExecuteClose())
        .setPrintOpenDelimiter(options.getDelimiterPrintOpen())
        .setPrintCloseDelimiter(options.getDelimiterPrintClose())
        .setWhitespaceTrim(options.getWhitespaceTrim())
        .build();
  }

}
