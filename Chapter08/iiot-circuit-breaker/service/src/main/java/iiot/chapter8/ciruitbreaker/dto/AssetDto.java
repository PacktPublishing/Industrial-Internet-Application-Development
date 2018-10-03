/**
 * Copyright (c) 2016 General Electric Company. All rights reserved.
 * <p>
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package iiot.chapter8.ciruitbreaker.dto;


/**
 * Asset entity
 */
public class AssetDto {

    public AssetDto() {
    }

    public AssetDto(Long id, String name, String description, String owner, String operator) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.operator = operator;
    }

    private Long id;

    private String name;

    private String description;

    private String owner;

    private String operator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "AssetDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", operator='" + operator + '\'' +
                '}';
    }
}
