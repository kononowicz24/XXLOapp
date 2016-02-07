
package com.kononowicz24.xxlo

import android.app.Activity
import android.os.Bundle

class MainActivity extends Activity with TypedFindView {

  //override def onCreate(bundle: Bundle) {
  //  super.onCreate(bundle)
  //  setContentView(R.layout.main)
  //  findView(TR.textview).setText(R.string.hello_world)
  //}

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
    setContentView(R.layout.main)
	
	  import java.io._
    import java.net._

    var url: URL = null
    var conn: HttpURLConnection = null
    //var rd: BufferedReader = null
    var result = ""
    val userFullName = "II b"
    try {
      //pobieranie lewej strony menu
      url = new URL("http://xxlo.pl/dokumenty/subst_left.htm")
      conn = url.openConnection().asInstanceOf[HttpURLConnection]
      findView(TR.progressBar).setProgress(10)
      conn.setRequestMethod("GET")
      findView(TR.progressBar).setProgress(20)
      val rd:BufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()))
      findView(TR.progressBar).setProgress(30)
      while (rd.ready()) {
        result += rd.readLine()
      }
      findView(TR.progressBar).setProgress(40)
      rd.close()
      //pobranie właściwego dokumentu
      val patern =
        """href=(.+?\.htm)""".r //"""href=([^>]+?[1]\.htm)""".r
      val href = patern.findFirstIn(result)
      findView(TR.progressBar).setProgress(50)
      if (href.isDefined) {
        val urlData = new URL("http://xxlo.pl/dokumenty/" + href.get.drop(5))
        result = ""
        conn = urlData.openConnection().asInstanceOf[HttpURLConnection]
        conn.setRequestMethod("GET")
        val rd2 = new BufferedReader(new InputStreamReader(conn.getInputStream))
        while (rd2.ready()) {
          result += rd2.readLine()
        }
        rd2.close()
        val reg = new scala.util.matching.Regex(userFullName)
        val m = reg.findFirstMatchIn(result)
        if (m.isDefined)

          findView(TR.textview).setText(R.string.sa_zastepstwa + userFullName + R.string.on_page + """(""" + urlData.toString + """)""")
        else
          findView(TR.textview).setText(R.string.brak_zastepstw + userFullName + R.string.on_page + """(""" + urlData.toString + """)""")
      }
      else findView(TR.textview).setText(R.string.unavail2)
    } catch {
      case e: IOException => findView(TR.textview).setText(R.string.io_error + " " + url.toString)
      case e: Exception => findView(TR.textview).setText(e.getLocalizedMessage + e.getMessage + e.toString)
    }
  }
}
