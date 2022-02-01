package com.rwqwr.processor.api

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class FragmentsModule(val generateFactory: Boolean = false)
