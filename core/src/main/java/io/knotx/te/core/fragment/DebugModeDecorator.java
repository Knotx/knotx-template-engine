package io.knotx.te.core.fragment;

import io.knotx.dataobjects.Fragment;
import io.knotx.dataobjects.KnotContext;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.apache.commons.io.IOUtils;

public class DebugModeDecorator {

  private static final Logger LOGGER = LoggerFactory.getLogger(DebugModeDecorator.class);
  private static final String HEAD_SECTION_END = "</head>";

  private String globalStyle;
  private String fragmentSectionTemplate;
  private String fragmentJsTemplate;

  public void init() {
    ClassLoader classLoader = getClass().getClassLoader();
    try (
        InputStream styleIS = classLoader.getResourceAsStream("debug-style.html");
        InputStream fragmentIS = classLoader.getResourceAsStream("debug-section.html");
        InputStream jsIS = classLoader.getResourceAsStream("debug-js.html")
    ) {
      globalStyle = IOUtils.toString(styleIS, StandardCharsets.UTF_8);
      fragmentSectionTemplate = IOUtils.toString(fragmentIS, StandardCharsets.UTF_8);
      fragmentJsTemplate = IOUtils.toString(jsIS, StandardCharsets.UTF_8);
    } catch (IOException e) {
      LOGGER.error("Failed to load file!", e);
    }
  }

  public void addGlobalStyle(KnotContext knotContext) {
    knotContext.getFragments().stream()
        .filter(fragment -> fragment.content().contains(HEAD_SECTION_END))
        .findFirst()
        .ifPresent(fragment -> fragment.content(
            fragment.content().replace(HEAD_SECTION_END, globalStyle + HEAD_SECTION_END)));
  }

  public void applyDebugMode(FragmentContext fragmentContext) {
    final String fragmentUID = UUID.randomUUID().toString();
    final Fragment fragment = fragmentContext.fragment();
    fragment.content("<!-- DEBUG STARTED -->"
        + wrap(fragmentUID, fragmentContext)
        + prepareJS(fragmentUID)
        + "<!-- DEBUG ENDED -->");
  }

  private String wrap(String fragmentUID, FragmentContext fragmentContext) {
    final Fragment fragment = fragmentContext.fragment();
    return fragmentSectionTemplate
        .replaceAll("%%DEBUG-UID%%", fragmentUID)
        .replace("%%DEBUG-FINAL-MARKUP%%", fragment.content())
        .replace("%%DEBUG-RAW-SNIPPET%%", fragmentContext.getOriginalSnippet())
        .replace("%%DEBUG-RAW-DATA%%", fragment.context().encodePrettily());
  }


  private String prepareJS(String fragmentUID) {
    return fragmentJsTemplate.replaceAll("%%DEBUG-UID%%", fragmentUID);
  }

}
