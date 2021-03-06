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

public class ShopResponse {
    private String name;
    private List<Asset> assets;
    private List<Analytic> analytics;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public List<Analytic> getAnalytics() {
        return analytics;
    }

    public void setAnalytics(List<Analytic> analytics) {
        this.analytics = analytics;
    }

    @Override
    public String toString() {
        return "ShopResponse{" +
            ", name='" + name + '\'' +
            ", assets=" + assets +
            ", analytics=" + analytics +
            '}';
    }
}
