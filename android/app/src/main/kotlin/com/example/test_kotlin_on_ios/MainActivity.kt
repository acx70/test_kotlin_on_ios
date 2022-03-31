// tony

package com.example.test_kotlin_on_ios

import io.flutter.embedding.android.FlutterActivity

import android.os.Bundle

import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.JarikoCallback
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.interpreter.ISymbolTable
import com.smeup.rpgparser.interpreter.IntValue
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import java.util.*

class MainActivity: FlutterActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GeneratedPluginRegistrant.registerWith(this.flutterEngine!!)
        
        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { methodCall, result ->
            if (methodCall.method == "InvokeJariko") {
                val num = methodCall.arguments<String>();
                result.success(calcFibonacci(num))
            }
        }
    }

    companion object {
        private const val CHANNEL = "invokeJavaClass"
    }

    fun calcFibonacci(input: String) : Long{
        val pgm = "     V*===================================================================== \n" +
                "     V* Description:\n" +
                "     V* This is a sample of an rpgle program with fibonacci series calculation \n" +
                "     V*===================================================================== \n" +
                "     D ppdat           S              8\n" +
                "     D NBR             S             21  0\n" +
                "     D RESULT          S             21  0 INZ(0)\n" +
                "     D FINAL_VAL       S             21  0 INZ(0)\n" +
                "     D COUNT           S             21  0\n" +
                "     D A               S             21  0 INZ(0)\n" +
                "     D B               S             21  0 INZ(1)\n" +
                "     C     *entry        plist\n" +
                "     C                   parm                    ppdat                          I\n" +
                "      *\n" +
                "     C                   Eval      NBR    = %Dec(ppdat : 8 : 0)\n" +
                "     C                   EXSR      FIB\n" +
                "     C                   EVAL      FINAL_VAL = RESULT\n" +
                "     C                   seton                                        lr\n" +
                "      *--------------------------------------------------------------*\n" +
                "     C     FIB           BEGSR\n" +
                "     C                   SELECT\n" +
                "     C                   WHEN      NBR = 0\n" +
                "     C                   EVAL      RESULT = 0\n" +
                "     C                   WHEN      NBR = 1\n" +
                "     C                   EVAL      RESULT = 1\n" +
                "     C                   OTHER\n" +
                "     C                   FOR       COUNT = 2 TO NBR\n" +
                "     C                   EVAL      RESULT = A + B\n" +
                "     C                   EVAL      A = B\n" +
                "     C                   EVAL      B = RESULT\n" +
                "     C                   ENDFOR\n" +
                "     C                   ENDSL\n" +
                "     C                   ENDSR\n" +
                "      *--------------------------------------------------------------*\n" +
                "\n"
        val systemInterface = JavaSystemInterface()
        val clm = getProgram(systemInterface = systemInterface, nameOrSource = pgm)
        var output: Long = 0
        val config = Configuration(jarikoCallback = JarikoCallback(
                onExitPgm = { _: String, symbolTable: ISymbolTable, _: Throwable? ->
                    output = (symbolTable["FINAL_VAL"] as IntValue).value
                }

        ))
        clm.singleCall(parms = listOf(input), configuration = config)
        return output
    }
}
