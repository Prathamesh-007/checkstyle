////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2021 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////

package com.puppycrawl.tools.checkstyle.checks.whitespace;

import static com.google.common.truth.Truth.assertWithMessage;
import static com.puppycrawl.tools.checkstyle.checks.whitespace.EmptyForInitializerPadCheck.MSG_NOT_PRECEDED;
import static com.puppycrawl.tools.checkstyle.checks.whitespace.EmptyForInitializerPadCheck.MSG_PRECEDED;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.AbstractModuleTestSupport;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

public class EmptyForInitializerPadCheckTest
    extends AbstractModuleTestSupport {

    @Override
    protected String getPackageLocation() {
        return "com/puppycrawl/tools/checkstyle/checks/whitespace/emptyforinitializerpad";
    }

    @Test
    public void testGetRequiredTokens() {
        final EmptyForInitializerPadCheck checkObj = new EmptyForInitializerPadCheck();
        final int[] expected = {TokenTypes.FOR_INIT};
        assertArrayEquals(expected, checkObj.getRequiredTokens(),
                "Default required tokens are invalid");
    }

    @Test
    public void testDefault() throws Exception {
        final String[] expected = {
            "51:15: " + getCheckMessage(MSG_PRECEDED, ";"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyForInitializerPadDefaultConfig.java"), expected);
    }

    @Test
    public void testSpaceOption() throws Exception {
        final String[] expected = {
            "54:14: " + getCheckMessage(MSG_NOT_PRECEDED, ";"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyForInitializerPad.java"), expected);
    }

    @Test
    public void testGetAcceptableTokens() {
        final EmptyForInitializerPadCheck emptyForInitializerPadCheckObj =
            new EmptyForInitializerPadCheck();
        final int[] actual = emptyForInitializerPadCheckObj.getAcceptableTokens();
        final int[] expected = {
            TokenTypes.FOR_INIT,
        };
        assertArrayEquals(expected, actual, "Default acceptable tokens are invalid");
    }

    /* Additional test for jacoco, since valueOf()
     * is generated by javac and jacoco reports that
     * valueOf() is uncovered.
     */
    @Test
    public void testPadOptionValueOf() {
        final PadOption option = PadOption.valueOf("NOSPACE");
        assertWithMessage("Result of valueOf is invalid")
            .that(option)
            .isEqualTo(PadOption.NOSPACE);
    }

    /* Additional test for jacoco, since valueOf()
     * is generated by javac and jacoco reports that
     * valueOf() is uncovered.
     */
    @Test
    public void testWrapOptionValueOf() {
        final WrapOption option = WrapOption.valueOf("EOL");
        assertWithMessage("Result of valueOf is invalid")
            .that(option)
            .isEqualTo(WrapOption.EOL);
    }

    @Test
    public void testInvalidOption() throws Exception {
        final DefaultConfiguration checkConfig =
                createModuleConfig(EmptyForInitializerPadCheck.class);
        checkConfig.addProperty("option", "invalid_option");

        try {
            final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;

            verify(createChecker(checkConfig),
                    getPath("InputEmptyForInitializerPad2.java"), expected);
            assertWithMessage("exception expected").fail();
        }
        catch (CheckstyleException ex) {
            assertWithMessage("Invalid exception message")
                .that(ex.getMessage())
                .isEqualTo("cannot initialize module com.puppycrawl.tools.checkstyle.TreeWalker - "
                    + "cannot initialize module com.puppycrawl.tools.checkstyle.checks."
                    + "whitespace.EmptyForInitializerPadCheck - "
                    + "Cannot set property 'option' to 'invalid_option'");
        }
    }

}
