package acolyte.reactivemongo

import reactivemongo.bson.{ BSONDocument, BSONInteger, BSONString }

object QueryResponseSpec
    extends org.specs2.mutable.Specification with ResponseMatchers {

  "Query response" title

  "Reponse" should {
    "be made for error message" >> {
      "using generic factory" in {
        QueryResponse("error message #1") aka "prepared" must beLike {
          case prepared ⇒ prepared(1) aka "applied" must beSome.which(
            _ aka "query response" must beQueryError("error message #1"))
        }
      }

      "using named factory" in {
        QueryResponse.failed("error message #2") aka "prepared" must beLike {
          case prepared ⇒ prepared(2) aka "applied" must beSome.which(
            _ aka "query response" must beQueryError("error message #2"))
        }
      }
    }

    "be made for error with code" >> {
      "using generic factory" in {
        QueryResponse("error message #3" -> 5) aka "prepared" must beLike {
          case prepared ⇒ prepared(1) aka "applied" must beSome.which(
            _ aka "query response" must beQueryError(
              "error message #3", Some(5)))
        }
      }

      "using named factory" in {
        QueryResponse.failed("error message #4", 7) aka "prepared" must beLike {
          case prepared ⇒ prepared(2) aka "applied" must beSome.which(
            _ aka "query response" must beQueryError(
              "error message #4", Some(7)))
        }
      }
    }

    "be made for successful result" >> {
      val Doc1 = BSONDocument("a" -> "b")

      "with a single document using generic factory" in {
        QueryResponse(Doc1) aka "prepared" must beLike {
          case prepared ⇒ prepared(3) aka "applied" must beSome.which(
            _ aka "query response" must beResponse {
              case ValueDocument(("a", BSONString("b")) :: Nil) :: Nil ⇒ ok
            })
        }
      }

      "with a single document using named factory" in {
        QueryResponse.successful(Doc1) aka "prepared" must beLike {
          case prepared ⇒ prepared(3) aka "applied" must beSome.which(
            _ aka "query response" must beResponse {
              case ValueDocument(("a", BSONString("b")) :: Nil) :: Nil ⇒ ok
            })
        }
      }

      "with a many documents using generic factory" in {
        QueryResponse(Seq(Doc1, BSONDocument("b" -> 2))).
          aka("prepared") must beLike {
            case prepared ⇒ prepared(3) aka "applied" must beSome.which(
              _ aka "response" must beResponse {
                case Doc1 :: ValueDocument(
                  ("b", BSONInteger(2)) :: Nil) :: Nil ⇒ ok
              })
          }
      }

      "with a many documents using named factory" in {
        QueryResponse.successful(Doc1, BSONDocument("b" -> 3)).
          aka("prepared") must beLike {
            case prepared ⇒ prepared(3) aka "applied" must beSome.which(
              _ aka "response" must beResponse {
                case Doc1 :: ValueDocument(
                  ("b", BSONInteger(3)) :: Nil) :: Nil ⇒ ok
              })
          }
      }
    }

    "be undefined" >> {
      "using generic factory" in {
        QueryResponse(None) aka "prepared" must beLike {
          case prepared ⇒ prepared(1) aka "applied" must beNone
        }
      }

      "using named factory" in {
        QueryResponse.empty aka "prepared" must beLike {
          case prepared ⇒ prepared(2) aka "applied" must beNone
        }
      }
    }
  }
}