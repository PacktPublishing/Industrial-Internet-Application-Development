/********************************************************************************
 * Copyright (c) 2015-2016 GE Digital. All rights reserved.                     *
 *                                                                              *
 * The copyright to the computer software herein is the property of GE Digital. *
 * The software may be used and/or copied only with the written permission of   *
 * GE Digital or in accordance with the terms and conditions stipulated in the  *
 * agreement/contract under which the software has been supplied.               *
 ********************************************************************************/

package iiotbook.chapter8.apigateway.utils;

import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final long LOWER_RANGE = 1;
    private static final long UPPER_RANGE = 1000000;
    private static Random random = new Random();

    public static long getNextLong() {
        long randomValue = LOWER_RANGE +
            (long) (random.nextDouble() * (UPPER_RANGE - LOWER_RANGE));
        return randomValue;
    }

    public static String object2JsonWithPrettyFormat(Object object) throws Exception {
        return objectMapper.writerWithDefaultPrettyPrinter()
            .writeValueAsString(object);
    }
}
