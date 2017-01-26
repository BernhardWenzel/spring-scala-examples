package hello

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
trait PersonRepository extends PagingAndSortingRepository[Person, java.lang.Long] {
	def findByLastName(@Param("name") name: String): List[Person]
}
