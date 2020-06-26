package objects

import io.gatling.core.Predef.{exec, _}
import io.gatling.http.Predef._
import scala.util.Random
import java.time.LocalDate.now

object PetControllerApiRequestProvider {

  val random : Random = new Random()

  def getInitUpdateForm(petId: String) =
    exec(
      http("Get 'Update Form' API request")
        .get("http://localhost:8080/pets/" + petId + "/edit")
        .header("Content-Type", "application/json")
        .check(status.is(200))
        .check(ResponseCheckUtils.getRandomObjectFromJson("$.titles[*].vlaue", "randomTitleValue"))
    )

  def getOwnerDetailsById(ownerId: String) =
    exec(
      http("Get Owner Details by OwnerId API request")
        .get("/owners/" + ownerId)
        .check(status.is(200))
        .check(ResponseCheckUtils.getRandomObjectFromJson("$..pet[*].id", "randomPetId"))
    )

  def getInitNewVisitDataByOwnerIdAndPetId(ownerId: String, petId: String) =
    exec(
      http("Get Initial data for 'Book new visit' pop-up")
        .get("/owners/" + ownerId + "/pets/" + petId + "/visits/new")
        .check(status.is(200))
    )

  def postNewVisitDataByOwnerIdAndPetIdWithRandomDescriptionAndDate(ownerId: String, petId: String) =
    exec(
      http("Post 'New visit' with Description and Date")
        .post("/owners/" + ownerId + "/pets/" + petId + "/visits/new")
        .formParam("date", now().plusDays(random.nextInt(20)))
        .formParam("description",random.nextString(8))
        .check(status.is(302))
    )
}

