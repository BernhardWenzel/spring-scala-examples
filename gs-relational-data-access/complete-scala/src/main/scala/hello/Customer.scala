package hello

class Customer(id: Long, firstName: String, lastName: String) {
    override def toString: String = "Customer[id=%s, firstName=%s, lastName=%s]".format(id, firstName, lastName)
}
