/*
RightCurly
option = (default)same
tokens = (default)LITERAL_TRY, LITERAL_CATCH, LITERAL_FINALLY, LITERAL_IF, LITERAL_ELSE


*/

package com.puppycrawl.tools.checkstyle.checks.blocks.rightcurly;

class InputRightCurlyLeftTestDefault
{
    /** @see test method **/
    int foo() throws InterruptedException
    {
        int x = 1;
        int a = 2;
        while (true)
        {
            try
            {
                if (x > 0)
                {
                    break;
                } // violation ''}' at column 17 should be on the same line as .*/else'
                else if (x < 0) {
                    ;
                } // violation ''}' at column 17 should be on the same line as .*/else'
                else
                {
                    break;
                }
                switch (a)
                {
                case 0:
                    break;
                default:
                    break;
                }
            } // violation ''}' at column 13 should be on the same line as .*/catch'
            catch (Exception e)
            {
                break;
            } // violation ''}' at column 13 should be on the same line as .*/finally'
            finally
            {
                break;
            }
        }

        synchronized (this)
        {
            do
            {
                x = 2;
            } while (x == 2);
        }

        this.wait(666
                 ); // Bizarre, but legal

        for (int k = 0; k < 1; k++)
        {
            String innerBlockVariable = "";
        }

        // test input for bug reported by Joe Comuzzi
        if (System.currentTimeMillis() > 1000)
            return 1;
        else
            return 2;
    }

    // Test static initialiser
    static
    {
        int x = 1; // should not require any javadoc
    }



    public enum GreetingsEnum
    {
        HELLO,
        GOODBYE
    };

    void method2()
    {
        boolean flag = true;
        if (flag) {
            System.identityHashCode("heh");
            flag = !flag; } String. // violation ''}' at column 27 should have line break before'
                CASE_INSENSITIVE_ORDER.equals("Xe-xe");
        // it is ok to have rcurly on the same line as previous
        // statement if lcurly on the same line.
        if (flag) { String.CASE_INSENSITIVE_ORDER.equals("it is ok."); }
    }
}

/**
 * Test input for closing brace if that brace terminates
 * a statement or the body of a constructor.
 */
class FooCtor
{
        int i;
        public FooCtor()
    {
                i = 1;
    }}

/**
* Test input for closing brace if that brace terminates
* a statement or the body of a method.
*/
class FooMethod
{
        public void fooMethod()
    {
                int i = 1;
    }}

/**
* Test input for closing brace if that brace terminates
* a statement or the body of a named class.
*/
class FooInner
{
        class InnerFoo
    {
                public void fooInnerMethod ()
        {

                }
    }}

/**
 * False positive
 *
 */
class Absent_CustomFieldSerializer3 {

    public static void serialize() {}
}

class Absent_CustomFieldSerializer4
{
    public Absent_CustomFieldSerializer4() {}
}

class EmptyClass2 {}

interface EmptyInterface3 {}

class ClassWithStaticInitializers
{
    static {
    }
    static
    {}

    static class Inner
    {
        static {
            int i = 1;
        }
    }

    public void emptyBlocks() {
        try {
            // comment
        } catch (RuntimeException e) {
            new Object();
        } catch (Exception e) {
            // comment
        } catch (Throwable e) {
        } finally {
            // comment
        }

        do {
        } while (true);
    }

    public void codeAfterLastRightCurly() {
        while (new Object().equals(new Object())) {
        };
        for (int i = 0; i < 1; i++) { new Object(); };
    }

    static final java.util.concurrent.ThreadFactory threadFactory
            = new java.util.concurrent.ThreadFactory() {
        @Override
        public Thread newThread(final Runnable r) {
            return new Thread(r);
        }};

    interface Interface1
    {
        int i = 1;
        public void meth1(); }

    interface Interface2
    { int i = 1; public void meth1(); }

    interface Interface3 {
        void display();
        interface Interface4 {
            void myMethod();
        }}
}
