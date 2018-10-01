/**
 * Copyright (c) 2016 General Electric Company. All rights reserved.
 * <p>
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package iiot.chapter8.ciruitbreaker;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.util.ReflectionUtils;

/**
 * Created by 212433176 on 6/17/16.
 */
public class Main {
    public static void main(String[] args) {
        JUnitCore.main(ApplicationTest.class.getName());
    }
}
