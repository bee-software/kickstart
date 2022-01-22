package kickstart.security

class InMemoryUsers: UserBase {
    private val users = mutableListOf<User>()

    fun add(user: User) {
        users += user
    }

    override fun findBy(username: Username): User? {
        return users.singleOrNull { it.username == username }
    }
}