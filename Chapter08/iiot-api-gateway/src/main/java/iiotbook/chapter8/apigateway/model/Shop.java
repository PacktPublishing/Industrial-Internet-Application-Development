/********************************************************************************
 * Copyright (c) 2015-2016 GE Digital. All rights reserved.                     *
 *                                                                              *
 * The copyright to the computer software herein is the property of GE Digital. *
 * The software may be used and/or copied only with the written permission of   *
 * GE Digital or in accordance with the terms and conditions stipulated in the  *
 * agreement/contract under which the software has been supplied.               *
 ********************************************************************************/
package iiotbook.chapter8.apigateway.model;

import java.util.List;

/**
 * Shop entity
 */
public class Shop {
    private String name;
    private List<ShopModel> supportedModels;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ShopModel> getSupportedModels() {
        return supportedModels;
    }

    public void setSupportedModels(List<ShopModel> supportedModels) {
        this.supportedModels = supportedModels;
    }

    @Override
    public String toString() {
        return "Shop{" +
            ", name='" + name + '\'' +
            ", supportedModels=" + supportedModels +
            '}';
    }
}
