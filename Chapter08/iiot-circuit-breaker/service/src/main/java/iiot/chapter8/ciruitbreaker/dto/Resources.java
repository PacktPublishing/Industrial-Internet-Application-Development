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

import org.springframework.hateoas.Link;

/**
 * HateOS Resource
 */
public class Resources  extends org.springframework.hateoas.Resources<AssetDto> {


    public Resources() {
    }

    public Resources(Iterable<AssetDto> content, Link... links) {
        super(content, links);
    }
}
