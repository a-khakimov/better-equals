
import munit.FunSuite

class Tests extends FunSuite {
  val a = 1
  val b = "21"

  case class Foo(a: String)
  case class Bar(b: String)

  test("Simple ....-.") {
    assertEquals(
      Foo("1") == Foo("1"),
      false
    )
  }

  test("Simple ......") {
    assertEquals(
      a == b,
      false
    )
  }
}
