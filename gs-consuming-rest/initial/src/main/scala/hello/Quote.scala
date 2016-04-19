package hello

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import scala.beans.BeanProperty


@JsonIgnoreProperties(ignoreUnknown = true)
class Value {
  @BeanProperty
  var id: Long  = _
  @BeanProperty
  var quote: String = _

  override def toString : String = "Value{id=" + id +", quote=" + quote +"}"
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Quote {

  var typeValue: String = _
  def getType : String = typeValue
  def setType(value: String) = typeValue = value

  @BeanProperty
  var value: Value = _


  override def toString: String =  "Quote{" + "type='" + typeValue + "'" + ", value=" + value + "}"

}

