/*
 *      Kores-SourceWriter - Translates Kores Structure to Java Source <https://github.com/JonathanxD/Kores-SourceWriter>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2018 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/) <jonathan.scripter@programmer.net>
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
package com.github.jonathanxd.kores.source.process.processors

import com.github.jonathanxd.iutils.data.TypedData
import com.github.jonathanxd.kores.Types
import com.github.jonathanxd.kores.literal.Literal
import com.github.jonathanxd.kores.processor.ProcessorManager
import com.github.jonathanxd.kores.source.process.AppendingProcessor
import com.github.jonathanxd.kores.type.KoresType
import com.github.jonathanxd.kores.type.koresType
import java.lang.reflect.Type

object LiteralProcessor : AppendingProcessor<Literal> {

    override fun process(
        part: Literal,
        data: TypedData,
        processorManager: ProcessorManager<*>,
        appender: com.github.jonathanxd.kores.source.process.JavaSourceAppender
    ) {
        when {
            part.type.`is`(Types.LONG) -> {
                appender += "${part.value}L"
            }
            part.type.`is`(Types.DOUBLE) -> {
                appender += "${part.value}D"
            }
            part.type.`is`(Types.FLOAT) -> {
                appender += "${part.value}F"
            }
            part.type.`is`(KoresType::class.koresType) -> {
                val type = part.value as Type
                appender.appendImport(type)
                com.github.jonathanxd.kores.source.process.KoresTypeHelper.appendName(type, appender)
                appender += ".class"
            }
            part.type.`is`(String::class.koresType) -> {
                val valueString = part.value.toString()
                val valueWithoutQuotes = valueString.substring(1, valueString.length - 1)
                val escapedValue = valueWithoutQuotes
                        .replace("\\", "\\\\")
                        .replace("\"","\\\"")
                appender += "\"$escapedValue\""
            }
            else -> {
                appender += part.value.toString()
            }
        }

        /*if (Util.isBody(parents)) {
            Util.close(values)
        }*/
    }

}
