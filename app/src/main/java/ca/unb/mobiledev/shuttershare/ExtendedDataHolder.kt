package ca.unb.mobiledev.shuttershare

//data class ExtendedDataHolder()
class ExtendedDataHolder private constructor() {
    private val extras: MutableMap<String, Any> = HashMap()
    fun putExtra(name: String, `object`: Any) {
        extras[name] = `object`
    }

    fun getExtra(name: String): Any? {
        return extras[name]
    }

    fun hasExtra(name: String): Boolean {
        return extras.containsKey(name)
    }

    fun clear() {
        extras.clear()
    }

    companion object {
        val instance = ExtendedDataHolder() // singleton instance
    }
}
