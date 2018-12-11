package io.knotx.te.core.fragment;

import io.knotx.dataobjects.Fragment;
import io.knotx.dataobjects.KnotContext;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

public class DebugModeDecorator {

  private static final Logger LOGGER = LoggerFactory.getLogger(DebugModeDecorator.class);
  private static final String BODY_SECTION_END = "</body>";

  private String debugCss;
  private String debugJs;

  public void init() {
    ClassLoader classLoader = getClass().getClassLoader();
    try (
        InputStream debugCssIs = classLoader.getResourceAsStream("debug.css");
        InputStream debugJsIs = classLoader.getResourceAsStream("debug.js")
    ) {
      debugCss = IOUtils.toString(debugCssIs, StandardCharsets.UTF_8);
      debugJs = IOUtils.toString(debugJsIs, StandardCharsets.UTF_8);
    } catch (IOException e) {
      LOGGER.error("Failed to load file!", e);
    }
  }

  public void addDebugAssetsAndData(KnotContext knotContext, String debugData) {
    knotContext.getFragments().stream()
        .filter(fragment -> fragment.content().contains(BODY_SECTION_END))
        .findFirst()
        .ifPresent(fragment -> fragment.content(
            fragment.content().replace(BODY_SECTION_END,
                addAsScript(debugData)
                    + addAsStyle(debugCss)
                    + addAsScript(debugJs)
                    + BODY_SECTION_END)));
  }

  private String addAsScript(String script) {
    return "<script>" + script + "</script>";
  }

  private String addAsStyle(String css) {
    return "<style>" + css + "</style>";
  }

  public Pair<String, Object> wrapFragment(FragmentContext fragmentContext) {
    // FixMe - replace with fragment ID when it will be available
    final String fragmentUID = UUID.randomUUID().toString();
    final Fragment fragment = fragmentContext.fragment();
    fragment.content("<!-- " + fragmentUID + " -->"
        + fragment.content()
        + "<!-- " + fragmentUID + " -->");

    final JsonObject debugData = new JsonObject();
    debugData.put("snippet", fragmentContext.getOriginalSnippet());
    debugData.put("data", fragment.context());

    return Pair.of(fragmentUID, debugData);
  }

}
