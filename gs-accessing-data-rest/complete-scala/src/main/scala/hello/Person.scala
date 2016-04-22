package hello

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

import scala.beans.BeanProperty

@Entity
class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@BeanProperty
	var id: Long = _

	@BeanProperty
	var firstName: String = _

	@BeanProperty
	var lastName: String = _

}
