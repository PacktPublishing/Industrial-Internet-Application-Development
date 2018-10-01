/********************************************************************************
 * Copyright (c) 2015-2016 GE Digital. All rights reserved.                     *
 *                                                                              *
 * The copyright to the computer software herein is the property of GE Digital. *
 * The software may be used and/or copied only with the written permission of   *
 * GE Digital or in accordance with the terms and conditions stipulated in the  *
 * agreement/contract under which the software has been supplied.               *
 ********************************************************************************/
package iiotbook.chapter8.apigateway.model;

/**
 * Shop models
 */

public class ShopModel {

    private Long id;
    private Shop shop;
    private AssetModel assetModel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public AssetModel getAssetModel() {
        return assetModel;
    }

    public void setAssetModel(AssetModel assetModel) {
        this.assetModel = assetModel;
    }
}
