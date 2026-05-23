package com.app.common.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/** Nạp FXML với {@code fx:root} — UI class là root và controller. */
public final class FxmlUiHelper {

  private FxmlUiHelper() {}

  public static void loadSelf(Parent root, String fxmlFileName) {
    try {
      var url = root.getClass().getResource(fxmlFileName);
      if (url == null) {
        throw new IOException("Không tìm thấy FXML: " + fxmlFileName + " (" + root.getClass().getName() + ")");
      }
      FXMLLoader loader = new FXMLLoader(url);
      loader.setRoot(root);
      loader.setController(root);
      loader.load();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static void addStylesheet(Parent node, String cssFileName) {
    var url = node.getClass().getResource(cssFileName);
    if (url != null) {
      node.getStylesheets().add(url.toExternalForm());
    }
  }
}
