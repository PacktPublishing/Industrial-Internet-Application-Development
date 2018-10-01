/********************************************************************************
 * Copyright (c) 2015-2016 GE Digital. All rights reserved.                     *
 *                                                                              *
 * The copyright to the computer software herein is the property of GE Digital. *
 * The software may be used and/or copied only with the written permission of   *
 * GE Digital or in accordance with the terms and conditions stipulated in the  *
 * agreement/contract under which the software has been supplied.               *
 ********************************************************************************/

package iiotbook.chapter8.apigateway.model;

import org.springframework.hateoas.Link;

/**
 * HateOS Resource
 */
public class AnalyticResources extends org.springframework.hateoas.Resources<Analytic> {

    public AnalyticResources() {
    }

    public AnalyticResources(Iterable<Analytic> content, Link... links) {
        super(content, links);
    }
}
