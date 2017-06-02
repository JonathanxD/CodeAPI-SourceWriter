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
package com.github.jonathanxd.codeapi.source.process.processors

import com.github.jonathanxd.codeapi.base.BodyHolder
import com.github.jonathanxd.codeapi.base.IfExpressionHolder
import com.github.jonathanxd.codeapi.base.IfStatement
import com.github.jonathanxd.codeapi.processor.CodeProcessor
import com.github.jonathanxd.codeapi.processor.processAs
import com.github.jonathanxd.codeapi.source.process.*
import com.github.jonathanxd.iutils.data.TypedData

object IfStatementProcessor : AppendingProcessor<IfStatement> {

    override fun process(part: IfStatement, data: TypedData, codeProcessor: CodeProcessor<*>, appender: JavaSourceAppender) {
        val isElvis = ELVIS.getOrNull(data) != null

        if (!isElvis) {
            appender += "if"
            appender += " "
        }

        codeProcessor.processAs<IfExpressionHolder>(part, data)

        val elseStatement = part.elseStatement

        if (elseStatement.isEmpty) {
            if (!isElvis) {
                // Clean body
                appender += " "
                VARIABLE_INDEXER.requireIndexer(data).tempFrame {
                    codeProcessor.processAs<BodyHolder>(part, data)
                }
                appender += "\n"
            } else {
                val body = part.body

                if (body.size != 1)
                    throw IllegalStateException("Elvis if expression must have only one element in the body!");
                VARIABLE_INDEXER.requireIndexer(data).tempFrame {
                    codeProcessor.processAs(body.single(), data)
                }
            }
        } else {

            if (!isElvis) {
                appender += " "
                VARIABLE_INDEXER.requireIndexer(data).tempFrame {
                    codeProcessor.processAs<BodyHolder>(part, data)
                }
                appender += " else "
                VARIABLE_INDEXER.requireIndexer(data).tempFrame {
                    codeProcessor.processAs(elseStatement, data)
                }
                appender += "\n"
            } else {
                val body = part.body

                if (body.size != 1)
                    throw IllegalStateException("Elvis if expression must have only one element in the body!")

                if (elseStatement.size != 1)
                    throw IllegalStateException("Elvis else expression must have only one element in the body!")

                appender += " ? "

                VARIABLE_INDEXER.requireIndexer(data).tempFrame {
                    codeProcessor.processAs(body.single(), data)
                }

                appender += " : "

                VARIABLE_INDEXER.requireIndexer(data).tempFrame {
                    codeProcessor.processAs(elseStatement.single(), data)
                }

            }

        }
    }
}
