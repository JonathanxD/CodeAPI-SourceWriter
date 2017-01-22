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

import com.github.jonathanxd.codeapi.common.Data
import com.github.jonathanxd.codeapi.gen.value.CodeSourceData
import com.github.jonathanxd.codeapi.gen.value.Parent
import com.github.jonathanxd.codeapi.gen.value.Value
import com.github.jonathanxd.codeapi.gen.value.ValueGenerator
import com.github.jonathanxd.codeapi.source.gen.PlainSourceGenerator
import com.github.jonathanxd.codeapi.source.gen.value.PlainValue
import com.github.jonathanxd.codeapi.source.gen.value.TargetValue
import java.util.*

object MethodSourceGenerator : ValueGenerator<MethodDeclaration, String, PlainSourceGenerator> {

    override fun gen(codeMethod: MethodDeclaration, c: PlainSourceGenerator, parents: Parent<ValueGenerator<*, String, PlainSourceGenerator>>, codeSourceData: CodeSourceData, data: Data): List<Value<*, String, PlainSourceGenerator>> {
        val values = ArrayList<Value<*, String, PlainSourceGenerator>>()


        values.add(TargetValue.create(Annotable::class.java, codeMethod, parents))
        values.add(TargetValue.create(Modifierable::class.java, codeMethod, parents))
        values.add(TargetValue.create(Generifiable::class.java, codeMethod, parents))

        if (codeMethod !is ConstructorDeclaration) {
            values.add(TargetValue.create(Returnable::class.java, codeMethod, parents))
        }

        if (codeMethod is ConstructorDeclaration) {

            var name = codeMethod.getName()

            val generatorParent = parents.find(ClassDeclaration::class.java)

            if (generatorParent.isPresent) {
                val generatorParent1 = generatorParent.get()
                val target = generatorParent1.target as ClassDeclaration

                name = target.simpleName
            }

            values.add(PlainValue.create(name))
        } else {
            values.add(TargetValue.create(Named::class.java, codeMethod, parents))
        }

        values.add(TargetValue.create(Parameterizable::class.java, codeMethod, parents))
        values.add(TargetValue.create(Bodied::class.java, codeMethod, parents))

        return values

    }

}
