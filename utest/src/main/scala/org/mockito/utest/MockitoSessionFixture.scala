package org.mockito.utest

import java.util.concurrent.{ Executors, ThreadFactory }
import org.mockito.MockitoScalaSession
import scala.concurrent.{ ExecutionContext, Future }
import utest._
import utest.framework.Executor

trait MockitoSessionFixture extends Executor {
  override def utestWrap(path: Seq[String], runBody: => concurrent.Future[Any])(implicit ec: ExecutionContext) = {
    val mockEc = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor(new ThreadFactory {
      def newThread(r: Runnable): Thread = new Thread(r, s"mocks for ${path.mkString(".")}")
    }))

    for {
      session <- Future(MockitoScalaSession())(mockEc)
      result <-
        super
          .utestWrap(path, runBody)
          .transform { result =>
            session.finishMocking(result.toEither.swap.toOption)
            result
          }(mockEc)
    } yield result
  }
}
