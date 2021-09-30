# Pattern-Matching
This is a program that performs pattern matching on strings in Scala without using regular expressions or Scala's regular expression libraray.

An ambiguous grammar for patterns was supplied in order to write a recursive descent parser.
The ambiguous grammar is as follows:

<ul>
  <li>S  -: E$*</li>
  <li>E  -: T E2</li>
  <li>E2 -: '|' E3</li>
  <li>E2 -: NIL*</li>
  <li>E3 -: T E2</li>
  <li>T  -: F T2</li>
  <li>T2 -: F T2</li>
  <li>T2 -: NIL*</li>
  <li>F  -: A F2</li>
  <li>F2 -: '?' F2</li>
  <li>F2 -: NIL*</li>
  <li>A  -: C</li>
  <li>A  -: '(' A2</li>
  <li>A2 -: E ')'</li>
</ul>

*$ is eof/end-of-string

*NIL means empty


Read more here: https://danielschlegel.org/wp/teaching/csc344-spring-2021/assignment-3/
