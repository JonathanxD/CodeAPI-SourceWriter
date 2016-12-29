/*
 *      CodeAPI-SourceWriter - Framework to generate Java code and Bytecode code. <https://github.com/JonathanxD/CodeAPI-SourceWriter>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2016 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
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

import com.github.jonathanxd.codeapi.CodePart;
import com.github.jonathanxd.codeapi.MutableCodeSource;
import com.github.jonathanxd.codeapi.common.CodeArgument;
import com.github.jonathanxd.codeapi.common.CodeModifier;
import com.github.jonathanxd.codeapi.common.CodeParameter;
import com.github.jonathanxd.codeapi.common.InvokeType;
import com.github.jonathanxd.codeapi.helper.Helper;
import com.github.jonathanxd.codeapi.impl.CodeClass;
import com.github.jonathanxd.codeapi.impl.CodeField;
import com.github.jonathanxd.codeapi.impl.CodeMethod;
import com.github.jonathanxd.codeapi.impl.MethodSpecImpl;
import com.github.jonathanxd.codeapi.keywords.Keywords;
import com.github.jonathanxd.codeapi.literals.Literals;
import com.github.jonathanxd.codeapi.operators.Operators;
import com.github.jonathanxd.codeapi.source.gen.PlainSourceGenerator;
import com.github.jonathanxd.codeapi.types.CodeType;

import org.junit.Test;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;

import static com.github.jonathanxd.codeapi.helper.Helper.accessLocalVariable;
import static com.github.jonathanxd.codeapi.helper.Helper.createFor;
import static com.github.jonathanxd.codeapi.helper.Helper.getJavaType;
import static com.github.jonathanxd.codeapi.helper.Helper.sourceOf;

public class TestForLoop {
    private static final TestForLoop TEST_FOR_LOOP = new TestForLoop();

    @Test
    public void simpleTest() {

        MutableCodeSource source = new MutableCodeSource();

        PlainSourceGenerator generator = new PlainSourceGenerator();

        CodeClass codeClass = new CodeClass(null, "me.jonathanscripter.codeapi.test.Nomade",
                Collections.singletonList(CodeModifier.PUBLIC),
                null,
                null,
                sourceOf(
                        createMethod()
                ));

        source.add(codeClass);
        String gen = generator.gen(source);

        System.out.println(gen);
    }

    private CodePart createMethod() {

        MutableCodeSource methodSource = new MutableCodeSource();

        CodeMethod method = new CodeMethod("printX", Collections.emptyList(),
                Arrays.asList(new CodeParameter("y", getJavaType(Integer.TYPE)), new CodeParameter("obj", getJavaType(Object.class))),
                getJavaType(Void.TYPE),
                methodSource);

        CodeField xField = new CodeField("x", Helper.getJavaType(Integer.TYPE), Literals.INT(0), Collections.emptyList());

        CodePart invokePrintln = Helper.invoke(InvokeType.INVOKE_STATIC, (CodeType) null, Helper.accessVariable(Helper.getJavaType(System.class), "out", Helper.getJavaType(OutputStream.class)), new MethodSpecImpl("println", Collections.singletonList(new CodeArgument(accessLocalVariable("obj", Helper.getJavaType(Object.class)), Helper.getJavaType(Object.class)))));

        CodePart addToX = Helper.operateLocalVariable(xField, Operators.INCREMENT);

        methodSource.add(
                createFor(xField,
                        Helper.createIfVal().add1(
                                Helper.check(accessLocalVariable(xField.getName(), Helper.getJavaType(Integer.TYPE)), Operators.LESS_THAN, accessLocalVariable("y", Helper.getJavaType(Integer.TYPE)))
                        ).make(), addToX,
                        sourceOf(invokePrintln,
                                Keywords.BREAK))
        );

        return method;
    }

}
