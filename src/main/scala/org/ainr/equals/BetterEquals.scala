package org.ainr.equals

import scala.tools.nsc
import nsc.Global
import nsc.Phase
import nsc.plugins.Plugin
import nsc.plugins.PluginComponent

class BetterEquals(val global: Global) extends Plugin {
  import global._

  val name = "better-equals"
  val description = "checks for equals"
  val components = List[PluginComponent](Component)

  private object Component extends PluginComponent {
    val global: BetterEquals.this.global.type = BetterEquals.this.global
    val runsAfter = List[String]("refchecks")
    val phaseName = BetterEquals.this.name
    def newPhase(_prev: Phase) = new DivByZeroPhase(_prev)
    class DivByZeroPhase(prev: Phase) extends StdPhase(prev) {
      override def name = BetterEquals.this.name
      def apply(unit: CompilationUnit): Unit = {
        for (tree@Apply(Select(left, nme.EQ), List(right)) <- unit.body if left.tpe != right.tpe) {
          global.reporter.error(tree.pos, s"Comparison error due to type mismatch:\n${left.tpe} != ${right.tpe}")
        }
        for (tree@Apply(Select(left, nme.NE), List(right)) <- unit.body if left.tpe != right.tpe) {
          global.reporter.error(tree.pos, s"Comparison error due to type mismatch:\n${left.tpe} != ${right.tpe}")
        }
      }
    }
  }
}