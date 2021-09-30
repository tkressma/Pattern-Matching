// The ambiguous grammar used for patterns:
//S  -: E$
//E  -: T E2
//E2 -: '|' E3
//E2 -: NIL
//E3 -: T E2
//T  -: F T2
//T2 -: F T2
//T2 -: NIL
//F  -: A F2
//F2 -: '?' F2
//F2 -: NIL
//A  -: C
//A  -: '(' A2
//A2 -: E ')'

package Simplify.Main
import scala.io.StdIn.readLine

class RecursiveDescent(input: String) {
  var index = 0;

  // Grammar rules
  abstract class S

  case class E(left: T, right: Option[E2]) extends S {
    def eval(): Boolean = {
      val tempPosition = Main.position
      right match {
        case Some(e2) => if (!left.eval()) {
          if (right.isEmpty) {
            false
          } else {
          Main.position = tempPosition
          e2.eval()
          }
        } else true
        case None => left.eval()
      }
    }
  }

  case class E2(left: E3) extends S {
    def eval(): Boolean = left.eval()
  }

  case class E3(left: T, right: Option[E2]) extends S {
    def eval(): Boolean = {
      val tempPosition = Main.position
      right match {
        case Some(e2) => if (!left.eval()) {
          Main.position = tempPosition
          left.eval || e2.eval()
        } else true
        case None => left.eval()
      }
    }
  }

  case class T(left: F, right: Option[T2]) extends S {
    def eval(): Boolean = {
      val lr: Boolean = left.right.isDefined
      val tempPosition = Main.position
      right match {
        case Some(t2) =>
          if (lr && t2.eval()) true else {
            Main.position = tempPosition
            (left.eval() && t2.eval())
          }
        case None => left.eval()
      }
    }
  }

  case class T2(left: F, right: Option[T2]) extends S {
    def eval(): Boolean = {
      val lr: Boolean = left.right.isDefined
      val tempPosition = Main.position
      right match {
        case Some(t2) => if (lr && t2.eval()) true else {
          Main.position = tempPosition
          (left.eval() && t2.eval())
        }
        case None => left.eval()
      }
    }
  }

  case class F(left: A, right: Option[F2]) extends S {
    def eval(): Boolean = {
        right match {
          case Some(_) => if (!left.eval()) Main.position+=1; true
          case None => left.eval()
        }
    }
  }

  case class F2(left: Option[F2]) extends S

  abstract class A extends S {
    def eval(): Boolean
  }

  case class A1(left: A2) extends A {
    def eval(): Boolean = left.eval()
  }
  case class A2(left: E) extends A {
    def eval(): Boolean = left.eval()
  }
  case class C(left: Char) extends A {
    def eval(): Boolean = {
      if (Main.inputString.charAt(Main.position) == left || left == '.') {
        // println("Pos: " + Main.position + " char: " + Main.inputString.charAt(Main.position) + " left: " + left)
        Main.position+=1;
        true
      }
      else false
    }

  }

  // Parse rule definitions
  def parseE(): E = E(parseT(), parseE2())

  def parseE2(): Option[E2] = {
    if (index < input.length && input(index) == '|') {
      //println('|')
      index+=1
      Some(E2(parseE3()))
    } else if (index < input.length() && input(index) == ')') {
      //println(')')
      index+=1
      None
    }
    else None
  }

  def parseE3(): E3 = E3(parseT(), parseE2())

  def parseT(): T = T(parseF(), parseT2())

  def parseT2(): Option[T2] = {
    if (index < input.length && input(index) != '|' && input(index) != ')') {
      Some(T2(parseF(), parseT2()))
    }
    else None
  }

  def parseF(): F = F(parseA(), parseF2())

  def parseF2(): Option[F2] = {
    if (index < input.length && input(index) == '?' ) {
      index+=1
      Some(F2(parseF2()))
    }
    else None
  }

  def parseA(): A = {
    if (index < input.length && input(index) == '(') {
      index+=1
      //println('(')
      A1(parseA2())
    } else {
      index+=1
      parseC()
    }
  }

  def parseA2(): A2 = A2(parseE())

  def parseC(): C = {
    //println("current char: " + input.charAt(index-1) + " index: " + index)
    C(input.charAt(index - 1))
  }

}

object Main {

  var position = 0
  var inputString = ""

  def main(args: Array[String]) = {

    val pattern: String = readLine("pattern? ")
    val rd = new RecursiveDescent(pattern)
    val parserTree = rd.parseE()
    //println(parserTree)

    // Retrieve test string from user
    Main.inputString = readLine("string? ")

    // Allow user to keep entering new strings until they end the program
    while (Main.inputString != "end") {
      val eval = parserTree.eval()
      if (eval && (!(Main.position < Main.inputString.length() - 1)))
        println("match")
      else
        println("no match")
      Main.inputString = readLine("string? ")
      Main.position = 0
    }
  }

  // Test patterns:
  // I (like|love|hate)( (cat|dog))? people
  // ((h|j)ell. worl?d)|(42)

}
