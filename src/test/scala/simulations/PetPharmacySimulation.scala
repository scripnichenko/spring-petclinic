package simulations


import io.gatling.core.Predef.{exec, scenario, _}
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import scala.concurrent.duration._

class PetPharmacySimulation extends Simulation {
  //  val scn = scenario("MyFirstScenario").repeat(30, "n") {
  //    exec(
  //      http("Find Owner - API")
  //        .get("http://localhost:8080/owners/10")
  //        .header("Content-Type", "application/json")
  //        .check(status.is(200))
  //    )
  //  }
  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .disableWarmUp
    .maxConnectionsPerHostLikeChrome
    .basicAuth("login", "pass")
    .acceptCharsetHeader("*/*")
    .userAgentHeader("Mozilla/5.0 (Windows NT 6.1; Win64; x64) " +
      "AppleWebKit/537.36 (KHTML, like Gecko)" +
      " Chrome/83.0.4103.116 Safari/537.36")
  //
  //Open model example
  //  setUp(scn.inject(
  //    incrementUsersPerSec(5)
  //      .times(5)
  //      .eachLevelLasting(10 seconds)
  //      .separatedByRampsLasting(10 seconds)
  //      .startingFrom(10)
  //  ))
  //    .protocols(httpProtocol)
  //    .maxDuration(1 minute)
  //    .assertions(
  //      global.responseTime.max.lt(3000),
  //      global.responseTime.percentile3.lt(500),
  //      global.successfulRequests.percent.gt(95)
  //    )


  val scn = scenario("FindUsersTestScenario").repeat(30, "n") {
    exec(
      http("Find User - API")
        .get("http://localhost:8080/owners/10")
        .header("Content-Type", "application/json")
        .check(status.is(200))
    )
  }

  //  @GetMapping("/owners/*/pets/{petId}/visits/new")
  //    public String initNewVisitForm(@PathVariable("petId") int petId, Map<String, Object> model) {
  //        return "pets/createOrUpdateVisitForm";
  //    }

  val regularClientScenario = scenario("BookNewVisit") {
    exec(
      http("Client: Book new visit")
        .post("/owners/*/pets/2/visits/new")
        .header("Content-Type", "application/json")
        .check(status.is(200))
    )
  }

  val doctorScenario = scenario("CheckDoctorsSchedule") {
    exec(
      http("Doctor: Get my schedule")
        .get("/doctors/10/visits")
        .header("Content-Type", "application/json")
        .check(status.is(200))
    )
  }

  //  setUp(
  //    scn.inject(
  //      incrementUsersPerSec(5)
  //        .times(5)
  //        .eachLevelLasting(10 seconds)
  //        .separatedByRampsLasting(10 seconds)
  //        .startingFrom(10)
  //    )
  //  )
  //    .maxDuration(1 minute)
  //    .assertions(
  //      global.responseTime.max.lt(1000),
  //      global.responseTime.percentile3.lt(500),
  //      global.successfulRequests.percent.gt(95)
  //    )

  setUp(
    // Open Model
    regularClientScenario.inject(
      incrementUsersPerSec(5)
        .times(10)
        .eachLevelLasting(10 seconds)
        .separatedByRampsLasting(10 seconds)
        .startingFrom(10)
    ),
    //Closed Model
    doctorScenario.inject(
      incrementConcurrentUsers(2)
        .times(6)
        .eachLevelLasting(10 seconds)
        .separatedByRampsLasting(10 seconds)
        .startingFrom(1)
    )
  )
    .maxDuration(1 minute)
    .assertions(
      global.responseTime.max.lt(1000),
      global.responseTime.percentile3.lt(500),
      global.successfulRequests.percent.gt(95)
    )

}
