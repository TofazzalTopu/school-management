package com.grailslab.myapp.config

import com.grailslab.myapp.upscript.MyappUpscriptExecutor
import org.springframework.core.env.Environment

/**
 * Created by Aminul on 4/1/2017.
 */
class MyappStartupConfiguration implements MyappUpscriptExecutor{

    void handleStartupConfiguration(Environment environment) {
        println "\nloading external configuration ..."
        println "finished loading external configuration ...\n"

        if (environment.getProperty(ConfigKey.STARTUP_UPSCRIPT_ENABLED, Boolean)) {
            println "starting upscript execution ..."
            MyappUpscriptExecutor.super.executeUpscript(environment)
            println "... finished upscript execution"
        }
    }
}
