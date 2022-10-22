package kickstart.db


import com.vtence.kabinet.Expression
import com.vtence.kabinet.Field
import com.vtence.kabinet.SqlBuilder
import com.vtence.kabinet.append


interface BooleanOp : Expression<Boolean>

class IsNull(private val expression: Expression<*>) : BooleanOp {
    override fun build(statement: SqlBuilder) = statement {
        append(expression, " IS NULL")
    }
}

val Expression<*>.isNull: BooleanOp get() = IsNull(this)


abstract class Comparison(val symbol: String, protected val left: Expression<*>, protected val right: Expression<*>) : BooleanOp {
    override fun build(statement: SqlBuilder) = statement {
        append(left, " $symbol ", right)
    }
}

class Eq(left: Expression<*>, right: Expression<*>) : Comparison("=", left, right), BooleanOp

infix fun <T> Expression<T>.eq(value: Expression<T>): BooleanOp = Eq(this, value)

infix fun <T> Field<T>.eq(value: T): BooleanOp = when (value) {
    null -> isNull
    else -> eq(value.asParameter())
}