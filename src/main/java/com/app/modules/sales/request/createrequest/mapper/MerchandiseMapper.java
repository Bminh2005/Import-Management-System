package com.app.modules.sales.request.createrequest.mapper;

import com.app.common.entity.Merchandise;
import com.app.common.util.FormatUtil;
import com.app.modules.sales.request.createrequest.dto.MerchandiseDTO;
import com.app.modules.sales.request.createrequest.ui.model.CreateImportItemModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MerchandiseMapper {
    public static ObservableList<CreateImportItemModel> mapToCreateImportItemModel(List<MerchandiseDTO> merchandiseList) {
        List<CreateImportItemModel> models = merchandiseList.stream()
                .map(e-> new CreateImportItemModel(
                        Long.toString(e.getId()), e.getMerchandise_name(), 1, e.getUnit(), e.getReference_price(), null
                )).collect(Collectors.toList());
        return FXCollections.observableArrayList(models);
    }
}
