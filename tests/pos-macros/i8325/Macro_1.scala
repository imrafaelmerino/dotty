package a

import scala.quoted._


object A:

  inline def transform[A](inline expr: A): A = ${
    transformImplExpr('expr)
  }

  def pure[A](a:A):A = ???

  def transformImplExpr[A:Type](using qctx: QuoteContext)(expr: Expr[A]): Expr[A] = {
     import qctx.reflect._
     Term.of(expr) match {
         case Inlined(x,y,z) => transformImplExpr(z.asExpr.asInstanceOf[Expr[A]])
         case Apply(fun,args) =>  '{  A.pure(${Apply(fun,args).asExpr.asInstanceOf[Expr[A]]}) }
         case other => expr
     }
  }
