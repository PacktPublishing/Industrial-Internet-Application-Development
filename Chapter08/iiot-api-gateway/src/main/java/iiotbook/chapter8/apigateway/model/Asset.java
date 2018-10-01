/********************************************************************************
 * Copyright (c) 2015-2016 GE Digital. All rights reserved.                     *
 *                                                                              *
 * The copyright to the computer software herein is the property of GE Digital. *
 * The software may be used and/or copied only with the written permission of   *
 * GE Digital or in accordance with the terms and conditions stipulated in the  *
 * agreement/contract under which the software has been supplied.               *
 ********************************************************************************/

package iiotbook.chapter8.apigateway.model;

public class Asset {

    private String name;
    private String description;
    private String owner;
    private String operator;

    public Asset() {
    }

    public Asset(String name, String description, String owner, String operator) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.operator = operator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "Asset{" +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", owner='" + owner + '\'' +
            ", operator='" + operator + '\'' +
            '}';
    }
}
