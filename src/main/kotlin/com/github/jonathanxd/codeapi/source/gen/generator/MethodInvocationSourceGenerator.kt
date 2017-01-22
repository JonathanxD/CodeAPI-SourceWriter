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
package com.github.jonathanxd.codeapi.source.gen.generator

import com.github.jonathanxd.codeapi.CodeAPI
import com.github.jonathanxd.codeapi.base.BodyHolder
import com.github.jonathanxd.codeapi.base.MethodInvocation
import com.github.jonathanxd.codeapi.base.MethodSpecification
import com.github.jonathanxd.codeapi.base.ParametersHolder
import com.github.jonathanxd.codeapi.common.Data
import com.github.jonathanxd.codeapi.common.InvokeDynamic.LambdaFragment
import com.github.jonathanxd.codeapi.common.InvokeDynamic.LambdaMethodReference
import com.github.jonathanxd.codeapi.common.MethodType
import com.github.jonathanxd.codeapi.gen.value.CodeSourceData
import com.github.jonathanxd.codeapi.gen.value.Parent
import com.github.jonathanxd.codeapi.gen.value.Value
import com.github.jonathanxd.codeapi.gen.value.ValueGenerator
import com.github.jonathanxd.codeapi.keyword.Keywords
import com.github.jonathanxd.codeapi.source.gen.PlainSourceGenerator
import com.github.jonathanxd.codeapi.source.gen.value.PlainValue
import com.github.jonathanxd.codeapi.source.gen.value.TargetValue
import java.util.*

object MethodInvocationSourceGenerator : ValueGenerator<MethodInvocation, String, PlainSourceGenerator> {

    override fun gen(inp: MethodInvocation, c: PlainSourceGenerator, parents: Parent<ValueGenerator<*, String, PlainSourceGenerator>>, codeSourceData: CodeSourceData, data: Data): List<Value<*, String, PlainSourceGenerator>> {

        val values = ArrayList<Value<*, String, PlainSourceGenerator>>()

        val invokeDynamic = inp.invokeDynamic

        val spec = inp.spec

        val METHOD_SEPARATOR: String

        if (invokeDynamic != null) {

            if (invokeDynamic is LambdaFragment) {

                val methodFragment = invokeDynamic.methodFragment

                val method = methodFragment.declaration

                val body = method.body

                values.add(TargetValue.create(ParametersHolder::class.java, method, parents))

                if (body.isNotEmpty) {
                    values.add(PlainValue.create("->"))

                    values.add(TargetValue.create(BodyHolder::class.java, method, parents))
                } else {
                    values.add(PlainValue.create("-> {};"))
                }

                return values

            } else if (invokeDynamic is LambdaMethodReference) {
                if (inp.arguments.isEmpty()) {
                    METHOD_SEPARATOR = "::"
                } else {
                    values.add(PlainValue.create("() ->"))
                    METHOD_SEPARATOR = "."
                }
            } else {
                return listOf(PlainValue.create("// Dynamic::[" + inp.toString() + "];"))
            }
        } else {
            METHOD_SEPARATOR = "."
        }

        // Is method reference
        val isRef = METHOD_SEPARATOR == "::"
        val isCtr = spec.methodName == "<init>"
        val isSuper = spec.methodType == MethodType.SUPER_CONSTRUCTOR

        var mi = inp

        if (isSuper) {
            val localization = mi.localization
            mi = mi.builder().withTarget(CodeAPI.accessLocal()).build()

            val type = Util.localizationResolve(localization, parents)

            if (localization.`is`(type)) {
                values.add(PlainValue.create("this"))
            } else {
                values.add(PlainValue.create("super"))
            }
        }

        if (isCtr && !isRef && !isSuper) {
            values.add(TargetValue.create(Keywords.NEW, parents))
            mi = mi.builder().withTarget(CodeAPI.accessLocal()).build()
        }

        if (!isSuper) {
            values.addAll(AccessorSourceGenerator.gen(mi, !isRef && !isCtr, parents))
        }

        if (isRef) {
            values.add(PlainValue.create(METHOD_SEPARATOR))
        }

        if (isCtr && isRef && !isSuper) {
            values.add(TargetValue.create(Keywords.NEW, parents))
        }

        values.add(TargetValue.create(MethodSpecification::class.java, spec, parents))


        if (Util.isBody(parents)) {
            values.add(PlainValue.create(";"))
        }

        return values
    }

}
