/*
 *      CodeAPI-SourceWriter - Framework to generate Java code and Bytecode code. <https://github.com/JonathanxD/CodeAPI-SourceWriter>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2017 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
 *      Copyright (c) contributors
 *
 *
 *      Permission is hereby granted, free of charge, to any person obtaining a copy
 *      of this software and associated documentation files (the "Software"), to deal
 *      in the Software without restriction, including without limitation the rights
 *      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *      copies of the Software, and to permit persons to whom the Software is
 *      furnished to do so, subject to the following conditions:
 *
 *      The above copyright notice and this permission notice shall be included in
 *      all copies or substantial portions of the Software.
 *
 *      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *      THE SOFTWARE.
 */
package com.github.jonathanxd.codeapi.source.test;

import com.github.jonathanxd.codeapi.CodeSource;
import com.github.jonathanxd.codeapi.base.TypeDeclaration;
import com.github.jonathanxd.codeapi.test.EnumTest_;
import com.github.jonathanxd.iutils.annotation.Named;
import com.github.jonathanxd.iutils.object.Pair;

import org.junit.Test;

public class EnumTest {

    @Test
    public void test() {
        Pair<@Named("Main class") TypeDeclaration, @Named("Source") CodeSource> $ = EnumTest_.$();

        SourceTest test = CommonSourceTest.test(this.getClass(), $._1(), $._2());
        test.expect("import com.github.jonathanxd.codeapi.test.EnumTest_.MyItf;\n" +
                "\n" +
                "public enum MyEnum implements MyItf {\n" +
                "    \n" +
                "    A {\n" +
                "        @Override()\n" +
                "        public void v() {\n" +
                "            System.out.println(\"A\");\n" +
                "        }\n" +
                "        \n" +
                "    }, \n" +
                "    B {\n" +
                "        @Override()\n" +
                "        public void v() {\n" +
                "            System.out.println(\"B\");\n" +
                "        }\n" +
                "        \n" +
                "    };\n" +
                "    \n" +
                "}\n");
        test.consume(System.out::println);
    }

}
