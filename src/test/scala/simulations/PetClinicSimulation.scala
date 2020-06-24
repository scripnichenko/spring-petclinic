package simulations;

import io.gatling.core.Predef.{atOnceUsers, exec, scenario, _}
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import scala.concurrent.duration._
class PetClinicSimulation extends Simulation {
  val scn = scenario("FindUsersTestScenario").repeat(30, "n") {
    exec(
      http("Find User - API")
        .get("http://localhost:8080/owners/10")
        .header("Content-Type", "application/json")
        .check(status.is(200))
    )
  }

  setUp(scn.inject(atOnceUsers(5)))
    .maxDuration(1 minute)
    .assertions(
      global.responseTime.max.lt(1000),
      global.responseTime.percentile3.lt(500),
      global.successfulRequests.percent.gt(95)
    )

}
