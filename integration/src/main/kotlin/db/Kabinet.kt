package kickstart.db


import com.vtence.kabinet.Expression
import com.vtence.kabinet.SqlBuilder
import com.vtence.kabinet.append


class IsNull(private val expression: Expression) : Expression {
    override fun build(statement: SqlBuilder) = statement {
        +expression
        +" IS NULL"
    }
}

val Expression.isNull: Expression get() = IsNull(this)


abstract class Comparison(val left: Expression, val right: Expression, val symbol: String) : Expression {
    override fun build(statement: SqlBuilder) = statement {
        append(left, " $symbol ", right)
    }
}


class Eq(left: Expression, right: Expression) : Comparison(left, right, "="), Expression

infix fun Expression.eq(value: Expression): Expression = Eq(this, value)

infix fun Expression.eq(value: Any?) = when (value) {
    null -> isNull
    else -> eq(value.asArgument())
}