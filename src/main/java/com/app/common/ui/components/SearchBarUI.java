package com.app.common.ui.components;

import com.app.common.util.FxmlUiHelper;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/** Thanh tìm kiếm tái sử dụng. */
public class SearchBarUI extends HBox {

  @FXML private TextField searchField;

  private Consumer<String> onSearchChanged;

  public SearchBarUI() {
    FxmlUiHelper.loadSelf(this, "SearchBar.fxml");
    searchField
        .textProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (onSearchChanged != null) {
                onSearchChanged.accept(newVal == null ? "" : newVal);
              }
            });
  }

  public void setPromptText(String prompt) {
    searchField.setPromptText(prompt);
  }

  public String getSearchQuery() {
    return searchField.getText() == null ? "" : searchField.getText();
  }

  public void setSearchQuery(String query) {
    searchField.setText(query);
  }

  public void setOnSearchChanged(Consumer<String> onSearchChanged) {
    this.onSearchChanged = onSearchChanged;
  }
}
