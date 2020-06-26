package util
import io.gatling.core.Predef.{exec, _}
import io.gatling.http.Predef._

object ResponseCheckUtils {
  def getRandomObjectFromJson(jsonPathString: String, variable: String): HttpCheck = {
    jsonPath(jsonPathString).findAll.transform(util.Random.shuffle(_))
      .transform(_.toList.get.head).saveAs(variable)
  }
}
