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

package com.puppycrawl.tools.checkstyle.checks.javadoc;

import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.DetailAstImpl;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class JavadocTagInfoTest {

    /* Additional test for jacoco, since valueOf()
     * is generated by javac and jacoco reports that
     * valueOf() is uncovered.
     */
    @Test
    public void testJavadocTagInfoValueOf() {
        final JavadocTagInfo tag = JavadocTagInfo.valueOf("AUTHOR");
        assertWithMessage("Invalid valueOf result")
            .that(tag)
            .isEqualTo(JavadocTagInfo.AUTHOR);
    }

    /* Additional test for jacoco, since valueOf()
     * is generated by javac and jacoco reports that
     * valueOf() is uncovered.
     */
    @Test
    public void testTypeValueOf() {
        final JavadocTagInfo.Type type = JavadocTagInfo.Type.valueOf("BLOCK");
        assertWithMessage("Invalid valueOf result")
            .that(type)
            .isEqualTo(JavadocTagInfo.Type.BLOCK);
    }

    /* Additional test for jacoco, since values()
     * is generated by javac and jacoco reports that
     * values() is uncovered.
     */
    @Test
    public void testTypeValues() {
        final JavadocTagInfo.Type[] expected = {
            JavadocTagInfo.Type.BLOCK,
            JavadocTagInfo.Type.INLINE,
        };
        final JavadocTagInfo.Type[] actual = JavadocTagInfo.Type.values();
        assertArrayEquals(expected, actual, "Invalid Type values");
    }

    @Test
    public void testAuthor() {
        final DetailAstImpl ast = new DetailAstImpl();

        final int[] validTypes = {
            TokenTypes.PACKAGE_DEF,
            TokenTypes.CLASS_DEF,
            TokenTypes.INTERFACE_DEF,
            TokenTypes.ENUM_DEF,
            TokenTypes.ANNOTATION_DEF,
        };
        for (int type: validTypes) {
            ast.setType(type);
            assertWithMessage("Invalid ast type for current tag: " + ast.getType())
                    .that(JavadocTagInfo.AUTHOR.isValidOn(ast))
                    .isTrue();
        }

        ast.setType(TokenTypes.LAMBDA);
        assertWithMessage("Should return false when ast type is invalid for current tag")
                .that(JavadocTagInfo.AUTHOR.isValidOn(ast))
                .isFalse();
    }

    @Test
    public void testOthers() throws ReflectiveOperationException {
        final JavadocTagInfo[] tags = {
            JavadocTagInfo.CODE,
            JavadocTagInfo.DOC_ROOT,
            JavadocTagInfo.LINK,
            JavadocTagInfo.LINKPLAIN,
            JavadocTagInfo.LITERAL,
            JavadocTagInfo.SEE,
            JavadocTagInfo.SINCE,
            JavadocTagInfo.VALUE,
        };
        for (JavadocTagInfo tagInfo : tags) {
            final DetailAstImpl astParent = new DetailAstImpl();
            astParent.setType(TokenTypes.LITERAL_CATCH);

            final DetailAstImpl ast = new DetailAstImpl();
            final Method setParent = ast.getClass().getDeclaredMethod("setParent",
                    DetailAstImpl.class);
            setParent.setAccessible(true);
            setParent.invoke(ast, astParent);

            final int[] validTypes = {
                TokenTypes.PACKAGE_DEF,
                TokenTypes.CLASS_DEF,
                TokenTypes.INTERFACE_DEF,
                TokenTypes.ENUM_DEF,
                TokenTypes.ANNOTATION_DEF,
                TokenTypes.METHOD_DEF,
                TokenTypes.CTOR_DEF,
                TokenTypes.VARIABLE_DEF,
            };
            for (int type: validTypes) {
                ast.setType(type);
                assertWithMessage("Invalid ast type for current tag: " + ast.getType())
                        .that(tagInfo.isValidOn(ast))
                        .isTrue();
            }

            astParent.setType(TokenTypes.SLIST);
            ast.setType(TokenTypes.VARIABLE_DEF);
            assertWithMessage("Should return false when ast type is invalid for current tag")
                    .that(tagInfo.isValidOn(ast))
                    .isFalse();

            ast.setType(TokenTypes.PARAMETER_DEF);
            assertWithMessage("Should return false when ast type is invalid for current tag")
                    .that(tagInfo.isValidOn(ast))
                    .isFalse();
        }
    }

    @Test
    public void testDeprecated() throws ReflectiveOperationException {
        final DetailAstImpl ast = new DetailAstImpl();
        final DetailAstImpl astParent = new DetailAstImpl();
        astParent.setType(TokenTypes.LITERAL_CATCH);
        final Method setParent = ast.getClass().getDeclaredMethod("setParent", DetailAstImpl.class);
        setParent.setAccessible(true);
        setParent.invoke(ast, astParent);

        final int[] validTypes = {
            TokenTypes.CLASS_DEF,
            TokenTypes.INTERFACE_DEF,
            TokenTypes.ENUM_DEF,
            TokenTypes.ANNOTATION_DEF,
            TokenTypes.METHOD_DEF,
            TokenTypes.CTOR_DEF,
            TokenTypes.ENUM_CONSTANT_DEF,
            TokenTypes.ANNOTATION_FIELD_DEF,
            TokenTypes.VARIABLE_DEF,
        };
        for (int type: validTypes) {
            ast.setType(type);
            assertWithMessage("Invalid ast type for current tag: " + ast.getType())
                    .that(JavadocTagInfo.DEPRECATED.isValidOn(ast))
                    .isTrue();
        }

        astParent.setType(TokenTypes.SLIST);
        ast.setType(TokenTypes.VARIABLE_DEF);
        assertWithMessage("Should return false when ast type is invalid for current tag")
                .that(JavadocTagInfo.DEPRECATED.isValidOn(ast))
                .isFalse();

        ast.setType(TokenTypes.PARAMETER_DEF);
        assertWithMessage("Should return false when ast type is invalid for current tag")
                .that(JavadocTagInfo.DEPRECATED.isValidOn(ast))
                .isFalse();
    }

    @Test
    public void testSerial() throws ReflectiveOperationException {
        final DetailAstImpl ast = new DetailAstImpl();
        final DetailAstImpl astParent = new DetailAstImpl();
        astParent.setType(TokenTypes.LITERAL_CATCH);
        final Method setParent = ast.getClass().getDeclaredMethod("setParent", DetailAstImpl.class);
        setParent.setAccessible(true);
        setParent.invoke(ast, astParent);

        final int[] validTypes = {
            TokenTypes.VARIABLE_DEF,
        };
        for (int type: validTypes) {
            ast.setType(type);
            assertWithMessage("Invalid ast type for current tag: " + ast.getType())
                    .that(JavadocTagInfo.SERIAL.isValidOn(ast))
                    .isTrue();
        }

        astParent.setType(TokenTypes.SLIST);
        ast.setType(TokenTypes.VARIABLE_DEF);
        assertWithMessage("Should return false when ast type is invalid for current tag")
                .that(JavadocTagInfo.SERIAL.isValidOn(ast))
                .isFalse();

        ast.setType(TokenTypes.PARAMETER_DEF);
        assertWithMessage("Should return false when ast type is invalid for current tag")
                .that(JavadocTagInfo.SERIAL.isValidOn(ast))
                .isFalse();
    }

    @Test
    public void testException() {
        final DetailAstImpl ast = new DetailAstImpl();

        final int[] validTypes = {
            TokenTypes.METHOD_DEF,
            TokenTypes.CTOR_DEF,
        };
        for (int type: validTypes) {
            ast.setType(type);
            assertWithMessage("Invalid ast type for current tag: " + ast.getType())
                    .that(JavadocTagInfo.EXCEPTION.isValidOn(ast))
                    .isTrue();
        }

        ast.setType(TokenTypes.LAMBDA);
        assertWithMessage("Should return false when ast type is invalid for current tag")
                .that(JavadocTagInfo.EXCEPTION.isValidOn(ast))
                .isFalse();
    }

    @Test
    public void testThrows() {
        final DetailAstImpl ast = new DetailAstImpl();

        final int[] validTypes = {
            TokenTypes.METHOD_DEF,
            TokenTypes.CTOR_DEF,
        };
        for (int type: validTypes) {
            ast.setType(type);
            assertWithMessage("Invalid ast type for current tag: " + ast.getType())
                    .that(JavadocTagInfo.THROWS.isValidOn(ast))
                    .isTrue();
        }

        ast.setType(TokenTypes.LAMBDA);
        assertWithMessage("Should return false when ast type is invalid for current tag")
                .that(JavadocTagInfo.THROWS.isValidOn(ast))
                .isFalse();
    }

    @Test
    public void testVersions() {
        final DetailAstImpl ast = new DetailAstImpl();

        final int[] validTypes = {
            TokenTypes.PACKAGE_DEF,
            TokenTypes.CLASS_DEF,
            TokenTypes.INTERFACE_DEF,
            TokenTypes.ENUM_DEF,
            TokenTypes.ANNOTATION_DEF,
        };
        for (int type: validTypes) {
            ast.setType(type);
            assertWithMessage("Invalid ast type for current tag: " + ast.getType())
                    .that(JavadocTagInfo.VERSION.isValidOn(ast))
                    .isTrue();
        }

        ast.setType(TokenTypes.LAMBDA);
        assertWithMessage("Should return false when ast type is invalid for current tag")
                .that(JavadocTagInfo.VERSION.isValidOn(ast))
                .isFalse();
    }

    @Test
    public void testParam() {
        final DetailAstImpl ast = new DetailAstImpl();

        final int[] validTypes = {
            TokenTypes.CLASS_DEF,
            TokenTypes.INTERFACE_DEF,
            TokenTypes.METHOD_DEF,
            TokenTypes.CTOR_DEF,
        };
        for (int type: validTypes) {
            ast.setType(type);
            assertWithMessage("Invalid ast type for current tag: " + ast.getType())
                    .that(JavadocTagInfo.PARAM.isValidOn(ast))
                    .isTrue();
        }

        ast.setType(TokenTypes.LAMBDA);
        assertWithMessage("Should return false when ast type is invalid for current tag")
                .that(JavadocTagInfo.PARAM.isValidOn(ast))
                .isFalse();
    }

    @Test
    public void testReturn() {
        final DetailAstImpl ast = new DetailAstImpl();
        final DetailAstImpl astChild = new DetailAstImpl();
        astChild.setType(TokenTypes.TYPE);
        ast.setFirstChild(astChild);
        final DetailAstImpl astChild2 = new DetailAstImpl();
        astChild2.setType(TokenTypes.LITERAL_INT);
        astChild.setFirstChild(astChild2);

        final int[] validTypes = {
            TokenTypes.METHOD_DEF,
        };
        for (int type: validTypes) {
            ast.setType(type);
            assertWithMessage("Invalid ast type for current tag: " + ast.getType())
                    .that(JavadocTagInfo.RETURN.isValidOn(ast))
                    .isTrue();
        }

        astChild2.setType(TokenTypes.LITERAL_VOID);
        assertWithMessage("Should return false when ast type is invalid for current tag")
                .that(JavadocTagInfo.RETURN.isValidOn(ast))
                .isFalse();

        ast.setType(TokenTypes.LAMBDA);
        assertWithMessage("Should return false when ast type is invalid for current tag")
                .that(JavadocTagInfo.RETURN.isValidOn(ast))
                .isFalse();
    }

    @Test
    public void testSerialField() {
        final DetailAstImpl ast = new DetailAstImpl();
        final DetailAstImpl astChild = new DetailAstImpl();
        astChild.setType(TokenTypes.TYPE);
        ast.setFirstChild(astChild);
        final DetailAstImpl astChild2 = new DetailAstImpl();
        astChild2.setType(TokenTypes.ARRAY_DECLARATOR);
        astChild2.setText("ObjectStreamField");
        astChild.setFirstChild(astChild2);

        final int[] validTypes = {
            TokenTypes.VARIABLE_DEF,
        };
        for (int type: validTypes) {
            ast.setType(type);
            assertWithMessage("Invalid ast type for current tag: " + ast.getType())
                    .that(JavadocTagInfo.SERIAL_FIELD.isValidOn(ast))
                    .isTrue();
        }

        astChild2.setText("1111");
        assertWithMessage("Should return false when ast type is invalid for current tag")
                .that(JavadocTagInfo.SERIAL_FIELD.isValidOn(ast))
                .isFalse();

        astChild2.setType(TokenTypes.LITERAL_VOID);
        assertWithMessage("Should return false when ast type is invalid for current tag")
                .that(JavadocTagInfo.SERIAL_FIELD.isValidOn(ast))
                .isFalse();

        ast.setType(TokenTypes.LAMBDA);
        assertWithMessage("Should return false when ast type is invalid for current tag")
                .that(JavadocTagInfo.SERIAL_FIELD.isValidOn(ast))
                .isFalse();
    }

    @Test
    public void testSerialData() {
        final DetailAstImpl ast = new DetailAstImpl();
        ast.setType(TokenTypes.METHOD_DEF);
        final DetailAstImpl astChild = new DetailAstImpl();
        astChild.setType(TokenTypes.IDENT);
        astChild.setText("writeObject");
        ast.setFirstChild(astChild);

        final String[] validNames = {
            "writeObject",
            "readObject",
            "writeExternal",
            "readExternal",
            "writeReplace",
            "readResolve",
        };
        for (String name: validNames) {
            astChild.setText(name);
            assertWithMessage("Invalid ast type for current tag: " + ast.getType())
                    .that(JavadocTagInfo.SERIAL_DATA.isValidOn(ast))
                    .isTrue();
        }

        astChild.setText("1111");
        assertWithMessage("Should return false when ast type is invalid for current tag")
                .that(JavadocTagInfo.SERIAL_DATA.isValidOn(ast))
                .isFalse();

        ast.setType(TokenTypes.LAMBDA);
        assertWithMessage("Should return false when ast type is invalid for current tag")
                .that(JavadocTagInfo.SERIAL_DATA.isValidOn(ast))
                .isFalse();
    }

    @Test
    public void testCoverage() {
        assertWithMessage("Invalid type")
            .that(JavadocTagInfo.VERSION.getType())
            .isEqualTo(JavadocTagInfo.Type.BLOCK);

        assertWithMessage("Invalid toString result")
            .that(JavadocTagInfo.VERSION.toString())
            .isEqualTo("text [@version] name [version] type [BLOCK]");

        try {
            JavadocTagInfo.fromName(null);
            assertWithMessage("IllegalArgumentException is expected").fail();
        }
        catch (IllegalArgumentException ex) {
            assertWithMessage("Invalid exception message")
                .that(ex.getMessage())
                .isEqualTo("the name is null");
        }

        try {
            JavadocTagInfo.fromName("myname");
            assertWithMessage("IllegalArgumentException is expected").fail();
        }
        catch (IllegalArgumentException ex) {
            assertWithMessage("Invalid exception message")
                .that(ex.getMessage())
                .isEqualTo("the name [myname] is not a valid Javadoc tag name");
        }

        try {
            JavadocTagInfo.fromText(null);
            assertWithMessage("IllegalArgumentException is expected").fail();
        }
        catch (IllegalArgumentException ex) {
            assertWithMessage("Invalid exception message")
                .that(ex.getMessage())
                .isEqualTo("the text is null");
        }

        try {
            JavadocTagInfo.fromText("myname");
            assertWithMessage("IllegalArgumentException is expected").fail();
        }
        catch (IllegalArgumentException ex) {
            assertWithMessage("Invalid exception message")
                .that(ex.getMessage())
                .isEqualTo("the text [myname] is not a valid Javadoc tag text");
        }

        assertWithMessage("Invalid fromText result")
            .that(JavadocTagInfo.fromText("@version"))
            .isEqualTo(JavadocTagInfo.VERSION);
    }

}
