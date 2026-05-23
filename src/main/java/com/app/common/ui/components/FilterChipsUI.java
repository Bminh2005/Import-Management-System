package com.app.common.ui.components;

import com.app.common.util.FxmlUiHelper;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/** Nhóm chip lọc tái sử dụng. */
public class FilterChipsUI extends HBox {

  public record ChipOption(String value, String label, String count, boolean selected) {
    public String displayText() {
      return label + " (" + count + ")";
    }
  }

  private Consumer<String> onFilterSelected;
  private String selectedValue = "all";

  public FilterChipsUI() {
    FxmlUiHelper.loadSelf(this, "FilterChips.fxml");
  }

  public void bindChips(List<ChipOption> chips) {
    getChildren().clear();
    for (ChipOption chip : chips) {
      Button btn = new Button(chip.displayText());
      applyChipStyle(btn, chip.selected());
      if (chip.selected()) {
        selectedValue = chip.value();
      }
      btn.setOnAction(
          e -> {
            selectedValue = chip.value();
            for (var node : getChildren()) {
              if (node instanceof Button b) {
                applyChipStyle(b, b == btn);
              }
            }
            if (onFilterSelected != null) {
              onFilterSelected.accept(selectedValue);
            }
          });
      getChildren().add(btn);
    }
  }

  public String getSelectedValue() {
    return selectedValue;
  }

  public void setOnFilterSelected(Consumer<String> onFilterSelected) {
    this.onFilterSelected = onFilterSelected;
  }

  private void applyChipStyle(Button btn, boolean selected) {
    btn.getStyleClass().removeAll("btn-chip", "btn-chip-selected");
    btn.getStyleClass().add(selected ? "btn-chip-selected" : "btn-chip");
  }
}
