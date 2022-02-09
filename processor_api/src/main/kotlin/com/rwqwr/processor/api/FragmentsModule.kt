package com.rwqwr.processor.api

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
public annotation class FragmentsModule(val generateFactory: Boolean = false)
