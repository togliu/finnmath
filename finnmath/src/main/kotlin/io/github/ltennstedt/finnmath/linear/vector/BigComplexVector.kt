/*
 * Copyright 2021 Lars Tennstedt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.ltennstedt.finnmath.linear.vector

import com.google.common.annotations.Beta
import com.google.errorprone.annotations.Immutable
import io.github.ltennstedt.finnmath.extension.sqrt
import io.github.ltennstedt.finnmath.linear.builder.BigComplexVectorJavaBuilder
import io.github.ltennstedt.finnmath.linear.field.BigComplexField
import io.github.ltennstedt.finnmath.number.complex.BigComplex
import org.apiguardian.api.API
import java.math.BigDecimal
import java.math.MathContext

@API(status = API.Status.EXPERIMENTAL, since = "0.0.1")
@Beta
@Immutable
public class BigComplexVector(
    indexToElement: Map<Int, BigComplex>
) : AbstractVector<BigComplex, BigComplex, BigComplexVector, BigDecimal, BigComplex>(
    indexToElement,
    BigComplexField
) {
    /**
     * Returns the sum of this and the [summand] based on the [mathContext]
     *
     * @throws IllegalArgumentException sizes are not equal
     * @since 0.0.1
     */
    public fun add(summand: BigComplexVector, mathContext: MathContext): BigComplexVector {
        require(size == summand.size) { "Equal sizes expected but $size!=${summand.size}" }
        return BigComplexVector(indexToElement.map { (i, e) -> i to e.add(summand[i], mathContext) }.toMap())
    }

    /**
     * Returns the difference of this and the [subtrahend] based on the [mathContext]
     *
     * @throws IllegalArgumentException sizes are not equal
     * @since 0.0.1
     */
    public fun subtract(subtrahend: BigComplexVector, mathContext: MathContext): BigComplexVector {
        require(size == subtrahend.size) { "Equal sizes expected but $size!=${subtrahend.size}" }
        return BigComplexVector(indexToElement.map { (i, e) -> i to e.subtract(subtrahend[i], mathContext) }.toMap())
    }

    /**
     * Returns the dot product of this and the [other one][other] based on the [mathContext]
     *
     * @throws IllegalArgumentException sizes are not equal
     * @since 0.0.1
     */
    public fun dotProduct(other: BigComplexVector, mathContext: MathContext): BigComplex {
        require(size == other.size) { "Equal sizes expected but $size!=${other.size}" }
        return indexToElement.map { (i, e) -> e.multiply(other[i], mathContext) }
            .reduce { a, b -> a.add(b, mathContext) }
    }

    /**
     * Returns the scalar product of this and the [scalar] based on the [mathContext]
     *
     * @since 0.0.1
     */
    public fun scalarMultiply(scalar: BigComplex, mathContext: MathContext): BigComplexVector = BigComplexVector(
        indexToElement.map { (i, e) -> i to scalar.multiply(e, mathContext) }.toMap()
    )

    /**
     * Returns the negated [AbstractVector] based on the [mathContext]
     *
     * @since 0.0.1
     */
    public fun negate(mathContext: MathContext): BigComplexVector =
        BigComplexVector(indexToElement.map { (i, e) -> i to e.negate(mathContext) }.toMap())

    override fun taxicabNorm(): BigDecimal = elements.map(BigComplex::abs).reduce(BigDecimal::add)

    /**
     * Returns the taxicab norm based on the [mathContext]
     *
     * @since 0.0.1
     */
    public fun taxicabNorm(mathContext: MathContext): BigDecimal = elements.map { it.abs(mathContext) }
        .reduce { a, b -> a.add(b, mathContext) }

    override fun euclideanNormPow2(): BigComplex = this * this

    override fun euclideanNorm(): BigDecimal = elements.map { it.abs().pow(2) }.reduce(BigDecimal::add).sqrt()

    /**
     * Returns the euclidean norm based on the [mathContext]
     *
     * @since 0.0.1
     */
    public fun euclideanNorm(mathContext: MathContext): BigDecimal = elements.map { it.abs(mathContext).pow(2) }
        .reduce { a, b -> a.add(b, mathContext) }
        .sqrt(mathContext)

    override fun maxNorm(): BigDecimal = elements.map(BigComplex::abs).maxOrNull() as BigDecimal

    /**
     * Returns the maximum norm based on the [mathContext]
     *
     * @since 0.0.1
     */
    public fun maxNorm(mathContext: MathContext): BigDecimal =
        elements.map { it.abs(mathContext) }.maxOrNull() as BigDecimal

    public companion object {
        /**
         * Returns a [BigComplexVectorJavaBuilder] of [size]
         *
         * @since 0.0.1
         */
        @JvmStatic
        public fun builder(size: Int): BigComplexVectorJavaBuilder = BigComplexVectorJavaBuilder(size)
    }
}
