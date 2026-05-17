package com.klen0010.flinders.zootreasurehunt.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StepCounterManager(context: Context) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    val hasStepCounter: Boolean
        get() = stepSensor != null

    private val _steps = MutableStateFlow(0)
    val steps: StateFlow<Int> = _steps

    private var initialSteps = -1

    fun startListening() {
        stepSensor?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        val totalSteps = event.values[0].toInt()

        if (initialSteps == -1) {
            initialSteps = totalSteps
        }

        _steps.value = totalSteps - initialSteps
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}