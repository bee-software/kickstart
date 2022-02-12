package kickstart


typealias UnitOfWork<T> = () -> T

interface Transactor {
    operator fun <T> invoke(work: UnitOfWork<T>): T
}
