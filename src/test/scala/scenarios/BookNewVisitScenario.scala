package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object BookNewVisitScenario {
  val ownerIdDataFeder = jsonfile("OwnerIdList.json").random
  val ownerId = "${ownerId}"
  val petId = "${petId_from_json}"
  val scn = scenario("Book new visit by Client scenario")
    .feed(ownerIdDataFeder)
    .exec(getOwnerDetailsById(ownerId))
    .exec(session => {
      val petIdRandomValue = session("randomPetId").as[String]
      session.set("petId_from_json", petIdRandomValue)
    })
    .exec(getInitNewVisitDataByOwnerIdAndPetId(ownerId, petId))
    .exec(postNewVisitDataByOwnerIdAndPetIdWithRandomDescriptionAndDate(ownerId, petId))
    .exec(session => {
      println(" === owner id === ")
      println(session("ownerId").as[String])
      println(" === pet id === ")
      println(session("petId_from_json").as[String])
      session
    })
  )
}

